package fr.automated.trading.systems.tradingrobot.datamanager.expectedvalues;

import fr.automated.trading.systems.utils.CSVData;
import fr.automated.trading.systems.utils.csvparser.CSVDataInterface;

import java.util.ArrayList;
import java.util.List;

public class RandomValues implements BuildExpectedValues {

    public List<Double> build(CSVData csvData) {
        List<Double> expectedValues = new ArrayList<>();

        for(int i=0; i<csvData.countLines()-1; i++) {

            int rand = (Math.random()<0.5)?0:1;

            if(rand>0.5) {
                expectedValues.add((double) 1);
            } else {
                expectedValues.add((double) 0);
            }

        }

        return expectedValues;
    }
}
