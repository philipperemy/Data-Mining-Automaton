package fr.automated.trading.systems.tradingrobot.study;

import fr.automated.trading.systems.marketdatas.MarketData2;
import fr.automated.trading.systems.marketdatas.symbols.YahooCac40Symbols;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.activationfunction.ActivationFunctionSigmoid;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.core.NeuralNetwork;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.core.NeuralNetworkFactory;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.execution.NeuralNetworkExecution;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.execution.NeuralNetworkExecutionFactory;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.execution.NeuralNetworkExecutionResult;
import fr.automated.trading.systems.tradingrobot.datamanager.DataManager;
import fr.automated.trading.systems.utils.csvparser.CSVParser;
import fr.automated.trading.systems.utils.csvparser.CSVParserTransformer;
import fr.automated.trading.systems.utils.csvparser.CSVParserTransformerBinary;
import fr.automated.trading.systems.utils.excel.ExcelAPI;
import fr.automated.trading.systems.utils.utils.AtsLogger;
import fr.automated.trading.systems.utils.utils.Constants;
import fr.automated.trading.systems.utils.utils.SharedMemory;
import fr.automated.trading.systems.utils.utils.UIMode;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Study {

    public Study() {}

    public static void main(String[] args) {
        Study study = new Study();
        study.run();
    }

    public void run() {

        try {
            SharedMemory.uiMode = UIMode.CLI_MODE;

            Constants.DISP_REFRESH_EPOCHS = 50;
            SharedMemory.epochs = 10;

            YahooCac40Symbols yahooCac40Symbols = new YahooCac40Symbols();
            Map<String, String> mappingSymbols = yahooCac40Symbols.getMapping();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = dateFormat.format(new Date());

            ExcelAPI excelAPI = new ExcelAPI("files/study/" + "result_"+ date + ".xls");
            int y = 0;
            int x = 0;
            excelAPI.write(x++, y, "Epochs");
            excelAPI.write(x++, y, "Best Epoch");
            excelAPI.write(x++, y, "Error");
            excelAPI.write(x++, y, "Latency");
            y++;


            for(int i=0; i<100; i++) {

                Long start2 = System.currentTimeMillis();

                String filename = "files/study/" + "PUB.PA" + "_" + date + ".csv";

                NeuralNetwork neuralnetwork = NeuralNetworkFactory.createInstance()
                        .createInputLayer(SharedMemory.inputCount)
                        .createHiddenLayer(SharedMemory.hiddenCount)
                        .createOutputLayer(Constants.OUTPUT_COUNT)
                        .addActivationFunction(new ActivationFunctionSigmoid());

                CSVParser csvParser = new CSVParser(filename);
                CSVParserTransformer csvParserTransformer = new CSVParserTransformerBinary(csvParser);
                DataManager dataManager = new DataManager(csvParser.getValues(), csvParserTransformer.getValues());

                NeuralNetworkExecution execution = NeuralNetworkExecutionFactory.createInstance()
                        .setDataManager(dataManager)
                        .setNeuralNetwork(neuralnetwork);

                execution.getNeuralNetwork().resetWeights();
                NeuralNetworkExecutionResult result;

                result = (NeuralNetworkExecutionResult) execution.exec();

                long latency = System.currentTimeMillis()-start2;

                int ok = result.getModelErrors().get("OK");
                int ko = result.getModelErrors().get("KO");
                double probaOk = ((double) ok) / (ok+ko);

                x = 0;
                excelAPI.write(x++, y, SharedMemory.epochs);
                excelAPI.write(x++, y, result.getBestEpoch());
                excelAPI.write(x++, y, probaOk);
                excelAPI.write(x++, y, latency);
                y++;

                SharedMemory.epochs = SharedMemory.epochs + 10;

            }

            excelAPI.close();

        } catch (Exception e) {
            AtsLogger.logException("", e);
        }

    }

    public void getMarketData() {
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        Map<String, String> mappingSymbols = (new YahooCac40Symbols()).getMapping();
        Collection<String> symbols = mappingSymbols.values();

        for(String symbol : symbols) {
            executorService.submit(new MarketDataRetrieval(symbol));
        }

        executorService.shutdown();

        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            AtsLogger.logException("", e);
        }

    }

    public class MarketDataRetrieval implements Runnable {

        private String symbol;

        public MarketDataRetrieval(String symbol) {
            this.symbol = symbol;
        }

        @Override
        public void run() {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = dateFormat.format(new Date());

            String filename = "files/study/" + symbol + "_" + date + ".csv";
            MarketData2 marketData = new MarketData2();
            marketData.getMarketDataOnlyYahoo(symbol, filename);

            AtsLogger.log("logging to filename " + filename);
        }
    }

    public void addLastPrices() {

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        Map<String, String> mappingSymbols = (new YahooCac40Symbols()).getMapping();
        Collection<String> symbols = mappingSymbols.values();

        for(String symbol : symbols) {
            executorService.submit(new MarketDataAbcBourseRetrieval(symbol));
        }

        executorService.shutdown();

        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            AtsLogger.logException("", e);
        }

    }

    public class MarketDataAbcBourseRetrieval implements Runnable {

        private String symbol;

        public MarketDataAbcBourseRetrieval(String symbol) {
            this.symbol = symbol;
        }

        @Override
        public void run() {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = dateFormat.format(new Date());

            String filename = "files/study/" + symbol + "_" + date + ".csv";
            MarketData2 marketData = new MarketData2();
            marketData.addLastPricesFromAbcBourseToFile(symbol, filename);

            AtsLogger.log("adding abcbourse last prices to filename " + filename);
        }
    }
}
