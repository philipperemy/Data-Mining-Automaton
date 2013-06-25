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

@Deprecated
public class MarketData {

    private String lastDateRetrieved;

    public void getMarketData(String symbol, String filename, int timePeriod, boolean onlyYahoo) {
        HttpCallYahoo httpCall = new HttpCallYahoo();
        List<String> yahooPricesString = httpCall.execute(symbol);

        StringToCsvFormat stringToCsvFormat = new StringToCsvFormat();
        String[][] returnedArray = stringToCsvFormat.convert(yahooPricesString);

        CSVData csvData = stringToCsvFormat.enhancedPrune(returnedArray);

        CSVParser csvParser = new CSVParser(filename, csvData);
        csvParser.removeLastColumn();

        //Yahoo duplicates
        if(returnedArray[1][0].equals(returnedArray[2][0])) {
            csvParser.removeLastLine();
        }

        if(!onlyYahoo) {
            try {
                List<Double> lastPrices = getLastPricesFromAbcBourse(SymbolsConverter.YahooToAbcBourseSymbol(symbol));
                double[] lastPricesArray = Utils.doubleListToArray(lastPrices);
                csvParser.addLine(lastPricesArray);

            } catch (Exception e) {
                AtsLogger.logException("cannot get last prices from AbcBourse", e);
            }
        }

        csvParser.write(filename);
        lastDateRetrieved = returnedArray[1][0];

    }

    public void addLastPricesFromAbcBourseToFile(String symbol, String filename) {
        try {
            CSVParser csvParser = new CSVParser(filename);
            List<Double> lastPrices = getLastPricesFromAbcBourse(SymbolsConverter.YahooToAbcBourseSymbol(symbol));
            csvParser.addLine(Utils.doubleListToArray(lastPrices));
            csvParser.write(filename);
        } catch (Exception e) {
            e.printStackTrace();
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

    public void getOnlyYahooMarketData(String symbol, String filename) {
        getMarketData(symbol, filename, 1, true);
    }

    public void getMarketData(String symbol, String filename) {
        getMarketData(symbol, filename, 1, false);
    }

    public void getMarketDataGeneric(String symbol, String filename) {
        getOnlyYahooMarketData(symbol, filename);
        String lastDateRetrieved = getLastDateRetrieved();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if(Utils.todayIsBusinessDay()) {
            if(!lastDateRetrieved.equals(dateFormat.format(new Date()))) {
                addLastPricesFromAbcBourseToFile(symbol, filename);
            }
        }
    }

}
