package fr.automated.trading.systems.utils.csvparser;

import fr.automated.trading.systems.exception.InvalidStateOfParser;
import fr.automated.trading.systems.utils.CSVData;
import fr.automated.trading.systems.utils.CSVDataImpl;
import fr.automated.trading.systems.utils.utils.AtsLogger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public abstract class CSVParserTransformer {

    protected CSVData transformedValues = new CSVDataImpl();
    protected boolean isFilledTransformed = false;
    protected CSVParser csvparser;

    public abstract void transform();

    protected abstract void onChange();

    private void init(CSVParser csvparser) {
        this.csvparser = csvparser;
        AtsLogger.log("countLines = " + csvparser.countLines());
        AtsLogger.log("countColumns = " + csvparser.countColumns());
    }

    public CSVParserTransformer(CSVParser csvparser) {
        init(csvparser);
        csvparser.register(this);
        transform();
    }

    protected CSVParserTransformer() {

    }

    public void debug() {
        if(isFilled()) {
            for(int x = 0; x < csvparser.countLines(); x++) {
                for(int y = 0; y < csvparser.countColumns(); y++)
                    System.out.print(getValue(x,y) + " ");
                System.out.println("");
            }
        }
    }

    public void write(String filename) {
        try {
            if(isFilled()) {
                PrintWriter writer =  new PrintWriter(new BufferedWriter(new FileWriter(filename)));
                for(int x = 0; x < csvparser.countLines(); x++) {
                    for(int y = 0; y < csvparser.countColumns(); y++) {
                        if(y == csvparser.countColumns()-1)
                            writer.print(getValue(x,y));
                        else
                            writer.print(getValue(x,y) + CSVParser.SEPARATOR);
                    }

                    if(x != csvparser.countLines()-1)	//Last line
                        writer.println("");
                }
                writer.close();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public CSVParser getCSVParserTransformed() {
        try {
            if(!isFilled())
                throw new InvalidStateOfParser();
            return new CSVParser(csvparser.getFilename(), this.transformedValues);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isFilled() {
        return isFilledTransformed;
    }

    public double getValue(int x, int y) {
        return transformedValues.getValue(x, y);
    }

    public CSVData getValues() {
        return transformedValues;
    }

    protected void setTransformed(boolean isFilledTransformed) {
        this.isFilledTransformed = isFilledTransformed;
    }

}
