package fr.automated.trading.systems.tradingrobot.datamanager.expectedvalues;

import fr.automated.trading.systems.utils.CSVData;
import fr.automated.trading.systems.utils.csvparser.CSVDataInterface;
import fr.automated.trading.systems.utils.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class GapBetweenTwoOpens implements BuildExpectedValues {

    public List<Double> build(CSVData csvData) {

        List<Double> expectedValues = new ArrayList<>();

        for(int i=0; i<csvData.countLines()-1; i++) {
            double currentPrice = csvData.getValue(i, Constants.OPEN_IDENTIFIER);
            double nextPrice = csvData.getValue(i+1, Constants.OPEN_IDENTIFIER);

            if(currentPrice < nextPrice)
                expectedValues.add((double) 1);
            else {
                expectedValues.add((double) 0);
            }
        }

        return expectedValues;
    }
}
