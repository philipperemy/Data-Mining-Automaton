package fr.automated.trading.systems.tradingrobot.uppertier.multithread;

import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.activationfunction.ActivationFunctionSigmoid;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.core.NeuralNetwork;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.execution.NeuralNetworkExecution;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.execution.NeuralNetworkExecutionResult;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.execution.NeuralNetworkExecutionCLI;
import fr.automated.trading.systems.tradingrobot.datamanager.DataManager;
import fr.automated.trading.systems.utils.csvparser.CSVParser;
import fr.automated.trading.systems.utils.csvparser.CSVParserTransformer;
import fr.automated.trading.systems.utils.csvparser.CSVParserTransformerBinary;
import fr.automated.trading.systems.utils.utils.AtsLogger;
import fr.automated.trading.systems.utils.utils.Constants;
import fr.automated.trading.systems.utils.utils.SharedMemory;
import fr.automated.trading.systems.utils.utils.UIMode;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UnitaryRepeater implements Runnable {

    private String symbol;

    public UnitaryRepeater(String symbol) {
        this.symbol = symbol;
    }

    //careful of singletons!
    public void run() {

            SharedMemory.uiMode = UIMode.CLI_MODE;
            SharedMemory.epochs = 60;
            Constants.DISP_REFRESH_EPOCHS = 50;

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = dateFormat.format(new Date());

            Long start = System.currentTimeMillis();

            AtsLogger.log("symbol = " + symbol);
            String filename = "files/repeater/" + symbol + "_" + date + ".csv";

            //MarketData2 marketData = new MarketData2();
            //marketData.getMarketData(symbol, filename);

            AtsLogger.log("logging to filename " + filename);

            //No thread safe!!
            NeuralNetwork neuralnetwork = new NeuralNetwork()
                    .createInputLayer(SharedMemory.inputCount)
                    .createHiddenLayer(SharedMemory.hiddenCount)
                    .createOutputLayer(Constants.OUTPUT_COUNT)
                    .addActivationFunction(new ActivationFunctionSigmoid());

            CSVParser csvParser = new CSVParser(filename);
            CSVParserTransformer csvParserTransformer = new CSVParserTransformerBinary(csvParser);
            DataManager dataManager = new DataManager(csvParser.getValues(), csvParserTransformer.getValues());

            NeuralNetworkExecution execution = new NeuralNetworkExecutionCLI()
                    .setDataManager(dataManager)
                    .setNeuralNetwork(neuralnetwork);

            execution.getNeuralNetwork().resetWeights();
            NeuralNetworkExecutionResult result = null;

            try {
                result = (NeuralNetworkExecutionResult) execution.exec();
            } catch (Exception e) {
                AtsLogger.logException("", e);
            }


            long latency = System.currentTimeMillis()-start;
            AtsLogger.log("latency = " + latency);

            Collector.add(symbol, result, latency);

    }

}
