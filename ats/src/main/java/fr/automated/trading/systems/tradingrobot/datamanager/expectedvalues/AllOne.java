package fr.automated.trading.systems.tradingrobot.datamanager.expectedvalues;

import fr.automated.trading.systems.utils.CSVData;
import fr.automated.trading.systems.utils.csvparser.CSVDataInterface;

import java.util.ArrayList;
import java.util.List;

public class AllOne implements BuildExpectedValues {

    public List<Double> build(CSVData csvData) {
        List<Double> expectedValues = new ArrayList<>();

        for(int i=0; i<csvData.countLines()-1; i++) {
            expectedValues.add((double) 1);
        }

        return expectedValues;
    }
}
