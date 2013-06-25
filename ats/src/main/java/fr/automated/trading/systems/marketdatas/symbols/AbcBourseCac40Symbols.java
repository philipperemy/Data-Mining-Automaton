package fr.automated.trading.systems.marketdatas.symbols;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class AbcBourseCac40Symbols implements MarketDataSymbols {

    private final Map<String, String> mappingCompaniesSymbols = new HashMap<>();

    public AbcBourseCac40Symbols() {
        fillList();
    }

    private void fillList() {
        mappingCompaniesSymbols.put("Accor","AC");
        mappingCompaniesSymbols.put("Air Liquide", "AI");
        mappingCompaniesSymbols.put("Alcatel Lucent", "ALU");
        mappingCompaniesSymbols.put("Alstom", "ALO");
        mappingCompaniesSymbols.put("Arcelor Mittal", "MT");
        mappingCompaniesSymbols.put("Axa", "CS");
        mappingCompaniesSymbols.put("Bnp Paribas", "BNP");
        mappingCompaniesSymbols.put("Bouygues", "EN");
        mappingCompaniesSymbols.put("Cap Gemini", "CAP");
        mappingCompaniesSymbols.put("Carrefour", "CA");
        mappingCompaniesSymbols.put("Credit Agricole", "ACA");
        mappingCompaniesSymbols.put("Danone", "BN");
        mappingCompaniesSymbols.put("Eads", "EAD");
        mappingCompaniesSymbols.put("EDF", "EDF");
        mappingCompaniesSymbols.put("Essilor Intl", "EI");
        mappingCompaniesSymbols.put("France Telecom", "FTE");
        mappingCompaniesSymbols.put("GDF Suez", "GSZ");
        mappingCompaniesSymbols.put("L'oreal", "OR");
        mappingCompaniesSymbols.put("Lafarge", "LG");
        mappingCompaniesSymbols.put("Legrand SA", "LR");
        mappingCompaniesSymbols.put("LVMH", "MC");
        mappingCompaniesSymbols.put("Michelin", "ML");
        mappingCompaniesSymbols.put("Pernod-Ricard", "RI");
        mappingCompaniesSymbols.put("Peugeot", "UG");
        mappingCompaniesSymbols.put("PPR", "PP");
        mappingCompaniesSymbols.put("Publicis", "PUB");
        mappingCompaniesSymbols.put("Renault", "RNO");
        mappingCompaniesSymbols.put("Safran", "SAF");
        mappingCompaniesSymbols.put("Saint Gobain", "SGO");
        mappingCompaniesSymbols.put("Sanofi", "SAN");
        mappingCompaniesSymbols.put("Schneider Electric", "SU");
        mappingCompaniesSymbols.put("Societe Generale", "GLE");
        mappingCompaniesSymbols.put("Stmicroelectronics", "STM");
        mappingCompaniesSymbols.put("Technip", "TEC");
        mappingCompaniesSymbols.put("Total", "FP");
        mappingCompaniesSymbols.put("Unibail-Rodamco", "UL");
        mappingCompaniesSymbols.put("Vallourec", "VK");
        mappingCompaniesSymbols.put("Veolia Environ.", "VIE");
        mappingCompaniesSymbols.put("Vinci", "DG");
        mappingCompaniesSymbols.put("Vivendi", "VIV");
        //mappingCompaniesSymbols.put("CAC40", "PX1");
    }

    public String[] getCompaniesNames() {
        Set<String> companiesNames = mappingCompaniesSymbols.keySet();
        Iterator<String> iterator = companiesNames.iterator();

        String[] companiesNamesArray = new String[companiesNames.size()];
        int i=0;
        while(iterator.hasNext()) {
            companiesNamesArray[i++] = iterator.next();
        }
        return companiesNamesArray;
    }

    public String getSymbol(String companyName) {
        return mappingCompaniesSymbols.get(companyName);
    }

    public Map<String, String> getMapping() {
        return mappingCompaniesSymbols;
    }
}