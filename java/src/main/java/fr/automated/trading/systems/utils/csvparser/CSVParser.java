package fr.automated.trading.systems.utils.csvparser;

import fr.automated.trading.systems.exception.InvalidCSVFile;
import fr.automated.trading.systems.exception.InvalidNewLineValuesException;
import fr.automated.trading.systems.exception.MaxValueFromColumnException;
import fr.automated.trading.systems.utils.CSVData;
import fr.automated.trading.systems.utils.CSVDataImpl;
import fr.automated.trading.systems.utils.utils.AtsLogger;
import fr.automated.trading.systems.utils.utils.Constants;
import fr.automated.trading.systems.utils.utils.ExtendedFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


public class CSVParser implements CSVDataInterface {

    private String filename;
    private CSVData values = new CSVDataImpl();
    public static final String SEPARATOR = Constants.SEPARATOR;
    private boolean isFilled = false;
    private List<CSVParserTransformer> csvParserTransformers = new ArrayList<>();

    public CSVParser(String filename, CSVData csvData) {
        this.values = new CSVDataImpl(csvData);
        this.filename = filename;
        setFilled(true);
    }

    public CSVParser() {

    }

    public double[] getLine(int x) {
        return values.getLine(x);
    }

    public String getFilename() {
        return filename;
    }

    public boolean isFilled() {
        return isFilled;
    }

    private void setFilled(boolean isFilled) {
        this.isFilled = isFilled;
    }

    public CSVData getValues() {
        return values;
    }

    public double getValue(int x, int y) {
        return values.getValue(x, y);
    }

    public int countColumns() {
        return values.countColumns();
    }

    public int countLines() {
        return values.countLines();
    }

    @Override
    public void addLine(double[] line) {
        try {
            values.addLine(line);
        } catch (InvalidNewLineValuesException e) {
            e.printStackTrace();
        }
    }

    public void debug() {
        if(isFilled) {
            for(int x = 0; x < countLines(); x++) {
                for(int y = 0; y < countColumns(); y++)
                    System.out.print(getValue(x,y) + " ");
                System.out.println("");
            }
        }
        else
            AtsLogger.log("Parser not filled !");
    }

    public double getMaxValueFromColumn(int y) throws MaxValueFromColumnException {
        if(y >= 0 && y < countColumns()) {
            double maxValue = getValue(0, y);
            for(int x = 0; x < values.countLines(); x++) {
                if(getValue(x,y) > maxValue)
                    maxValue = getValue(x,y);
            }
            return maxValue;
        }
        throw new MaxValueFromColumnException();
    }

    /*private int countOccurrences(String record, String pattern) {
        int lastIndex = 0;
        int count = 0;
        while(lastIndex != -1) {
            lastIndex = record.indexOf(pattern, lastIndex);
            if(lastIndex != -1) {
                count++;
                lastIndex += pattern.length();
            }
        }
        return count;
    }*/

    private void fillParser(ArrayList<String> records) {
        int iteratorX = 0;
        for(String record : records) {
            int iteratorY = 0;
            StringTokenizer stringtokenizer = new StringTokenizer(record, SEPARATOR);
            while (stringtokenizer.hasMoreTokens()) {
                values.setValue(iteratorX, iteratorY++, Double.parseDouble(stringtokenizer.nextToken()));
            }
            iteratorX++;
        }
    }

    public CSVParser(String filename) {

        try {
            this.filename = filename;
            ArrayList<String> records = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(this.filename));

            String line;
            while ((line = reader.readLine()) != null) {
                records.add(line);
            }

            fillParser(records);
            setFilled(true);

            reader.close();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }


    public void forceReload() {
        reloadObservers();
    }

    public void reload() throws InvalidCSVFile {

        int newCountColumns = (new ExtendedFile(filename)).countLines() - values.countLines();
        if(newCountColumns < 0) {
            throw new InvalidCSVFile();
        }
        else if(newCountColumns == 0) {
            AtsLogger.log("Nothing to change");
        }
        else {
            AtsLogger.log("CSVParser has changed.");
            ArrayList<String> newRecords = (new ExtendedFile(filename)).extractLines(values.countLines(), newCountColumns);
            fillParser(newRecords);
            reloadObservers();
        }
    }

    public void register(CSVParserTransformer csvParserTransformer) {
        AtsLogger.log("Registration of a CSVParserTransformer");
        csvParserTransformers.add(csvParserTransformer);
    }

    public void reloadObservers() {
        AtsLogger.log("ReloadObservers in progress");
        for(CSVParserTransformer csvParserTransformer : csvParserTransformers)
            csvParserTransformer.onChange();
    }

    public void write(String filename) {
        AtsLogger.log("writing to filename ... " + filename);
        try {
            if(isFilled()) {
                PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
                for(int x = 0; x < countLines(); x++) {
                    for(int y = 0; y < countColumns(); y++) {
                        if(y == countColumns()-1)
                            writer.print(getValue(x,y));
                        else
                            writer.print(getValue(x,y) + CSVParser.SEPARATOR);
                    }

                    if(x != countLines()-1)	//Last line
                        writer.println("");
                }
                writer.close();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        File f = new File(filename);
        if(f.exists())
            if(f.canWrite()) {
                boolean isDeleted = f.delete();
                if(!isDeleted)
                    throw new RuntimeException("File not deleted!");
            }
    }

    public void removeLine(int lineId) {
        values.removeLine(lineId);
    }

    public void removeLastLine() {
        removeLine(countLines()-1);
    }

    public void removeLastColumn() {
        values.removeLastColumn();
    }

}