package fr.automated.trading.systems.utils.csvparser;


import fr.automated.trading.systems.utils.utils.AtsLogger;

import java.util.ArrayList;

public class CSVParserTransformerBinary extends CSVParserTransformer {

    private CSVParserTransformerBinary() {
        super();
    }

    public CSVParserTransformerBinary(CSVParser csvparser) {
        super(csvparser);
    }

    @Override
    public void transform() {

        int iteratorX = 0;
        int iteratorY = 0;
        transformedValues.clear();
        for(int y = 0; y < csvparser.countColumns(); y++) {
            transformedValues.setValue(iteratorX, iteratorY++, 0.0);
        }

        iteratorX++;

        for (int x = 1; x < csvparser.countLines(); x++) {
            iteratorY = 0;
            for(int y = 0; y < csvparser.countColumns(); y++) {
                if(csvparser.getValue(x,y) > csvparser.getValue(x-1,y)) {
                    transformedValues.setValue(iteratorX, iteratorY++, 1.0);
                }
                else {
                    transformedValues.setValue(iteratorX, iteratorY++, 0.0);
                }
            }

            iteratorX++;

        }
        setTransformed(true);

        write(csvparser.getFilename() + ".BINARY");
    }

    @Override
    protected void onChange() {
        AtsLogger.log("CSV File has changed. Reloading transformed Data");
        transform();
    }

}
