package fr.automated.trading.systems.marketdatas.symbols;

import java.util.Map;

public class SymbolsConverter {

    public static String YahooToAbcBourseSymbol(String yahooSymbol) {
        YahooCac40Symbols yahooSymbols = new YahooCac40Symbols();
        Map<String, String> yahooMapping = yahooSymbols.getMapping();

        String company = "";
        for (Map.Entry<String, String> currentEntry : yahooMapping.entrySet()) {
            if(currentEntry.getValue().equals(yahooSymbol)) {
                company = currentEntry.getKey();
                break;
            }
        }

        AbcBourseCac40Symbols abcBourseSymbols = new AbcBourseCac40Symbols();
        return abcBourseSymbols.getSymbol(company);
    }

    public static String AbcBourseToYahooSymbol(String abcBourseSymbol) {
        AbcBourseCac40Symbols abcBourseSymbols = new AbcBourseCac40Symbols();
        Map<String, String> yahooMapping = abcBourseSymbols.getMapping();

        String company = "";
        for (Map.Entry<String, String> currentEntry : yahooMapping.entrySet()) {
            if(currentEntry.getValue().equals(abcBourseSymbol)) {
                company = currentEntry.getKey();
                break;
            }
        }

        YahooCac40Symbols yahooSymbols = new YahooCac40Symbols();
        return yahooSymbols.getSymbol(company);
    }

}
