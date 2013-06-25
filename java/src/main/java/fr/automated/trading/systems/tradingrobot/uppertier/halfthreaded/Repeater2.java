package fr.automated.trading.systems.tradingrobot.uppertier.halfthreaded;

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

public class Repeater2 {

    public Repeater2() {}

    public static void main(String[] args) {
        Repeater2 repeater = new Repeater2();
        repeater.run();
    }

    public void run() {

        SharedMemory.uiMode = UIMode.CLI_MODE;

        Constants.DISP_REFRESH_EPOCHS = 50;
        SharedMemory.epochs = 60;

        YahooCac40Symbols yahooCac40Symbols = new YahooCac40Symbols();
        Map<String, String> mappingSymbols = yahooCac40Symbols.getMapping();
        Collection<String> symbols = mappingSymbols.values();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new Date());

        ExcelAPI excelAPI = new ExcelAPI("files/repeater/" + "result_"+ date + ".xls");
        int y = 0;
        int x = 0;
        excelAPI.write(x++, y, "Symbol");
        excelAPI.write(x++, y, "Is tradable");
        excelAPI.write(x++, y, "Expected direction");
        excelAPI.write(x++, y, "Expected variation");
        excelAPI.write(x++, y, "Best epochs");
        excelAPI.write(x++, y, "P(OK)");
        excelAPI.write(x++, y, "P(KO)");
        excelAPI.write(x++, y, "Latency");
        y++;

        getMarketData();

        AtsLogger.log("starting counter!");
        long start = System.currentTimeMillis();

        addLastPrices();
        AtsLogger.log("all last prices retrieved in " + (System.currentTimeMillis()-start) + " ms.");

        int count = 0;

        for(String symbol : symbols) {
            try {

                Long start2 = System.currentTimeMillis();

                AtsLogger.log("symbol = " + symbol);
                String filename = "files/repeater/" + symbol + "_" + date + ".csv";

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


                AtsLogger.log("("+(++count)+"/"+symbols.size()+")");
                result = (NeuralNetworkExecutionResult) execution.exec();

                x = 0;
                excelAPI.write(x++, y, yahooCac40Symbols.getCompany(symbol));
                excelAPI.write(x++, y, result.isTradable());
                excelAPI.write(x++, y, result.getExpectedDirection());
                excelAPI.write(x++, y, result.getExpectedVariation());
                excelAPI.write(x++, y,result.getBestEpoch());

                int ok = result.getModelErrors().get("OK");
                excelAPI.write(x++, y, ok);

                int ko = result.getModelErrors().get("KO");
                excelAPI.write(x++, y, ko);
                long latency = System.currentTimeMillis()-start2;
                excelAPI.write(x++, y, latency);

                y++;

            } catch (Exception e) {
                AtsLogger.logException("CANNOT COMPUTE RESULTS FOR : " + symbol, e);
            }

        }

        AtsLogger.log("time elapsed : " + (System.currentTimeMillis()-start) + " ms.");

        excelAPI.close();
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

            String filename = "files/repeater/" + symbol + "_" + date + ".csv";
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

            String filename = "files/repeater/" + symbol + "_" + date + ".csv";
            MarketData2 marketData = new MarketData2();
            marketData.addLastPricesFromAbcBourseToFile(symbol, filename);

            AtsLogger.log("adding abcbourse last prices to filename " + filename);
        }
    }
}
