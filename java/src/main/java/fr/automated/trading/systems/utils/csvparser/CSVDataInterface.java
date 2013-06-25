package fr.automated.trading.systems.utils.csvparser;

import fr.automated.trading.systems.utils.CSVData;


public interface CSVDataInterface {

    boolean isFilled();

    String getFilename();

	CSVData getValues();

    double getValue(int x, int y);
	
	int countColumns();
	
	int countLines();

    void addLine(double[] line);

}
