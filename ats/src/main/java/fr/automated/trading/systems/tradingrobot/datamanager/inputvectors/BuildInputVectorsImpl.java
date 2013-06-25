package fr.automated.trading.systems.tradingrobot.datamanager.inputvectors;

import fr.automated.trading.systems.exception.BuildInputVectorsException;
import fr.automated.trading.systems.utils.CSVData;
import fr.automated.trading.systems.utils.csvparser.CSVDataInterface;

import java.util.ArrayList;
import java.util.List;

public class BuildInputVectorsImpl implements BuildInputVectors {

    public List<List<Double>> build(CSVData csvData, int inputNeuronsCount) {
        
        List<List<Double>> inputVectors = new ArrayList<>();
        
        try {

            if(inputNeuronsCount % csvData.countColumns() != 0) {
                throw new BuildInputVectorsException();
            }

            int neuronsPerColumn = inputNeuronsCount / csvData.countColumns();

            for(int line = neuronsPerColumn-1; line<csvData.countLines(); line++) {
                List<Double> lineVector = new ArrayList<>();
                for(int column=0; column<csvData.countColumns(); column++) {
                    for(int shift=line-neuronsPerColumn+1; shift<=line; shift++) {
                        lineVector.add(csvData.getValue(shift, column));
                    }
                }

                inputVectors.add(lineVector);
            }

        } catch(BuildInputVectorsException e) {
            e.printStackTrace();
        }

        return inputVectors;
    }

}

// 16 neurones, 4 columns so 4 neurons for each column.
// Line range is from 4 to countLines()
// For each column
// Adding range from line-4 to line
// When the 4 columns have been reached. Push vector line