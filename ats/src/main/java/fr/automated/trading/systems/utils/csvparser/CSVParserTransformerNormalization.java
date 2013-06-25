package fr.automated.trading.systems.utils.csvparser;

import fr.automated.trading.systems.exception.MaxValueFromColumnException;
import fr.automated.trading.systems.utils.utils.AtsLogger;

import java.util.ArrayList;

public class CSVParserTransformerNormalization extends CSVParserTransformer {

	public CSVParserTransformerNormalization(CSVParser csvparser) {
		super(csvparser);
	}

    @Override
	public void transform() {
        transformedValues.clear();
        int iteratorX = 0;
        for (int x = 0; x < csvparser.countLines(); x++) {
            int iteratorY = 0;
            for(int y = 0; y < csvparser.countColumns(); y++) {
                try {
                    transformedValues.setValue(iteratorX, iteratorY++, csvparser.getValue(x,y) / csvparser.getMaxValueFromColumn(y));
                } catch (MaxValueFromColumnException e) {
                    e.printStackTrace();
                }
            }
            iteratorX++;
        }
		setTransformed(true);

        write(csvparser.getFilename() + ".NORMALIZED");
	}

    @Override
    protected void onChange() {
        AtsLogger.log("CSV File has changed. Reloading transformed Data");
        transform();
    }

}
