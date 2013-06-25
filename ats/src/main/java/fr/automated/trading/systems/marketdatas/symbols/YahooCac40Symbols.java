package fr.automated.trading.systems.marketdatas.symbols;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class YahooCac40Symbols implements MarketDataSymbols {

    private final Map<String, String> mappingCompaniesSymbols = new HashMap<>();

    public YahooCac40Symbols() {
        fillList();
    }

    private void fillList() {
        mappingCompaniesSymbols.put("Accor","AC.PA");
        mappingCompaniesSymbols.put("Air Liquide", "AI.PA");
        mappingCompaniesSymbols.put("Alcatel Lucent", "ALU.PA");
        mappingCompaniesSymbols.put("Alstom", "ALO.PA");
        mappingCompaniesSymbols.put("Arcelor Mittal", "MT.PA");
        mappingCompaniesSymbols.put("Axa", "CS.PA");
        mappingCompaniesSymbols.put("Bnp Paribas", "BNP.PA");
        mappingCompaniesSymbols.put("Bouygues", "EN.PA");
        mappingCompaniesSymbols.put("Cap Gemini", "CAP.PA");
        mappingCompaniesSymbols.put("Carrefour", "CA.PA");
        mappingCompaniesSymbols.put("Credit Agricole", "ACA.PA");
        mappingCompaniesSymbols.put("Danone", "BN.PA");
        mappingCompaniesSymbols.put("Eads", "EAD.PA");
        mappingCompaniesSymbols.put("EDF", "EDF.PA");
        mappingCompaniesSymbols.put("Essilor Intl", "EI.PA");
        mappingCompaniesSymbols.put("France Telecom", "FTE.PA");
        mappingCompaniesSymbols.put("GDF Suez", "GSZ.PA");
        mappingCompaniesSymbols.put("L'oreal", "OR.PA");
        mappingCompaniesSymbols.put("Lafarge", "LG.PA");
        mappingCompaniesSymbols.put("Legrand SA", "LR.PA");
        mappingCompaniesSymbols.put("LVMH", "MC.PA");
        mappingCompaniesSymbols.put("Michelin", "ML.PA");
        mappingCompaniesSymbols.put("Pernod-Ricard", "RI.PA");
        mappingCompaniesSymbols.put("Peugeot", "UG.PA");
        mappingCompaniesSymbols.put("PPR", "PP.PA");
        mappingCompaniesSymbols.put("Publicis", "PUB.PA");
        mappingCompaniesSymbols.put("Renault", "RNO.PA");
        mappingCompaniesSymbols.put("Safran", "SAF.PA");
        mappingCompaniesSymbols.put("Saint Gobain", "SGO.PA");
        mappingCompaniesSymbols.put("Sanofi", "SAN.PA");
        mappingCompaniesSymbols.put("Schneider Electric", "SU.PA");
        mappingCompaniesSymbols.put("Societe Generale", "GLE.PA");
        mappingCompaniesSymbols.put("Stmicroelectronics", "STM.PA");
        mappingCompaniesSymbols.put("Technip", "TEC.PA");
        mappingCompaniesSymbols.put("Total", "FP.PA");
        mappingCompaniesSymbols.put("Unibail-Rodamco", "UL.PA");
        mappingCompaniesSymbols.put("Vallourec", "VK.PA");
        mappingCompaniesSymbols.put("Veolia Environ.", "VIE.PA");
        mappingCompaniesSymbols.put("Vinci", "DG.PA");
        mappingCompaniesSymbols.put("Vivendi", "VIV.PA");
        //mappingCompaniesSymbols.put("CAC40", "^FCHI");
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

    public String getCompany(String symbol) {
        Set<String> companies = mappingCompaniesSymbols.keySet();
        for(String company : companies) {
            if(mappingCompaniesSymbols.get(company).equals(symbol)) {
                return company;
            }
        }
        throw new RuntimeException("No company for this symbol");
    }
}
