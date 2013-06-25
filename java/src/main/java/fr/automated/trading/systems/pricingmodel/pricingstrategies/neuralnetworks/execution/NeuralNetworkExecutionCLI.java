package fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.execution;

import fr.automated.trading.systems.exception.ErrorCalculusException;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.error.*;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.execution.NeuralNetworkExecutionResult;
import fr.automated.trading.systems.utils.utils.Constants;
import fr.automated.trading.systems.utils.utils.SharedMemory;

import java.util.Map;

public class NeuralNetworkExecutionCLI extends fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.execution.NeuralNetworkExecution {

    @Override
    public NeuralNetworkExecutionResult exec() throws ErrorCalculusException {
        debug();

        neuralNetworkProcess.setDataManager(dataManager);
        neuralNetworkProcess.setErrorCalculus(new ErrorSimpleCalculus());

        int bestEpoch = 0;
        double error, errorMin = Double.POSITIVE_INFINITY;
        for(int epochsIt = 0; epochsIt < SharedMemory.epochs; epochsIt++) {

            neuralNetworkProcess.trainProcess(SharedMemory.learningRate, SharedMemory.momentum);
            error = neuralNetworkProcess.errorProcess();

            if(error < errorMin) {
                errorMin = error; bestEpoch = epochsIt;
                neuralnetwork.saveWeights();
            }

            displayError(error, errorMin, bestEpoch, epochsIt);

        }

        neuralnetwork.loadWeights();

        ErrorValues errorValues = neuralNetworkProcess.errorProcessValues();

        ErrorValues errorTransformedValues = ErrorValuesTransformers.transform(errorValues);
        ErrorSimpleCalculus errorCalculus = new ErrorSimpleCalculus();
        errorCalculus.calculate(errorTransformedValues.getComputedValues(), errorTransformedValues.getExpectedValues());

        ErrorValues errorTransformedValues2 = ErrorSetReduction.transform(errorTransformedValues, dataManager, neuralNetworkProcess.getDelimiter());
        Map<String, Integer> modelErrors = ErrorInfoExtractor.extractModelError(errorTransformedValues2);


        NeuralNetworkExecutionResult result = new NeuralNetworkExecutionResult();
        result.setBestEpoch(bestEpoch);
        result.setTradable(userCanTrade());
        result.setModelError(modelErrors);
        double probabilityOk = ((double) modelErrors.get("OK")) / (modelErrors.get("OK") + modelErrors.get("KO"));
        result.setOkProbability(probabilityOk);
        result.setExpectedStockDirection(neuralNetworkProcess.predictProcess());
        result.setExpectedStockVariation(Math.abs(dataManager.getLastCsvLine()[Constants.OPEN_IDENTIFIER]-dataManager.getLastCsvLine()[Constants.CLOSE_IDENTIFIER]));

        return result;
    }

}
