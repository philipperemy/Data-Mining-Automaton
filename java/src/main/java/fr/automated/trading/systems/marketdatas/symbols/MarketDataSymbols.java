package fr.automated.trading.systems.marketdatas.symbols;


import java.util.Map;

public interface MarketDataSymbols {

    String[] getCompaniesNames();

    String getSymbol(String companyName);

    Map<String, String> getMapping();

}
