package fr.automated.trading.systems.utils;

import fr.automated.trading.systems.exception.InvalidNewLineValuesException;

public interface CSVData {

    double getValue(int x, int y);

    void setValue(int x, int y, double value);
	
	int countColumns();
	
	int countLines();

    void debug();

    double[] getLine(int x);

    void addLine(double[] rowRecords) throws InvalidNewLineValuesException;

    void removeLastColumn();

    void removeLine(int lineId);

    public double[][] getArray();

    void setArray(double[][] array, boolean reinit);

    int getMaxLines();

    void setMaxLines(int maxLines);

    int getMaxColumns();

    void setMaxColumns(int maxColumns);

    void clear();
}
