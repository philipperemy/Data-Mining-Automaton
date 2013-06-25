package all;

import fr.automated.trading.systems.exception.ErrorCalculusException;
import fr.automated.trading.systems.exception.InvalidNewLineValuesException;
import fr.automated.trading.systems.exception.MaxValueFromColumnException;
import fr.automated.trading.systems.exception.PropertiesException;
import fr.automated.trading.systems.marketdatas.MarketData;
import fr.automated.trading.systems.marketdatas.symbols.SymbolsConverter;
import fr.automated.trading.systems.marketdatas.symbols.YahooCac40Symbols;
import fr.automated.trading.systems.portfoliosmanager.PricesConstants;
import fr.automated.trading.systems.portfoliosmanager.TradeRecords;
import fr.automated.trading.systems.portfoliosmanager.TradeRecordsImpl;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.activationfunction.ActivationFunctionSigmoid;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.core.NeuralNetwork;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.core.NeuralNetworkFactory;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.error.*;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.execution.NeuralNetworkExecution;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.execution.NeuralNetworkExecutionBuildModel;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.execution.NeuralNetworkExecutionFactory;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.execution.NeuralNetworkExecutionResult;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.weights.Weights;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.weights.WeightsManager;
import fr.automated.trading.systems.profitandloss.PnL;
import fr.automated.trading.systems.tradingrobot.datamanager.DataManager;
import fr.automated.trading.systems.tradingrobot.datamanager.expectedvalues.BuildExpectedValues;
import fr.automated.trading.systems.tradingrobot.datamanager.expectedvalues.GapBetweenTwoOpens;
import fr.automated.trading.systems.tradingrobot.datamanager.inputvectors.BuildInputVectors;
import fr.automated.trading.systems.tradingrobot.datamanager.inputvectors.BuildInputVectorsImpl;
import fr.automated.trading.systems.tradingrobot.monitor.filemonitoring.FileMonitor;
import fr.automated.trading.systems.tradingrobot.monitor.filemonitoring.FileMonitorRobot;
import fr.automated.trading.systems.tradingrobot.robot.TradingRobotStub;
import fr.automated.trading.systems.tradingrobot.robot.TradingRobotType;
import fr.automated.trading.systems.utils.CSVData;
import fr.automated.trading.systems.utils.CSVDataImpl;
import fr.automated.trading.systems.utils.csvparser.CSVParser;
import fr.automated.trading.systems.utils.csvparser.CSVParserTransformer;
import fr.automated.trading.systems.utils.csvparser.CSVParserTransformerBinary;
import fr.automated.trading.systems.utils.excel.ExcelAPI;
import fr.automated.trading.systems.utils.utils.*;
import jxl.write.WriteException;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Testing {

    private double binaryRand() {
        new Random();
        return (double) ((Math.random() < 0.5) ? 0 : 1);
    }

    @Test
    public void CSVParserTest() throws MaxValueFromColumnException, InvalidNewLineValuesException {
        CSVParser csvParser = new CSVParser("files/test/test.csv");
        Assert.assertEquals(csvParser.isFilled(), true);

        Assert.assertEquals(5000, csvParser.countLines());
        Assert.assertEquals(4, csvParser.countColumns());

        ArrayList<Double> line9 = new ArrayList<>();
        line9.add(48.0); line9.add(49.50); line9.add(47.50); line9.add(49.25);
        Assert.assertArrayEquals(Utils.doubleListToArray(line9), csvParser.getLine(9), 0.1);

        Assert.assertEquals(csvParser.getFilename(), "files/test/test.csv");
        Assert.assertEquals(csvParser.getMaxValueFromColumn(1), 644.0, 0.1);
        Assert.assertEquals(csvParser.getValue(9,2), 47.50, 0.1);

        Assert.assertArrayEquals(csvParser.getValues().getLine(9), Utils.doubleListToArray(line9), 0.1);

        Assert.assertEquals(csvParser.getValues().countLines(), csvParser.countLines());

        for(int i=0; i<csvParser.countLines(); i++) {
            Assert.assertEquals(csvParser.getValues().getLine(i).length, csvParser.countColumns());
        }

        csvParser.addLine(Utils.doubleListToArray(line9));

        Assert.assertEquals(5001, csvParser.countLines());
        Assert.assertEquals(4, csvParser.countColumns());

        Assert.assertArrayEquals(csvParser.getLine(csvParser.countLines()-1), Utils.doubleListToArray(line9), 0.1);

        for(int i=0; i<csvParser.countLines(); i++) {
            Assert.assertEquals(csvParser.getValues().getLine(i).length, csvParser.countColumns());
        }

        csvParser.write("files/test/test_tmp.csv");

        csvParser = new CSVParser("files/test/test_tmp.csv");
        csvParser.removeLastColumn();
        Assert.assertEquals(csvParser.countLines(), 5001);
        Assert.assertEquals(csvParser.countColumns(), 3);
        line9.remove(line9.size()-1);
        Assert.assertArrayEquals(csvParser.getLine(csvParser.countLines()-1), Utils.doubleListToArray(line9), 0.1);

        for(int i=0; i<csvParser.countLines(); i++) {
            Assert.assertEquals(csvParser.getValues().getLine(i).length, csvParser.countColumns());
        }

        csvParser.delete();
    }

    @Test
    public void ExtendedFileTest() {

        Utils.delete("files/test/test_tmp2.csv");
        Utils.delete("files/test/test_tmp3.csv");

        Utils.copyfile("files/test/test.csv", "files/test/test_tmp2.csv");
        ExtendedFile f = new ExtendedFile("files/test/test_tmp2.csv");
        Assert.assertEquals(f.countLines(), 5000);
        f.copy("files/test/test_tmp3.csv");
        Assert.assertEquals(Utils.md5sum("files/test/test_tmp3.csv"), Utils.md5sum("files/test/test_tmp2.csv"));

        f.append("hello world");

        Assert.assertTrue(!Utils.md5sum("files/test/test_tmp2.csv").equals(Utils.md5sum("files/test/test_tmp3.csv")));

        ArrayList<String> extractedLines = f.extractLines(10, 20);
        Assert.assertEquals(extractedLines.size(), 20);
        Assert.assertEquals(extractedLines.get(0), "49.0;49.25;47.5;47.63");

        ArrayList<String> extractedLines2 = f.extractLines(f.countLines()-2, 1);
        Assert.assertEquals(extractedLines2.size(), 1);
        Assert.assertEquals(extractedLines2.get(0), "578.0;584.0;574.25;584.0");

    }

    @Test
    public void UtilsTest() throws PropertiesException {
        List<Double> list = new ArrayList<>();
        list.add(10.0); list.add(12.0); list.add(9.0); list.add(13.0);
        Assert.assertEquals(Utils.max(list), 13.0, 0.1);

        ArrayList<ArrayList<Double>> doubleList = new ArrayList<>();
        ArrayList<Double> list2 = new ArrayList<>();
        list2.add(10.0); list2.add(12.0); list2.add(900.0); list2.add(10.0);
        ArrayList<Double> list3 = new ArrayList<>();
        list3.add(100.0); list3.add(120.0); list3.add(90.0); list3.add(130.0);
        doubleList.add(list2); doubleList.add(list3);

        Assert.assertEquals(list2.size(), list3.size());
        Assert.assertEquals(Utils.maxDoubleList(doubleList), 900.0, 0.1);

        TradingRobotType type1 = Utils.convertStringToTradingRobotType("TRADING_ROBOT_FILE_MONITOR");
        TradingRobotType type2 = Utils.convertStringToTradingRobotType("TRADING_ROBOT_MARKET_DATA");
        TradingRobotType type3 = Utils.convertStringToTradingRobotType("TRADING_ROBOT_ONE_LOOP");

        Assert.assertEquals(type1.toString(), TradingRobotType.TRADING_ROBOT_FILE_MONITOR.toString());
        Assert.assertEquals(type2.toString(), TradingRobotType.TRADING_ROBOT_MARKET_DATA.toString());
        Assert.assertEquals(type3.toString(), TradingRobotType.TRADING_ROBOT_ONE_LOOP.toString());

        String separator = Constants.SEPARATOR;
        List<Double> list4 = Utils.strtokList("25.0" + separator + "23.0" + separator + "22.0" + separator + "21.0");
        List<Double> list5 = new ArrayList<>();
        list5.add(25.0); list5.add(23.0); list5.add(22.0); list5.add(21.0);

        Assert.assertArrayEquals(Utils.doubleListToArray(list4),Utils.doubleListToArray(list5), 0.1);

        double[] array1 = Utils.strtokArray("25.0" + separator + "23.0" + separator + "22.0" + separator + "21.0");

        Assert.assertArrayEquals(array1, Utils.doubleListToArray(list5), 0.1);

        Assert.assertEquals(Utils.getMethodName(), "UtilsTest");

        List<Double> reversedList5 = Utils.reverseList(list5);
        List<Double> list6 = new ArrayList<>();
        list6.add(21.0); list6.add(22.0); list6.add(23.0); list6.add(25.0);
        Assert.assertArrayEquals(Utils.doubleListToArray(reversedList5),Utils.doubleListToArray(list6), 0.1);

    }

    @Test
    public void CSVParserTransformerBinaryTest() {
        CSVParser csvParser = new CSVParser("files/test/test.csv");
        CSVParserTransformer binaryTransformation = new CSVParserTransformerBinary(csvParser);

        Assert.assertTrue(binaryTransformation.isFilled());
        Assert.assertEquals(binaryTransformation.getValues().countLines(), csvParser.countLines());

        double[] line1 = binaryTransformation.getValues().getLine(0);
        List<Double> expectedLine1 = new ArrayList<>();
        expectedLine1.add(0.0); expectedLine1.add(0.0); expectedLine1.add(0.0); expectedLine1.add(0.0);
        Assert.assertArrayEquals(line1, Utils.doubleListToArray(expectedLine1), 0.1);

        double[] line2 = binaryTransformation.getValues().getLine(csvParser.countLines()-1);
        List<Double> expectedLine2 = new ArrayList<>();
        expectedLine2.add(1.0); expectedLine2.add(1.0); expectedLine2.add(1.0); expectedLine2.add(1.0);
        binaryTransformation.write("files/test/test-binary.csv");
        Assert.assertArrayEquals(line2, Utils.doubleListToArray(expectedLine2), 0.1);

        binaryTransformation.transform();
        Assert.assertEquals(binaryTransformation.getValues().countLines(), csvParser.countLines());

        Assert.assertEquals(binaryTransformation.getCSVParserTransformed().countLines(), csvParser.countLines());
        Assert.assertEquals(binaryTransformation.getCSVParserTransformed().countColumns(), csvParser.countColumns());
        Assert.assertEquals(binaryTransformation.getCSVParserTransformed().getFilename(), csvParser.getFilename());
        Assert.assertTrue(binaryTransformation.getCSVParserTransformed().isFilled());

        double[] lastLine = binaryTransformation.getCSVParserTransformed().getLine(csvParser.countLines()-1);
        double[] firstLine = binaryTransformation.getCSVParserTransformed().getLine(0);
        Assert.assertArrayEquals(lastLine, Utils.doubleListToArray(expectedLine2), 0.1);
        Assert.assertArrayEquals(firstLine, Utils.doubleListToArray(expectedLine1), 0.1);

        double[] line500 = csvParser.getLine(500);
        double[] line501 = csvParser.getLine(501);
        List<Double> expectedBinary500 = new ArrayList<>();

        for(int i=0; i<line500.length; i++) {
            if(line501[i]>line500[i]) {
                expectedBinary500.add(1.0);
            } else {
                expectedBinary500.add(0.0);
            }
        }

        //There is a shift of +1 because the first line is 0;0;0;0
        Assert.assertArrayEquals(binaryTransformation.getValues().getLine(501), Utils.doubleListToArray(expectedBinary500), 0.1);
    }

    @Test
    public void FileMonitorTest() throws InvalidNewLineValuesException {
        File f = new File("files/test/test_monitor.csv");
        TradingRobotStub tradingRobot = new TradingRobotStub();
        FileMonitor fileMonitor = new FileMonitorRobot(f, new TradingRobotStub());
        fileMonitor.run();
        Assert.assertEquals(tradingRobot.onChangeCount, 1);

        CSVParser csvParser = new CSVParser("files/test/test_monitor.csv");
        double[] newLine = new double[4];
        newLine[0] = 0.0;
        newLine[0] = 0.0;
        newLine[0] = 0.0;
        newLine[0] = 0.0;
        csvParser.addLine(newLine);
        csvParser.write("files/test/test_monitor.csv");

        fileMonitor.run();
        Assert.assertEquals(tradingRobot.onChangeCount, 2);

        fileMonitor.run();
        Assert.assertEquals(tradingRobot.onChangeCount, 2);

    }

    @Test
    public void ExpectedValuesTest() {
        CSVParser csvParser = new CSVParser("files/test/test_input_vectors.csv");
        BuildExpectedValues buildExpectedValues = new GapBetweenTwoOpens();
        List<Double> expectedValues = buildExpectedValues.build(csvParser.getValues());

        List<Double> closePrices = new ArrayList<>();
        for(int i=0; i<csvParser.countLines(); i++)
            closePrices.add(csvParser.getValue(i, Constants.OPEN_IDENTIFIER));

        for(int i=0; i<closePrices.size()-1; i++) {
            if(closePrices.get(i) < closePrices.get(i+1))
                Assert.assertEquals(expectedValues.get(i),1.0,0.1);
            else
                Assert.assertEquals(expectedValues.get(i),0.0,0.1);
        }

    }

    @Test
    public void InputVectorsTest() {
        CSVParser csvParser = new CSVParser("files/test/test_input_vectors.csv");
        BuildInputVectors buildInputVectors = new BuildInputVectorsImpl();
        List<List<Double>> inputVectors = buildInputVectors.build(csvParser.getValues(), 16);

        double[] lastLineBinary = csvParser.getLine(3);
        double[] lastLineBinary2 = csvParser.getLine(2);
        double[] lastLineBinary3 = csvParser.getLine(1);
        double[] lastLineBinary4 = csvParser.getLine(0);
        List<Double> lastLineWithInputNeurons = new ArrayList<>();

        //OPEN
        lastLineWithInputNeurons.add(lastLineBinary4[Constants.OPEN_IDENTIFIER]);
        lastLineWithInputNeurons.add(lastLineBinary3[Constants.OPEN_IDENTIFIER]);
        lastLineWithInputNeurons.add(lastLineBinary2[Constants.OPEN_IDENTIFIER]);
        lastLineWithInputNeurons.add(lastLineBinary[Constants.OPEN_IDENTIFIER]);

        //HIGH
        lastLineWithInputNeurons.add(lastLineBinary4[Constants.HIGH_IDENTIFIER]);
        lastLineWithInputNeurons.add(lastLineBinary3[Constants.HIGH_IDENTIFIER]);
        lastLineWithInputNeurons.add(lastLineBinary2[Constants.HIGH_IDENTIFIER]);
        lastLineWithInputNeurons.add(lastLineBinary[Constants.HIGH_IDENTIFIER]);

        //LOW
        lastLineWithInputNeurons.add(lastLineBinary4[Constants.LOW_IDENTIFIER]);
        lastLineWithInputNeurons.add(lastLineBinary3[Constants.LOW_IDENTIFIER]);
        lastLineWithInputNeurons.add(lastLineBinary2[Constants.LOW_IDENTIFIER]);
        lastLineWithInputNeurons.add(lastLineBinary[Constants.LOW_IDENTIFIER]);

        //CLOSE
        lastLineWithInputNeurons.add(lastLineBinary4[Constants.CLOSE_IDENTIFIER]);
        lastLineWithInputNeurons.add(lastLineBinary3[Constants.CLOSE_IDENTIFIER]);
        lastLineWithInputNeurons.add(lastLineBinary2[Constants.CLOSE_IDENTIFIER]);
        lastLineWithInputNeurons.add(lastLineBinary[Constants.CLOSE_IDENTIFIER]);

        Assert.assertArrayEquals(Utils.doubleListToArray(lastLineWithInputNeurons), Utils.doubleListToArray(inputVectors.get(0)) ,0.1);
    }

    @Test
    public void WeightsTest() {
        WeightsManager weightsManager = new WeightsManager(Constants.MAX_WEIGHTS, Constants.MAX_WEIGHTS);
        weightsManager.resetWeights();

        Weights weights = new Weights(Constants.MAX_WEIGHTS, Constants.MAX_WEIGHTS);
        weightsManager.addWeights(weights);

        for(int i=0; i<Constants.MAX_WEIGHTS; i++) {
            for(int j=0; j<Constants.MAX_WEIGHTS; j++) {
                Assert.assertTrue(weightsManager.getCurrentWeights().getWeight(i,j) <= 0.5);
                Assert.assertTrue(weightsManager.getCurrentWeights().getWeight(i,j) >= -0.5);
            }
        }

        for(int i=0; i<Constants.MAX_WEIGHTS; i++) {
            for(int j=0; j<Constants.MAX_WEIGHTS; j++) {
                Assert.assertTrue(weightsManager.getLastWeights().getWeight(i,j) <= 0.5);
                Assert.assertTrue(weightsManager.getLastWeights().getWeight(i,j) >= -0.5);
            }
        }

        for(int i=0; i<Constants.MAX_WEIGHTS; i++) {
            for(int j=0; j<Constants.MAX_WEIGHTS; j++) {
                Assert.assertTrue(weightsManager.getPreviousLastWeights().getWeight(i,j) <= 0.5);
                Assert.assertTrue(weightsManager.getPreviousLastWeights().getWeight(i,j) >= -0.5);
            }
        }

        Weights beforeWeights = weightsManager.getCurrentWeights().clone();
        weightsManager.saveWeights();

        for(int i=0; i<Constants.MAX_WEIGHTS; i++) {
            for(int j=0; j<Constants.MAX_WEIGHTS; j++) {
                weightsManager.setWeights(i,j, 99.0);
            }
        }

        weightsManager.loadWeights();
        for(int i=0; i<Constants.MAX_WEIGHTS; i++) {
            for(int j=0; j<Constants.MAX_WEIGHTS; j++) {
                Assert.assertEquals(beforeWeights.getWeight(i,j), weightsManager.getCurrentWeights().getWeight(i,j), 0.1);
            }
        }

        beforeWeights = weightsManager.getCurrentWeights().clone();

        for(int i=0; i<Constants.MAX_WEIGHTS; i++) {
            for(int j=0; j<Constants.MAX_WEIGHTS; j++) {
                weightsManager.setWeights(i,j, 99.0);
            }
        }

        for(int i=0; i<Constants.MAX_WEIGHTS; i++) {
            for(int j=0; j<Constants.MAX_WEIGHTS; j++) {
                //must be different
                Assert.assertTrue(Math.abs(beforeWeights.getWeight(i,j) - weightsManager.getCurrentWeights().getWeight(i,j)) > 0.01);
            }
        }

    }

    @Test
    public void CircularTest2() {
        CircularList<Double> circularList = new CircularList<>();
        for(int i=0; i<5000; i++) {
            circularList.add((double) i);
        }
    }

    @Test
    public void CircularListTest() {
        CircularList<Double> circularList = new CircularList<>();

        circularList.LIMIT_MAX = 10;

        for(int i=0; i<100; i++) {
            circularList.add((double) i);
        }

        double someNumber = circularList.get(circularList.LIMIT_MAX-5);
        double previous = circularList.getPrevious(circularList.LIMIT_MAX-5);

        Assert.assertEquals(someNumber, previous+1, 0.1);

        double last = circularList.getLast();
        double lastPrevious = circularList.getLastPrevious();

        Assert.assertEquals(last, lastPrevious+1, 0.1);
        Assert.assertEquals(last, 99, 0.1);

        circularList.clear();

        circularList.LIMIT_MAX = 10;

        circularList.add((double) 0);
        double first = circularList.getLast();
        double firstPrevious = circularList.getLastPrevious();
        Assert.assertEquals(first, 0, 0.1);
        Assert.assertEquals(firstPrevious, 0, 0.1);

        for(int i=1; i<100; i++) {
            circularList.add((double) i);
        }

        someNumber = circularList.get(circularList.LIMIT_MAX-5);
        previous = circularList.getPrevious(circularList.LIMIT_MAX-5);

        Assert.assertEquals(someNumber, previous+1, 0.1);

        last = circularList.getLast();
        lastPrevious = circularList.getLastPrevious();

        Assert.assertEquals(last, lastPrevious+1, 0.1);
        Assert.assertEquals(last, 99, 0.1);
    }

    @Test
    public void ErrorValuesTransformersTest() {
        ErrorValues begin = new ErrorValues();
        List<Double> computedValues = new ArrayList<>();
        List<Double> expectedValues = new ArrayList<>();
        begin.setComputedValues(computedValues);
        begin.setExpectedValues(expectedValues);

        int even = 0;
        for(int i=0; i<1000; i++) {
            if(even%2 == 0)
                computedValues.add(0.3);
            else
                computedValues.add(0.7);
            even++;
            expectedValues.add(1.0);
        }

        ErrorValues result = ErrorValuesTransformers.transform(begin);
        Assert.assertArrayEquals(Utils.doubleListToArray(result.getExpectedValues()), Utils.doubleListToArray(begin.getExpectedValues()), 0.1);

        even = 0;
        for(int i=0; i<1000; i++) {
            if(even%2 == 0)
                Assert.assertEquals(result.getComputedValues().get(i), 0.0, 0.1);
            else
                Assert.assertEquals(result.getComputedValues().get(i), 1.0, 0.1);
            even++;
        }
    }

    @Test
    public void ErrorSimpleCalculusTest() throws ErrorCalculusException {
        ErrorSimpleCalculus errorSimpleCalculus = new ErrorSimpleCalculus();

        List<Double> computedValues = new ArrayList<>();
        List<Double> expectedValues = new ArrayList<>();

        int even = 0;
        for(int i=0; i<1000; i++) {
            if(even%2 == 0)
                computedValues.add(1.0);
            else
                computedValues.add(0.0);
            even++;
            expectedValues.add(0.0);
        }

        double error = errorSimpleCalculus.calculate(computedValues, expectedValues);
        Assert.assertEquals(error, 0.5, 0.1);

        computedValues.clear();
        expectedValues.clear();

        for(int i=0; i<1000; i++) {
            computedValues.add(1.0);
            expectedValues.add(0.0);
        }

        error = errorSimpleCalculus.calculate(computedValues, expectedValues);
        Assert.assertEquals(error, 1.0, 0.1);
    }

    @Test
    public void ErrorSetReductionTest() {
        CSVParser csvParser = new CSVParser("files/test/test-set-reduction.csv");
        CSVParserTransformerBinary csvParserTransformerBinary = new CSVParserTransformerBinary(csvParser);
        DataManager dataManager = new DataManager(csvParser.getValues(), csvParserTransformerBinary.getValues());

        List<Double> computedValues = new ArrayList<>();
        List<Double> expectedValues = new ArrayList<>();

        SharedMemory.lastDelimiter = csvParser.countLines()-1000;

        for(int i=0; i<1000; i++) {
            computedValues.add(binaryRand());
            expectedValues.add(binaryRand());
        }

        ErrorValues begin = new ErrorValues(computedValues, expectedValues);
        ErrorValues result = ErrorSetReduction.transform(begin, dataManager, SharedMemory.lastDelimiter);

        int count = 0;
        for(int i=0; i<1000; i++) {
            if(expectedValues.get(i) == 1.0) {
                if(dataManager.getCsvData().getValue(i+SharedMemory.lastDelimiter, Constants.CLOSE_IDENTIFIER) < dataManager.getCsvData().getValue(i+SharedMemory.lastDelimiter, Constants.OPEN_IDENTIFIER)) {
                    count++;
                }
            } else if(expectedValues.get(i) == 0.0) {
                if(dataManager.getCsvData().getValue(i+SharedMemory.lastDelimiter, Constants.CLOSE_IDENTIFIER) > dataManager.getCsvData().getValue(i+SharedMemory.lastDelimiter, Constants.OPEN_IDENTIFIER)) {
                    count++;
                }
            }
        }

        Assert.assertEquals(result.size(), count, 1);

        count = 0;
        for(int i=0; i<result.size(); i++) {
            if(expectedValues.get(i) == 1.0) {
                if(dataManager.getCsvData().getValue(i+SharedMemory.lastDelimiter, Constants.CLOSE_IDENTIFIER) < dataManager.getCsvData().getValue(i+SharedMemory.lastDelimiter, Constants.OPEN_IDENTIFIER)) {
                    Assert.assertEquals(result.getExpectedValues().get(count), expectedValues.get(i));
                    Assert.assertEquals(result.getComputedValues().get(count), computedValues.get(i));
                    count++;
                }
            } else if(expectedValues.get(i) == 0.0) {
                if(dataManager.getCsvData().getValue(i+SharedMemory.lastDelimiter, Constants.CLOSE_IDENTIFIER) > dataManager.getCsvData().getValue(i+SharedMemory.lastDelimiter, Constants.OPEN_IDENTIFIER)) {
                    Assert.assertEquals(result.getExpectedValues().get(count), expectedValues.get(i));
                    Assert.assertEquals(result.getComputedValues().get(count), computedValues.get(i));
                    count++;
                }
            }
        }
    }

    @Test
    public void ExtractModelErrorTest() {
        List<Double> computedValues = new ArrayList<>();
        List<Double> expectedValues = new ArrayList<>();
        ErrorValues errorValues = new ErrorValues(computedValues, expectedValues);

        int even = 0;
        for(int i=0; i<1000; i++) {
            if(even%2 == 0)
                computedValues.add(1.0);
            else
                computedValues.add(0.0);
            even++;
            expectedValues.add(1.0);
        }


        Map<String, Integer> modelError =  ErrorInfoExtractor.extractModelError(errorValues);
        Assert.assertEquals(modelError.get("OK"), 500, 0.1);
        Assert.assertEquals(modelError.get("KO"), 500, 0.1);

        computedValues.clear();
        expectedValues.clear();

        for(int i=0; i<1000; i++) {
            computedValues.add(1.0);
            expectedValues.add(1.0);
        }

        Map<String, Integer> modelError2 = ErrorInfoExtractor.extractModelError(errorValues);
        Assert.assertEquals(modelError2.get("OK"), 1000, 0.1);
        Assert.assertEquals(modelError2.get("KO"), 0, 0.1);

    }

    @Test(expected = RuntimeException.class)
    public void TradeTest() {
        String ISIN = "FR001";
        TradeRecords tradeRecords = new TradeRecordsImpl();
        tradeRecords.saveTrade(ISIN, 12.00, 13.00, 100, PricesConstants.BUY);
        tradeRecords.saveTrade(ISIN, 12.00, 13.00, 100, PricesConstants.BUY);
    }

    @Test
    public void TradeTest2() {
        PnL pnl = new PnL();
        String ISIN = "FR001";
        TradeRecords tradeRecords = new TradeRecordsImpl();
        tradeRecords.saveTrade(ISIN, 12.00, 13.00, 100, PricesConstants.BUY);
        Assert.assertEquals(pnl.calculatePnL(tradeRecords.getTrades()), 0.0, 0.1);
        tradeRecords.update(14.00);
        Assert.assertEquals(pnl.calculatePnL(tradeRecords.getTrades()), 200.0, 0.1);
        tradeRecords.saveTrade(ISIN, 10.00, 8.00, 100, PricesConstants.SELL);
        tradeRecords.update(11.00);
        Assert.assertEquals(pnl.calculatePnL(tradeRecords.getTrades()), 100.0, 0.1);
    }

    @Test
    public void PnLTest() {
        PnL pnl = new PnL();
        String ISIN = "FR001";
        TradeRecords tradeRecords = new TradeRecordsImpl();
        tradeRecords.saveTrade(ISIN, 12.00, 13.00, 100, PricesConstants.BUY);
        Assert.assertEquals(pnl.calculatePnL(tradeRecords.getTrades()), 0.0, 0.1);
        Assert.assertEquals(pnl.getPnLHistory().size(), 1);
        tradeRecords.update(14.00);
        Assert.assertEquals(pnl.calculatePnL(tradeRecords.getTrades()), 200.0, 0.1);
        Assert.assertEquals(pnl.getPnLHistory().size(), 2);
        tradeRecords.saveTrade(ISIN, 10.00, 8.00, 100, PricesConstants.SELL);
        tradeRecords.update(11.00);
        Assert.assertEquals(pnl.calculatePnL(tradeRecords.getTrades()), 100.0, 0.1);
        Assert.assertEquals(pnl.getPnLHistory().size(), 3);
    }

    @Test
    public void NeuralNetworkExecutionTest2() throws Exception {
        SharedMemory.debug();
        SharedMemory.filename = "files/test/test_neural_network_execution.csv";
        NeuralNetwork neuralnetwork = NeuralNetworkFactory.createInstance()
                .createInputLayer(SharedMemory.inputCount)
                .createHiddenLayer(SharedMemory.hiddenCount)
                .createOutputLayer(Constants.OUTPUT_COUNT)
                .addActivationFunction(new ActivationFunctionSigmoid());

        CSVParser csvParser = new CSVParser(SharedMemory.filename);
        CSVParserTransformer csvParserTransformer = new CSVParserTransformerBinary(csvParser);
        DataManager dataManager = new DataManager(csvParser.getValues(), csvParserTransformer.getValues());

        NeuralNetworkExecution execution = NeuralNetworkExecutionFactory.createInstance()
                .setDataManager(dataManager)
                .setNeuralNetwork(neuralnetwork);

        List<NeuralNetworkExecutionResult> results = new ArrayList<>();
        List<Long> latencies = new ArrayList<>();
        double probabilityOkOnAverage = 0.0;

        int iterations = 10;

        for(int i=0; i<iterations; i++) {
            Long start = System.currentTimeMillis();
            execution.getNeuralNetwork().resetWeights();
            NeuralNetworkExecutionResult result = (NeuralNetworkExecutionResult) execution.exec();
            Assert.assertTrue(result.getProbabilityOk() > 0.75);
            Assert.assertTrue(result.isTradable());
            Assert.assertTrue(result.getExpectedDirection() < 0.5);
            Assert.assertTrue(result.getExpectedVariation() > 4.0);
            AtsLogger.log("probability["+i+"] = " + result.getProbabilityOk());
            probabilityOkOnAverage += result.getProbabilityOk();
            latencies.add(System.currentTimeMillis()-start);
            results.add(result);
        }

        for(int i=0; i<iterations; i++) {
            AtsLogger.log("probability[" + i + "] = " + results.get(i).getProbabilityOk());
        }

        for(int i=0; i<iterations; i++) {
            AtsLogger.log("latency[" + i + "] = " + latencies.get(i));
            Assert.assertTrue(latencies.get(i) < 55000);
        }

        probabilityOkOnAverage /= iterations;
        AtsLogger.log("probability on average = " + probabilityOkOnAverage);
        double minBound = probabilityOkOnAverage-0.02;
        double maxBound = probabilityOkOnAverage+0.02;
        AtsLogger.log("min bound = " + minBound);
        AtsLogger.log("max bound = " + maxBound);

        for(int i=0; i<iterations; i++) {
            double currentProbabilityOk = results.get(i).getProbabilityOk();
            Assert.assertTrue((currentProbabilityOk > minBound) && (currentProbabilityOk < maxBound));
        }


    }

    @Test
    public void NeuralNetworkBuildModelTest() throws Exception {
        SharedMemory.debug();
        SharedMemory.filename = "files/test/test_neural_network_execution.csv";

        NeuralNetwork neuralnetwork = NeuralNetworkFactory.createInstance()
                .createInputLayer(SharedMemory.inputCount)
                .createHiddenLayer(SharedMemory.hiddenCount)
                .createOutputLayer(Constants.OUTPUT_COUNT)
                .addActivationFunction(new ActivationFunctionSigmoid());

        CSVParser csvParser = new CSVParser(SharedMemory.filename);
        CSVParserTransformer csvParserTransformer = new CSVParserTransformerBinary(csvParser);
        DataManager dataManager = new DataManager(csvParser.getValues(), csvParserTransformer.getValues());

        SharedMemory.uiMode = UIMode.CLI_MODE;
        NeuralNetworkExecution execution = NeuralNetworkExecutionFactory.createInstance()
                .setDataManager(dataManager)
                .setNeuralNetwork(neuralnetwork);

        NeuralNetworkExecutionResult result = (NeuralNetworkExecutionResult) execution.exec();

        NeuralNetworkExecutionBuildModel neuralNetworkExecutionBuildModel = new NeuralNetworkExecutionBuildModel();
        neuralNetworkExecutionBuildModel.setDataManager(dataManager);
        neuralNetworkExecutionBuildModel.setNeuralNetwork(neuralnetwork);
        neuralNetworkExecutionBuildModel.buildModel();
    }

    @Test
    public void NeuralNetworkExecutionTest() throws Exception {
        SharedMemory.debug();
        SharedMemory.filename = "files/test/test_neural_network_execution.csv";
        NeuralNetwork neuralnetwork = NeuralNetworkFactory.createInstance()
                .createInputLayer(SharedMemory.inputCount)
                .createHiddenLayer(SharedMemory.hiddenCount)
                .createOutputLayer(Constants.OUTPUT_COUNT)
                .addActivationFunction(new ActivationFunctionSigmoid());

        CSVParser csvParser = new CSVParser(SharedMemory.filename);
        CSVParserTransformer csvParserTransformer = new CSVParserTransformerBinary(csvParser);
        DataManager dataManager = new DataManager(csvParser.getValues(), csvParserTransformer.getValues());

        SharedMemory.uiMode = UIMode.CLI_MODE;
        NeuralNetworkExecution execution = NeuralNetworkExecutionFactory.createInstance()
                .setDataManager(dataManager)
                .setNeuralNetwork(neuralnetwork);

        NeuralNetworkExecutionResult result = (NeuralNetworkExecutionResult) execution.exec();
        Assert.assertTrue(result.getProbabilityOk() > 0.75);
        Assert.assertTrue(result.isTradable());
        Assert.assertTrue(result.getExpectedDirection() < 0.5);
        Assert.assertTrue(result.getExpectedVariation() > 4.0);
    }

    @Test
    public void testMarketData() {
        long start = System.currentTimeMillis();
        MarketData marketData = new MarketData();
        String filename1 = "files/test/market-data-calls/UG-filename1-" + start + ".csv";
        marketData.getMarketData("UG.PA", filename1);
        long end = System.currentTimeMillis()-start;
        AtsLogger.log("HTTP request performed in " + end + " ms");
        Assert.assertTrue(end < 12000);

        long start2 = System.currentTimeMillis();
        String filename2 = "files/test/market-data-calls/UG-filename2-" + start2 + ".csv";
        marketData.getOnlyYahooMarketData("UG.PA", filename2);

        CSVParser csvParser1 = new CSVParser(filename1);
        CSVParser csvParser2 = new CSVParser(filename2);

        Assert.assertEquals(csvParser1.countLines(), csvParser2.countLines()+1);
    }

    @Test
    public void testMarketDataYahooCall() {
        long start = System.currentTimeMillis();
        MarketData marketData = new MarketData();
        marketData.getOnlyYahooMarketData("UG.PA", "files/test/market-data-calls/" + start + ".csv");
        long end = System.currentTimeMillis()-start;
        AtsLogger.log("HTTP request performed in " + end + " ms");
        Assert.assertTrue(end < 7500);
    }

    @Test
    public void testMarketDataAbcBourseCall() {
        long start = System.currentTimeMillis();
        MarketData marketData = new MarketData();
        List<Double> prices = marketData.getLastPricesFromAbcBourse("EI");
        for(double price : prices) {
            Assert.assertTrue(price != 0.0);
            AtsLogger.log("price : " + price);
        }

        long end = System.currentTimeMillis()-start;
        AtsLogger.log("HTTP request performed in " + end + " ms");
        Assert.assertTrue(end < 2500);
    }

    @Test
    public void testSymbolsConverter() {
        Assert.assertEquals(SymbolsConverter.AbcBourseToYahooSymbol("UG"), "UG.PA");
        Assert.assertEquals(SymbolsConverter.AbcBourseToYahooSymbol("GLE"), "GLE.PA");

        Assert.assertEquals(SymbolsConverter.YahooToAbcBourseSymbol("RNO.PA"), "RNO");
        Assert.assertEquals(SymbolsConverter.YahooToAbcBourseSymbol("BNP.PA"), "BNP");

        Assert.assertEquals(SymbolsConverter.AbcBourseToYahooSymbol(SymbolsConverter.YahooToAbcBourseSymbol("BNP.PA")), "BNP.PA");
        Assert.assertEquals(SymbolsConverter.AbcBourseToYahooSymbol(SymbolsConverter.YahooToAbcBourseSymbol("VK.PA")), "VK.PA");

        Assert.assertEquals(SymbolsConverter.YahooToAbcBourseSymbol(SymbolsConverter.AbcBourseToYahooSymbol("BNP")), "BNP");
        Assert.assertEquals(SymbolsConverter.YahooToAbcBourseSymbol(SymbolsConverter.AbcBourseToYahooSymbol("VK")), "VK");
    }

    @Test
    public void testCompareDates() {
        String date1 = "2012-04-04";
        String date2 = "2011-09-09";
        Assert.assertEquals(Utils.compareDates(date1, date2), 1);

        date1 = "2012-04-04";
        date2 = "2012-08-01";
        Assert.assertEquals(Utils.compareDates(date1, date2), 2);

        date1 = "2012-05-05";
        date2 = "2012-05-31";
        Assert.assertEquals(Utils.compareDates(date1, date2), 2);
    }

    @Test
    public void testExcel() throws WriteException, IOException {
        ExcelAPI excelAPI = new ExcelAPI("files/excel/output.xls");
        excelAPI.write(0,0, "hello");
        excelAPI.write(0,2, "hello");
        excelAPI.write(1, 1, "hello");
        excelAPI.colorBad(0, 0);
        excelAPI.colorGood(0, 2);
        Assert.assertEquals(excelAPI.read(0, 0), "hello");
        Assert.assertEquals(excelAPI.read(0, 2), "hello");
        Assert.assertEquals(excelAPI.read(1, 1), "hello");
        excelAPI.close();

    }

    @Test
    public void testGetCompany() {
        YahooCac40Symbols yahooCac40Symbols = new YahooCac40Symbols();
        Assert.assertEquals(yahooCac40Symbols.getCompany("ACA.PA"), "Credit Agricole");
        Assert.assertEquals(yahooCac40Symbols.getCompany("RI.PA"), "Pernod-Ricard");
    }

    @Test
    public void testCsvData() {
        CSVData csvData = new CSVDataImpl();
        int counter = 0;
        for(int x = 0; x < 100000; x++) {
            for(int y = 0; y < 500; y++) {
                csvData.setValue(x, y, counter++);
            }
        }

        //csvData.debug();

        int counter2 = 0;
        for(int x = 0; x < 100000; x++) {
            for(int y = 0; y < 500; y++) {
                Assert.assertEquals(counter2++, csvData.getValue(x,y), 0.001);
            }
        }

    }


    @Test
    public void testSpeedCsvData2() throws InvalidNewLineValuesException {
        long t1 = System.currentTimeMillis();
        CSVData csvData = new CSVDataImpl(500001, 8);
        double counter = 0;
        for(int x = 0; x < 500000; x++) {
            for(int y = 0; y < 7; y++) {
                csvData.setValue(x, y, counter++);
            }
        }
        long t2 = System.currentTimeMillis();
        System.out.println("load : " + (t2-t1));

        double readCounter = 0;
        for(int x = 0; x < 500000; x++) {
            for(int y = 0; y < 7;  y++) {
                readCounter = csvData.getValue(x,y);
            }
        }
        System.out.println("read : " + (System.currentTimeMillis()-t2));
        System.out.println("readCounter : " + readCounter);

    }

    @Test
    public void testDataManager2() throws IOException {

        CSVParser csvParser = new CSVParser("files/test/test_data_manager.csv");
        CSVParserTransformerBinary csvParserTransformerBinary = new CSVParserTransformerBinary(csvParser);

        DataManager dataManager = new DataManager(csvParser.getValues(), csvParserTransformerBinary.getValues());

        double[] lastLine = csvParser.getLine(csvParser.countLines()-1);
        Assert.assertArrayEquals(dataManager.getLastCsvLine(), lastLine, 0.1);

        Assert.assertTrue(dataManager.getCsvData() != null);
        Assert.assertTrue(dataManager.getCsvDataTransformed() != null);

        double[] lastLineBinary = dataManager.getCsvDataTransformed().getLine(csvParser.countLines()-1);
        double[] lastLineBinary2 = dataManager.getCsvDataTransformed().getLine(csvParser.countLines()-2);
        double[] lastLineBinary3 = dataManager.getCsvDataTransformed().getLine(csvParser.countLines()-3);
        double[] lastLineBinary4 = dataManager.getCsvDataTransformed().getLine(csvParser.countLines()-4);
        List<Double> lastLineWithInputNeurons = new ArrayList<>();

        //OPEN
        lastLineWithInputNeurons.add(lastLineBinary4[Constants.OPEN_IDENTIFIER]);
        lastLineWithInputNeurons.add(lastLineBinary3[Constants.OPEN_IDENTIFIER]);
        lastLineWithInputNeurons.add(lastLineBinary2[Constants.OPEN_IDENTIFIER]);
        lastLineWithInputNeurons.add(lastLineBinary[Constants.OPEN_IDENTIFIER]);

        //HIGH
        lastLineWithInputNeurons.add(lastLineBinary4[Constants.HIGH_IDENTIFIER]);
        lastLineWithInputNeurons.add(lastLineBinary3[Constants.HIGH_IDENTIFIER]);
        lastLineWithInputNeurons.add(lastLineBinary2[Constants.HIGH_IDENTIFIER]);
        lastLineWithInputNeurons.add(lastLineBinary[Constants.HIGH_IDENTIFIER]);

        //LOW
        lastLineWithInputNeurons.add(lastLineBinary4[Constants.LOW_IDENTIFIER]);
        lastLineWithInputNeurons.add(lastLineBinary3[Constants.LOW_IDENTIFIER]);
        lastLineWithInputNeurons.add(lastLineBinary2[Constants.LOW_IDENTIFIER]);
        lastLineWithInputNeurons.add(lastLineBinary[Constants.LOW_IDENTIFIER]);

        //CLOSE
        lastLineWithInputNeurons.add(lastLineBinary4[Constants.CLOSE_IDENTIFIER]);
        lastLineWithInputNeurons.add(lastLineBinary3[Constants.CLOSE_IDENTIFIER]);
        lastLineWithInputNeurons.add(lastLineBinary2[Constants.CLOSE_IDENTIFIER]);
        lastLineWithInputNeurons.add(lastLineBinary[Constants.CLOSE_IDENTIFIER]);

        AtsLogger.log("data manager = ");
        Utils.displayList(dataManager.getLastVector());
        AtsLogger.log("computed manually = ");
        Utils.displayList(lastLineWithInputNeurons);
        Assert.assertArrayEquals(Utils.doubleListToArray(dataManager.getLastVector()), Utils.doubleListToArray(lastLineWithInputNeurons), 0.1);
        Assert.assertArrayEquals(dataManager.getLastCsvLine(), csvParser.getLine(csvParser.countLines()-1), 0.1);

        int begin = SharedMemory.inputCount / csvParser.countColumns();

        //Expected values
        for(int i=0; i<dataManager.getCsvData().countLines()-1; i++) {
            double currentPrice = dataManager.getCsvData().getValue(i, Constants.OPEN_IDENTIFIER);
            double nextPrice = dataManager.getCsvData().getValue(i + 1, Constants.OPEN_IDENTIFIER);
            if(currentPrice < nextPrice)
                Assert.assertEquals(dataManager.getExpectedValues().get(i), 1.0, 0.1);
            else
                Assert.assertEquals(dataManager.getExpectedValues().get(i), 0.0, 0.1);

        }

        //Expected values
        DataManager.DataManagerIterator iterator = dataManager.iterator();
        while (iterator.hasNext()) {
            DataManager.DataManagerUnit unit = iterator.next();
            double currentOpenValue = unit.getCsvDataValue(unit.getCsvSize()-1, Constants.OPEN_IDENTIFIER);
            double nextOpenValue = dataManager.getCsvData().getValue(unit.getLine() + 1, Constants.OPEN_IDENTIFIER);
            if(currentOpenValue < nextOpenValue)
                Assert.assertEquals(unit.getExpectedValue(), 1, 0.1);
            else
                Assert.assertEquals(unit.getExpectedValue(), 0, 0.1);
        }

        //Input vectors
        DataManager.DataManagerIterator iterator2 = dataManager.iterator();
        while (iterator2.hasNext()) {
            int position = 0;
            DataManager.DataManagerUnit unit = iterator2.next();
            for(int y = 0; y < unit.getCsvDataTransformed().get(0).size(); y++) {
                for(int x = 0; x < unit.getCsvDataTransformed().size(); x++) {
                    Assert.assertEquals(unit.getCsvDataTransformedValue(x,y), unit.getInputVectors().get(position), 0.1);
                    position++;
                }
            }
        }

        //Results vectors
        List<List<Double>> results = dataManager.getResults();
        DataManager.DataManagerIterator iterator3 = dataManager.iterator();
        int position = 0;
        while (iterator3.hasNext()) {
            DataManager.DataManagerUnit unit = iterator3.next();
            List<Double> inputVector = unit.getInputVectors();
            double expectedValue = unit.getExpectedValue();

            for(int i=0; i<inputVector.size(); i++) {
                Assert.assertEquals(inputVector.get(i), results.get(position).get(i), 0.1);
            }

            Assert.assertEquals(expectedValue, results.get(position).get(SharedMemory.inputCount), 0.1);
            position++;
        }


    }

}

