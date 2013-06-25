package fr.automated.trading.systems.tradingrobot.datamanager;

import fr.automated.trading.systems.tradingrobot.datamanager.expectedvalues.BuildExpectedValues;
import fr.automated.trading.systems.tradingrobot.datamanager.expectedvalues.GapBetweenTwoOpens;
import fr.automated.trading.systems.tradingrobot.datamanager.inputvectors.BuildInputVectors;
import fr.automated.trading.systems.tradingrobot.datamanager.inputvectors.BuildInputVectorsImpl;
import fr.automated.trading.systems.utils.CSVData;
import fr.automated.trading.systems.utils.utils.AtsLogger;
import fr.automated.trading.systems.utils.utils.Constants;
import fr.automated.trading.systems.utils.utils.SharedMemory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataManager {

    private CSVData csvData;
    private CSVData csvDataTransformed;
    private ComputationalDataStruct dataStruct;

    public DataManager(CSVData csvData, CSVData csvDataTransformed) {
        setCsvData(csvData);
        setCsvDataTransformed(csvDataTransformed);
        dataStruct = new ComputationalDataStruct();
        dataStruct.reloadValues();
    }

    public CSVData getCsvData() {
        return csvData;
    }

    private void setCsvData(CSVData csvData) {
        this.csvData = csvData;
    }

    public CSVData getCsvDataTransformed() {
        return csvDataTransformed;
    }

    private void setCsvDataTransformed(CSVData csvDataTransformed) {
        this.csvDataTransformed = csvDataTransformed;
    }

    public double[] getLastCsvLine() {
        return getCsvData().getLine(getCsvData().countLines()-1);
    }

    public List<List<Double>> getResults() {
        return dataStruct.fullVectors;
    }

    public void reload() {
        dataStruct.reloadValues();
    }

    public List<List<Double>> getInputVectors() {
        return dataStruct.inputVectors;
    }

    public List<Double> getExpectedValues() {
        return dataStruct.expectedVectors;
    }

    public List<Double> getLastVector() {
        List<Double> lastVector = new ArrayList<>(dataStruct.fullVectors.get(dataStruct.fullVectors.size()-1));
        lastVector.remove(lastVector.size()-1);
        return lastVector;
    }

    private class ComputationalDataStruct {

        private final BuildExpectedValues buildExpectedValues_function = new GapBetweenTwoOpens();
        private final BuildInputVectors buildInputVectors_function = new BuildInputVectorsImpl();

        private final Double UNDEFINED = Double.MAX_VALUE;

        private List<List<Double>> inputVectors;
        private List<Double> expectedVectors;
        private List<List<Double>> fullVectors;

        public void reloadValues() {

            int inputNeuronsCount = SharedMemory.inputCount;
            inputVectors = buildInputVectors_function.build(csvDataTransformed, inputNeuronsCount);
            expectedVectors = buildExpectedValues_function.build(csvData);

            fullVectors = new ArrayList<>();
            fullVectors.addAll(inputVectors);

            //A checker
            fullVectors.get(inputVectors.size()-1).add(UNDEFINED);
            for(int i=1; i<inputVectors.size(); i++) {
                fullVectors.get(inputVectors.size()-i-1).add(expectedVectors.get(expectedVectors.size()-i));
            }
        }
    }

    public void debug() {
        AtsLogger.log("CSVPARSER");
        getCsvData().debug();
        AtsLogger.log("CSV PARSER TRANSFORMER");
        getCsvDataTransformed().debug();
    }

    public void debugFile(String filename) throws IOException {

        String DELIMITER = ",";
        DataManagerIterator dmIterator = iterator();

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filename));
        PrintWriter writer = new PrintWriter(bufferedWriter);

        while (dmIterator.hasNext()) {
            DataManagerUnit dmUnit = dmIterator.next();
            writer.print(dmUnit.getLine());
            writer.print(DELIMITER);
            List<Double> csvData = dmUnit.getCsvData().get(dmUnit.getCsvData().size()-1);
            for(double csvElement : csvData) {
                writer.print(csvElement);
                writer.print(DELIMITER);
            }
            List<Double> csvTransformedData = dmUnit.getCsvDataTransformed().get(dmUnit.getCsvDataTransformed().size()-1);
            for(double csvElement : csvTransformedData) {
                writer.print(csvElement);
                writer.print(DELIMITER);
            }
            writer.print(dmUnit.getExpectedValue());
            writer.println();
            writer.flush();
        }

        writer.close();
        bufferedWriter.close();
    }

    public DataManagerIterator iterator() {
        return new DataManagerIterator();
    }

    public class DataManagerIterator implements Iterator
    {
        private final int begin = SharedMemory.inputCount / getCsvData().countColumns();
        private int position = begin-1;

        public void setPosition(int position) {
            this.position = position;
        }

        public int getPosition() {
            return position;
        }

        @Override
        public boolean hasNext() {
            return (position < getCsvData().countLines()-1);
        }

        @Override
        public DataManagerUnit next() {
            if (this.hasNext()) {

                DataManagerUnit dataManagerCollector = new DataManagerUnit();

                dataManagerCollector.setLine(position);

                List<List<Double>> csvData = new ArrayList<>();
                for(int i=(position-begin)+1; i <= position ; i++) {
                    List<Double> csvDataEntity = new ArrayList<>();
                    csvDataEntity.add(getCsvData().getValue(i, Constants.OPEN_IDENTIFIER));
                    csvDataEntity.add(getCsvData().getValue(i, Constants.HIGH_IDENTIFIER));
                    csvDataEntity.add(getCsvData().getValue(i, Constants.LOW_IDENTIFIER));
                    csvDataEntity.add(getCsvData().getValue(i, Constants.CLOSE_IDENTIFIER));
                    csvData.add(csvDataEntity);
                }

                dataManagerCollector.setCsvData(csvData);

                List<List<Double>> csvDataTransformed = new ArrayList<>();
                for(int i=(position-begin)+1; i <= position ; i++) {
                    List<Double> csvDataTransformedEntity = new ArrayList<>();
                    csvDataTransformedEntity.add(getCsvDataTransformed().getValue(i, Constants.OPEN_IDENTIFIER));
                    csvDataTransformedEntity.add(getCsvDataTransformed().getValue(i, Constants.HIGH_IDENTIFIER));
                    csvDataTransformedEntity.add(getCsvDataTransformed().getValue(i, Constants.LOW_IDENTIFIER));
                    csvDataTransformedEntity.add(getCsvDataTransformed().getValue(i, Constants.CLOSE_IDENTIFIER));
                    csvDataTransformed.add(csvDataTransformedEntity);
                }

                dataManagerCollector.setCsvDataTransformed(csvDataTransformed);

                dataManagerCollector.setExpectedValue(dataStruct.expectedVectors.get(position - begin + 1 + 3));

                List<Double> inputVectors = new ArrayList<>();
                for(int i=0; i<SharedMemory.inputCount; i++) {
                    inputVectors.add(dataStruct.inputVectors.get(position - begin + 1).get(i));
                }

                dataManagerCollector.setInputVectors(inputVectors);

                position++;

                return dataManagerCollector;
            }
            else
                return null;
        }

        @Override
        public void remove() {
            throw new NotImplementedException();
        }
    }

    public class DataManagerUnit {

        private List<List<Double>> csvData = new ArrayList<>();
        private List<List<Double>> csvDataTransformed = new ArrayList<>();
        private double expectedValue;
        private List<Double> inputVectors = new ArrayList<>();
        private int line;

        public List<List<Double>> getCsvData() {
            return csvData;
        }

        public int getCsvSize() {
            return csvData.size();
        }

        public double getCsvDataValue(int x, int y) {
            return csvData.get(x).get(y);
        }

        public void setCsvData(List<List<Double>> csvData) {
            this.csvData = csvData;
        }

        public double getCsvDataTransformedValue(int x, int y) {
            return csvDataTransformed.get(x).get(y);
        }

        public List<List<Double>> getCsvDataTransformed() {
            return csvDataTransformed;
        }

        public void setCsvDataTransformed(List<List<Double>> csvDataTransformed) {
            this.csvDataTransformed = csvDataTransformed;
        }

        public double getExpectedValue() {
            return expectedValue;
        }

        public void setExpectedValue(double expectedValue) {
            this.expectedValue = expectedValue;
        }

        public List<Double> getInputVectors() {
            return inputVectors;
        }

        public void setInputVectors(List<Double> inputVectors) {
            this.inputVectors = inputVectors;
        }

        public int getLine() {
            return line;
        }

        public void setLine(int line) {
            this.line = line;
        }
    }
}
