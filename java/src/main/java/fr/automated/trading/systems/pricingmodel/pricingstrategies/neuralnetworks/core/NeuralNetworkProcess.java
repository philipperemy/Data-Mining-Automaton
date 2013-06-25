package fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.core;

import fr.automated.trading.systems.exception.ErrorCalculusException;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.error.ErrorCalculus;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.error.ErrorSimpleCalculus;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.error.ErrorValues;
import fr.automated.trading.systems.tradingrobot.datamanager.DataManager;
import fr.automated.trading.systems.utils.utils.AtsLogger;
import fr.automated.trading.systems.utils.utils.SharedMemory;
import fr.automated.trading.systems.utils.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NeuralNetworkProcess {

    protected NeuralNetwork neuralNetwork;
    protected DataManager dataManager;
    protected ErrorCalculus errorCalculus = new ErrorSimpleCalculus();

    public void trainProcess(double learningRate, double momentum) {
        int delimiter = getDelimiter();
        DataManager.DataManagerIterator dataManagerIterator = dataManager.iterator();
        while (dataManagerIterator.hasNext() && dataManagerIterator.getPosition() < delimiter) {
            DataManager.DataManagerUnit dataUnit = dataManagerIterator.next();
            double[] inputs = Utils.doubleListToArray(dataUnit.getInputVectors());
            double expectedValue = dataUnit.getExpectedValue();

            AtsLogger.log("line = " + dataManagerIterator.getPosition() + " inputs = " + Arrays.toString(inputs) + " expected = " + expectedValue);
            neuralNetwork.train(inputs, expectedValue, learningRate, momentum);
        }
    }

    //TODO test it
    public double predictProcess() {
        List<Double> lastVector = dataManager.getLastVector();
        double[] inputs = Utils.doubleListToArray(lastVector);
        return runningProcess(inputs);
    }

    /**
     * The goal of RunningProcess is to compute the value of the neural network for an inputVector.
     * In theory, it must not be passed the expected values.
     */
    public double runningProcess(double[] inputs) {
        return neuralNetwork.run(inputs);
    }

    //TODO test it
    public double errorProcess() {
        try {
            int delimiter = getDelimiter();
            DataManager.DataManagerIterator dataManagerIterator = dataManager.iterator();
            dataManagerIterator.setPosition(delimiter);

            List<Double> computedValues = new ArrayList<>();
            List<Double> expectedValues = new ArrayList<>();
            while (dataManagerIterator.hasNext()) {
                DataManager.DataManagerUnit dataUnit = dataManagerIterator.next();
                computedValues.add(runningProcess(Utils.doubleListToArray(dataUnit.getInputVectors())));
                expectedValues.add(dataUnit.getExpectedValue());
            }

            return errorCalculus.calculate(computedValues, expectedValues);

        } catch (ErrorCalculusException e) {
            AtsLogger.logException("Error in neural network process error", e);
            return 0.0;
        }
    }

    //TODO test it
    public ErrorValues errorProcessValues() {

        try {

            int delimiter = getDelimiter();
            DataManager.DataManagerIterator dataManagerIterator = dataManager.iterator();
            dataManagerIterator.setPosition(delimiter);

            List<Double> computedValues = new ArrayList<>();
            List<Double> expectedValues = new ArrayList<>();

            while (dataManagerIterator.hasNext()) {
                DataManager.DataManagerUnit dataUnit = dataManagerIterator.next();
                computedValues.add(runningProcess(Utils.doubleListToArray(dataUnit.getInputVectors())));
                expectedValues.add(dataUnit.getExpectedValue());
            }

            return new ErrorValues(computedValues, expectedValues);

        } catch (Exception e) {
            AtsLogger.logException("error in process values", e);
        }

        return null;
    }

    /*public ErrorValues errorProcessValues() {

        try {
            int delimiter = getDelimiter();
            int end = dataManager.getResults().size();
            double runningValue, trueValue;

            List<Double> computedValues = new ArrayList<>();
            List<Double> expectedValues = new ArrayList<>();

            for(int line = delimiter; line<end; line++) {
                runningValue = runningProcess(line);
                trueValue = dataManager.getExpectedValues().get(line);

                computedValues.add(runningValue);
                expectedValues.add(trueValue);
            }

            return new ErrorValues(computedValues, expectedValues);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }*/

    public void debug(int line, double[] input, double expectedValue) {
        AtsLogger.log("----------------------------------------");
        AtsLogger.log("Line = " + line);
        Utils.displayArray(input);
        AtsLogger.log("");
        AtsLogger.log("expected value = " + expectedValue);
        AtsLogger.log("----------------------------------------");

    }

    public void setDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void setErrorCalculus(ErrorCalculus errorCalculus) {
        this.errorCalculus = errorCalculus;
    }

    public void setNeuralNetwork(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
    }

    public NeuralNetwork getNeuralNetwork() {
        return neuralNetwork;
    }

    public int getDelimiter() {
        int delimiter = (dataManager.getResults().size() * SharedMemory.percentage) / 100;
        SharedMemory.lastDelimiter = delimiter;
        return delimiter;
    }
}
