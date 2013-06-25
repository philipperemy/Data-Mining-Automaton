package fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.execution;

import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.core.NeuralNetwork;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.core.NeuralNetworkProcess;
import fr.automated.trading.systems.tradingrobot.datamanager.DataManager;
import fr.automated.trading.systems.utils.utils.AtsLogger;
import fr.automated.trading.systems.utils.utils.Constants;
import fr.automated.trading.systems.utils.utils.SharedMemory;
import fr.automated.trading.systems.utils.utils.Utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Map;

public abstract class NeuralNetworkExecution {

    protected DataManager dataManager;
    protected NeuralNetwork neuralnetwork;
    protected final NeuralNetworkProcess neuralNetworkProcess = new NeuralNetworkProcess();

    public abstract Object exec() throws Exception;

    protected boolean userCanTrade() {

        double[] lastLine = dataManager.getLastCsvLine();
        double computedValue = neuralNetworkProcess.predictProcess();
        boolean binaryComputedValue;
        if(computedValue > 0.50) {
            binaryComputedValue = true;
        } else {
            binaryComputedValue = false;
        }

        if(binaryComputedValue) {
            return(lastLine[Constants.OPEN_IDENTIFIER] > lastLine[Constants.CLOSE_IDENTIFIER]);
        } else {
            return(lastLine[Constants.OPEN_IDENTIFIER] < lastLine[Constants.CLOSE_IDENTIFIER]);
        }
    }

    protected void displayError(double error, double errorMin, int bestEpoch, int epochsIt) {
        if(epochsIt % Constants.DISP_REFRESH_EPOCHS == 0) {
            AtsLogger.log("error : " + error + " - error min : " + errorMin + " at epoch : " + bestEpoch + " epochs : " + epochsIt);
        }
    }

    protected void writeResults(Map<String, Integer> modelErrors, Boolean userCanTrade) {
        String filename = "files/results.txt";
        double probabilityOk = ((double) modelErrors.get("OK")) / (modelErrors.get("OK") + modelErrors.get("KO"));
        try {
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
            writer.write("[Results]");
            writer.println();

            writer.write("True prediction : ");
            writer.write(String.valueOf(modelErrors.get("OK")));
            writer.println();

            writer.write("Bad prediction : ");
            writer.write(String.valueOf(modelErrors.get("KO")));
            writer.println();

            writer.write("Probability(True) : ");
            writer.write(String.valueOf(Utils.truncate(probabilityOk, 3)));
            writer.println();

            if(userCanTrade) {
                writer.write("Expected stock direction : ");
                writer.write(String.valueOf(Utils.truncate(neuralNetworkProcess.predictProcess(),3)));
                writer.println();
                writer.write("If the expected value is greater than 0.5, the stock is expected to rise.");
                writer.println();
                writer.write("If the expected value is less greater than 0.5, the stock is expected to drop.");
                writer.println();
                writer.write("Expected variation : ");
                double expectedVariation = Math.abs(dataManager.getLastCsvLine()[Constants.OPEN_IDENTIFIER]-dataManager.getLastCsvLine()[Constants.CLOSE_IDENTIFIER]);
                writer.write(String.valueOf(Utils.truncate(expectedVariation, 3)));
                writer.println();

            } else {
                writer.write("Do not trade! This is not an optimal solution! Stay away from this equity");
                writer.println();
            }


            writer.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    protected void debug()  {
        try {
            AtsLogger.log("momentum = " + SharedMemory.momentum);
            AtsLogger.log("learning rate = " + SharedMemory.learningRate);
            AtsLogger.log("epochs = " + SharedMemory.epochs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NeuralNetworkExecution setNeuralNetwork(NeuralNetwork neuralnetwork) {
        this.neuralnetwork = neuralnetwork;
        neuralNetworkProcess.setNeuralNetwork(neuralnetwork);
        return this;
    }
    public NeuralNetwork getNeuralNetwork() {
        return neuralnetwork;
    }

    public NeuralNetworkExecution setDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
        return this;
    }

}