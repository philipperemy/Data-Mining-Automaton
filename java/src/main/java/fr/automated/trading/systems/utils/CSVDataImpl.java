package fr.automated.trading.systems.utils;

import com.sun.deploy.util.ArrayUtil;
import fr.automated.trading.systems.exception.InvalidNewLineValuesException;

import java.util.Arrays;

public class CSVDataImpl implements CSVData {

    private double[][] array = null;

    private int maxLines = 6000;
    private int maxColumns = 5;

    private int lineCount = 1;
    private int columnCount = 1;

    private double resizeFactor = 1.5;

    //Copy constructor
    public CSVDataImpl(CSVData csvData) {
        this.setMaxLines(csvData.countLines() + 10);
        this.setMaxColumns(csvData.countColumns() + 10);
        this.lineCount = csvData.countLines();
        this.columnCount = csvData.countColumns();
        this.setArray(csvData.getArray(), true);
    }

    public CSVDataImpl(int maxLines, int maxColumns) {
        this.setMaxLines(maxLines);
        this.setMaxColumns(maxColumns);
        setArray(new double[this.getMaxLines()][this.getMaxColumns()], true);
    }

    public CSVDataImpl() {
        array = new double[this.getMaxLines()][this.getMaxColumns()];
    }

    public void resizeX() {
        double[][] array2 = new double[(int) (getMaxLines() *resizeFactor)][getMaxColumns()];
        for(int x = 0; x < lineCount; x++) {
            for(int y = 0; y < columnCount; y++) {
                array2[x][y] = getArray()[x][y];
            }
        }

        setMaxLines((int) (getMaxLines() *resizeFactor));
        setArray(array2, true);
    }

    public void resizeY() {
        double[][] array2 = new double[getMaxLines()][(int)(getMaxColumns() *resizeFactor)];
        for(int x = 0; x < lineCount; x++) {
            for(int y = 0; y < columnCount; y++) {
                array2[x][y] = getArray()[x][y];
            }
        }

        setMaxColumns((int)(getMaxColumns() *resizeFactor));
        setArray(array2, true);
    }

    @Override
    public double getValue(int x, int y) {
        return getArray()[x][y];
    }

    @Override
    public void setValue(int x, int y, double value) {
        if(x+1 > lineCount) {
            //New line is added

            if(x+1 > getMaxLines()) {
                resizeX();
            }

            lineCount = x+1;
        }
        if(y+1 > columnCount) {
            //New column is added

            if(y+1 > getMaxColumns()) {
                resizeY();
            }

            columnCount = y+1;
        }

        getArray()[x][y] = value;
    }

    public void debug() {
        for(int x = 0; x < lineCount; x++) {
            for(int y = 0; y < columnCount; y++) {
                System.out.print(getValue(x, y) + ", ");
            }
            System.out.println();
        }
    }

    @Override
    public double[] getLine(int x) {
        return Arrays.copyOfRange(getArray()[x], 0, columnCount);
    }

    @Override
    public void addLine(double[] rowRecords) throws InvalidNewLineValuesException {
        if(columnCount == rowRecords.length) {
            int lineId = lineCount;
            for(int y = 0; y < columnCount; y++) {
                setValue(lineId, y, rowRecords[y]);
            }
        } else {
            throw new InvalidNewLineValuesException(rowRecords.length, columnCount);
        }
    }

    //TODO test it
    @Override
    public void removeLastColumn() {
        double[][] array2 = new double[getMaxLines()][getMaxColumns()];
        for(int x = 0; x < lineCount; x++) {
            for(int y = 0; y < columnCount-1; y++) {
                array2[x][y] = array[x][y];
            }
        }

        columnCount--;
        setArray(array2, true);
    }

    //TODO test it
    @Override
    public void removeLine(int lineId) {
        double[][] array2 = new double[getMaxLines()][getMaxColumns()];
        int linearX = 0;
        for(int x = 0; x < lineCount; x++) {

            if(x == lineId) {
                continue;
            }

            for(int y = 0; y < columnCount; y++) {
                array2[linearX][y] = getArray()[x][y];
            }

            linearX++;
        }

        lineCount--;
        setArray(array2, true);
    }

    @Override
    public int countColumns() {
        return columnCount;
    }

    @Override
    public int countLines() {
        return lineCount;
    }


    public double[][] getArray() {
        return array;
    }

    public void setArray(double[][] array, boolean reinit) {
        this.array = array;
        if(!reinit) {
            this.lineCount = array.length;
            this.columnCount = array[0].length;
        }
    }

    public int getMaxLines() {
        return maxLines;
    }

    public void setMaxLines(int maxLines) {
        this.maxLines = maxLines;
    }

    public int getMaxColumns() {
        return maxColumns;
    }

    public void setMaxColumns(int maxColumns) {
        this.maxColumns = maxColumns;
    }

    @Override
    public void clear() {
        lineCount = 1;
        columnCount = 1;

        maxLines = 6000;
        maxColumns = 7;

        setArray(new double[getMaxLines()][getMaxColumns()], true);
    }

}
