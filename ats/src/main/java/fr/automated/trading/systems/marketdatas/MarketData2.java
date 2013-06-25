package fr.automated.trading.systems.marketdatas;

import fr.automated.trading.systems.marketdatas.httpcalls.HttpCallAbcBourse;
import fr.automated.trading.systems.marketdatas.httpcalls.HttpCallYahoo;
import fr.automated.trading.systems.marketdatas.httpcalls.utils.AbcBourseLastPrices;
import fr.automated.trading.systems.marketdatas.httpcalls.utils.StringToCsvFormat;
import fr.automated.trading.systems.marketdatas.symbols.SymbolsConverter;
import fr.automated.trading.systems.utils.CSVData;
import fr.automated.trading.systems.utils.csvparser.CSVParser;
import fr.automated.trading.systems.utils.utils.AtsLogger;
import fr.automated.trading.systems.utils.utils.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MarketData2 {

    private String lastDateRetrieved;

    public void getMarketData(String symbol, String filename, boolean onlyYahoo) {
        HttpCallYahoo httpCall = new HttpCallYahoo();
        List<String> yahooPricesString = httpCall.execute(symbol);

        StringToCsvFormat stringToCsvFormat = new StringToCsvFormat();
        String[][] returnedArray = stringToCsvFormat.convert(yahooPricesString);

        CSVData csvData = stringToCsvFormat.enhancedPrune(returnedArray);

        CSVParser csvParser = new CSVParser(filename, csvData);
        csvParser.removeLastColumn();
        csvParser.write(filename);

        lastDateRetrieved = returnedArray[1][0];
        String previousToLastDateRetrieved = returnedArray[2][0];

        //Yahoo duplicates
        if(lastDateRetrieved.equals(previousToLastDateRetrieved)) {
            csvParser.removeLastLine();
        }

        //after 17:30, yahoo updates the price but it's not accurate at all
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if(Utils.todayIsBusinessDay()) {
            if(lastDateRetrieved.equals(dateFormat.format(new Date()))) {
                csvParser.removeLastLine();
                csvParser.write(filename);
                if(!onlyYahoo) {
                    addLastPricesFromAbcBourseToFile(symbol, filename);
                }
            }
        } else {
            csvParser.write(filename);
        }
    }

    public void addLastPricesFromAbcBourseToFile(String symbol, String filename) {
        try {
            CSVParser csvParser = new CSVParser(filename);
            ArrayList<Double> lastPrices = (ArrayList<Double>) getLastPricesFromAbcBourse(SymbolsConverter.YahooToAbcBourseSymbol(symbol));
            double[] lastPricesArray = Utils.doubleListToArray(lastPrices);
            csvParser.addLine(lastPricesArray);
            csvParser.write(filename);
        } catch (Exception e) {
            AtsLogger.logException("cannot get last prices from AbcBourse", e);
        }
    }

    public String getLastDateRetrieved() {
        return lastDateRetrieved;
    }

    public List<Double> getLastPricesFromAbcBourse(String symbol) {
        HttpCallAbcBourse httpCall = new HttpCallAbcBourse();
        String AbcBourseSource = httpCall.executeString(symbol);
        AbcBourseLastPrices lastPrices = new AbcBourseLastPrices(AbcBourseSource);
        return lastPrices.getLastPrices();
    }

    //get all market datas with yahoo + abcbourse
    public void getMarketData(String symbol, String filename) {
        getMarketData(symbol, filename, false);
    }
    
    public void getMarketDataOnlyYahoo(String symbol, String filename) {
        getMarketData(symbol, filename, true);
    }

}
