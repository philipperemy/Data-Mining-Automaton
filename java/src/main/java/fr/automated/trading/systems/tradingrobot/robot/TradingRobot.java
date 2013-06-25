package fr.automated.trading.systems.tradingrobot.robot;

import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.activationfunction.ActivationFunctionSigmoid;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.core.NeuralNetwork;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.core.NeuralNetworkFactory;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.execution.NeuralNetworkExecution;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.execution.NeuralNetworkExecutionFactory;
import fr.automated.trading.systems.tradingrobot.datamanager.DataManager;
import fr.automated.trading.systems.utils.csvparser.CSVParser;
import fr.automated.trading.systems.utils.csvparser.CSVParserTransformer;
import fr.automated.trading.systems.utils.csvparser.CSVParserTransformerBinary;
import fr.automated.trading.systems.utils.utils.AtsLogger;
import fr.automated.trading.systems.utils.utils.Constants;
import fr.automated.trading.systems.utils.utils.SharedMemory;

public abstract class TradingRobot implements IRobot {

	private fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.execution.NeuralNetworkExecution neuralnetworkexecution;

    private DataManager dataManager;

    //private final TradeRecords tradesRecords = new TradeRecordsImpl();
    //private final PricesRecord openPricesRecords = new OpenPricesRecord();

	public void run() {
		
		try {

            NeuralNetwork neuralnetwork = NeuralNetworkFactory.createInstance()
                    .createInputLayer(SharedMemory.inputCount)
                    .createHiddenLayer(SharedMemory.hiddenCount)
                    .createOutputLayer(Constants.OUTPUT_COUNT)
                    .addActivationFunction(new ActivationFunctionSigmoid());

            CSVParser csvParser = new CSVParser(SharedMemory.filename);
            CSVParserTransformer csvParserTransformer = new CSVParserTransformerBinary(csvParser);
            dataManager = new DataManager(csvParser.getValues(), csvParserTransformer.getValues());

            neuralnetworkexecution = NeuralNetworkExecutionFactory.createInstance()
                    .setDataManager(dataManager)
                    .setNeuralNetwork(neuralnetwork);

            //openPricesRecords.registerObserver(tradesRecords);

            loop();
			
		} catch (Exception e) {
            e.printStackTrace();
        }
	}

    protected abstract void loop();
	
	public void onChange() {

        try {

            AtsLogger.log("Locking resource ... Processing");

            dataManager.reload();
            dataManager.debugFile(SharedMemory.filename + ".DM");

            //openPricesRecords.addPrice(dataManager.getLastVector().get(Constants.OPEN_IDENTIFIER));
			neuralnetworkexecution.getNeuralNetwork().resetWeights();

            if(neuralnetworkexecution instanceof NeuralNetworkExecution) {
                neuralnetworkexecution.exec();
            }

		} catch (Exception e) {
            e.printStackTrace();
        }
		
	}

}
