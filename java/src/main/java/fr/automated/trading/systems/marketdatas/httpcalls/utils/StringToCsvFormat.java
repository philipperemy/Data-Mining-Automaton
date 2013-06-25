package fr.automated.trading.systems.marketdatas.httpcalls.utils;

import fr.automated.trading.systems.utils.CSVData;
import fr.automated.trading.systems.utils.CSVDataImpl;
import fr.automated.trading.systems.utils.utils.AtsLogger;

import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//LINE LIMIT SET TO 5000
public class StringToCsvFormat {

    private static final int DEFAULT_YAHOO_CSV_COLUMN_SIZE = 7;
    private static final String YAHOO_SEPARATOR = ",";

    public String[][] convert(List<String> values) {
        if(values == null || values.isEmpty()) {
            AtsLogger.log("List values is null or empty");
            return null;
        }

        Collections.reverse(values);

        int linesCount = values.size();
        int columnCount = 0;

        StringTokenizer stringtokenizer = new StringTokenizer(values.get(0), YAHOO_SEPARATOR);
        while(stringtokenizer.hasMoreTokens()) {
            stringtokenizer.nextToken();
            columnCount++;
        }

        String[][] valuesArray = new String[linesCount][columnCount];

        int iteratorX = 0;
        for(String value : values) {
            int iteratorY = 0;
            stringtokenizer = new StringTokenizer(value, YAHOO_SEPARATOR);
            while(stringtokenizer.hasMoreTokens()) {
                valuesArray[iteratorX][iteratorY++] = stringtokenizer.nextToken();
            }
            iteratorX++;
        }

        return valuesArray;

    }

    public CSVData enhancedPrune(String[][] results) {
        double [][] doubleResults = new double[results.length-1][5];
        //Open, High, Low, Close, Volume
        CSVData csvData = new CSVDataImpl();
        //x = 1 => no headers please (they are put at the end due to the reversal of the array
        for(int x = 0; x < results.length-1; x++) {
            for(int y = 1; y < results[x].length-1; y++) {
                doubleResults[x][y-1] = new Double(results[x][y]);
            }
        }

        csvData.setArray(doubleResults, false);
        return csvData;
    }

    @Deprecated
    public String[][] prune(String[][] results, String filename, int horizon) {
        // Date;Ope;High;Low;Close;Volume;Adj Close
        int open = 1, high = 2, low = 3, close = 4, volume = 5;

        String[][] pruned = new String[results.length][5];

        for(int i=0; i<results.length; i++) {
            int j = 0;
            pruned[i][j] = results[i][open];
            j++;
            pruned[i][j] = results[i][high];
            j++;
            pruned[i][j] = results[i][low];
            j++;
            pruned[i][j] = results[i][close];
            j++;
            pruned[i][j] = results[i][volume];
        }


        AtsLogger.log("countLines = " + results.length);
        //Remove headers as well


        String[][] withoutHeaders = new String[results.length-1][5];

        for(int i=1; i<results.length; i++) {
            System.arraycopy(pruned[i], 0, withoutHeaders[i - 1], 0, 5);
        }
        
        String[][] reverse = new String[results.length-1][5];

        for(int i=0; i<results.length-1; i++) {
            System.arraycopy(withoutHeaders[i], 0, reverse[(results.length - 2) - i], 0, 5);
        }

        File f = new File(filename);
        f.delete();

        int begin = 0;

        reverse = timeHorizonTransformation(reverse, horizon);
        reverse = format(reverse);

        if(reverse.length > 5000)
            begin = reverse.length - 5000;

        File file = new File(filename);
        new File(file.getParent()).mkdirs();

        for(int i=begin; i<reverse.length; i++) {

            PrintWriter writer = null;
            try {
                writer = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
            } catch (IOException e) {
                AtsLogger.logException("", e);
            }

            if(i!=begin)
                writer.println();

            if(i == reverse.length -1 ) {
                for(int j=0; j<5; j++) {
                    if(j == 4)
                        writer.print(reverse[i][j]);
                    else
                        writer.print(reverse[i][j] + ";");
                }
            } else {

                for(int j=0; j<5; j++) {
                    if(j == 4)
                        writer.print(reverse[i][j]);
                    else
                        writer.print(reverse[i][j] + ";");
                }
            }

            writer.close();
        }

        return reverse;
    }
    
    String[][] timeHorizonTransformation(String[][] inputs, int timeHorizon) {

        ArrayList<ArrayList<Double>> transValues = new ArrayList<>();

        if(timeHorizon <=1)
            return inputs;

        int blockNumber = (inputs.length / timeHorizon);
        int begin = inputs.length - blockNumber*timeHorizon;

        double currentValue;
        for(int j=0; j<5; j++) {

            ArrayList<Double> currentColumnTransValues = new ArrayList<>();
           for(int i=begin; i<inputs.length; i+=timeHorizon) {
               currentValue = mean(inputs, j, i, i+timeHorizon);
               currentColumnTransValues.add(currentValue);
           }

           transValues.add(currentColumnTransValues);
        }

        String[][] arrayReturned = new String[transValues.get(0).size()][transValues.size()];

        for(int j=0; j<transValues.size(); j++) {
            for(int i=0; i<transValues.get(0).size();i++) {
                arrayReturned[i][j] = String.valueOf(transValues.get(j).get(i));
            }
        }

        return arrayReturned;
    }

    private double mean(String[][] inputs, int column, int a, int b) {
        double sum = 0;
        for(int i=a; i<b; i++) {
            sum += Double.parseDouble(inputs[i][column]);
        }

        return sum / (b-a);
    }
    
    private String[][] format(String[][] inputs) {
        
        String[][] results = new String[inputs.length][5];
        
        for(int i=0; i<inputs.length; i++) {
            for(int j=0; j<5; j++) {
                double value = Double.parseDouble(inputs[i][j]);
                NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
                DecimalFormat df = (DecimalFormat)nf;
                //DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits(2);
                df.setMinimumFractionDigits(2);
                results[i][j] = getRidOfCommasInBigNumbers(df.format(value));
            }
        }

        return results;
    }

    private String getRidOfCommasInBigNumbers(String input) {

        String regex = "(?<=[\\d])(,)(?=[\\d])";
        Pattern p = Pattern.compile(regex);

        String str = input;
        Matcher m = p.matcher(str);

        str = m.replaceAll("");
        return str;
    }

}
