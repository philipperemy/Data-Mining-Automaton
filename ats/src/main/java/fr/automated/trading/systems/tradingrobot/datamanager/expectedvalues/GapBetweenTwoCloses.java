package fr.automated.trading.systems.tradingrobot.datamanager.expectedvalues;

import fr.automated.trading.systems.utils.CSVData;
import fr.automated.trading.systems.utils.csvparser.CSVDataInterface;
import fr.automated.trading.systems.utils.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class GapBetweenTwoCloses implements BuildExpectedValues {

    public List<Double> build(CSVData csvData) {

        List<Double> expectedValues = new ArrayList<>();

        for(int i=0; i<csvData.countLines()-1; i++) {
            if(csvData.getValue(i, Constants.CLOSE_IDENTIFIER) < csvData.getValue(i+1, Constants.CLOSE_IDENTIFIER))
                expectedValues.add((double) 1);
            else {
                expectedValues.add((double) 0);
            }
        }

        return expectedValues;
    }
}
