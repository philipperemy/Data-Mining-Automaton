package fr.automated.trading.systems.tradingrobot.uppertier.multithread;

import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.execution.NeuralNetworkExecutionResult;
import fr.automated.trading.systems.utils.excel.ExcelAPI;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExcelWriter {

    public static void write() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new Date());
        ExcelAPI excelAPI = new ExcelAPI("files/repeater/" + "result_"+ date + ".xls");

        int y = 0;
        int x = 0;
        excelAPI.write(x++, y, "Symbol");
        excelAPI.write(x++, y, "Is tradable");
        excelAPI.write(x++, y, "Expected direction");
        excelAPI.write(x++, y, "Expected variation");
        excelAPI.write(x++, y, "Best epochs");
        excelAPI.write(x++, y, "P(OK)");
        excelAPI.write(x++, y, "P(KO)");
        excelAPI.write(x++, y, "Latency");
        y++;

        List<List<Object>> results = Collector.getCollection();
        for(List<Object> result : results) {

            String symbol = (String) result.get(0);
            NeuralNetworkExecutionResult networkExecutionResult = (NeuralNetworkExecutionResult) result.get(1);

            x = 0;
            excelAPI.write(x++, y, symbol);
            excelAPI.write(x++, y, networkExecutionResult.isTradable());
            excelAPI.write(x++, y, networkExecutionResult.getExpectedDirection());
            excelAPI.write(x++, y, networkExecutionResult.getExpectedVariation());
            excelAPI.write(x++, y,networkExecutionResult.getBestEpoch());

            int ok = networkExecutionResult.getModelErrors().get("OK");
            excelAPI.write(x++, y, ok);

            int ko = networkExecutionResult.getModelErrors().get("KO");
            excelAPI.write(x++, y, ko);
            Long latency = (Long) result.get(2);
            excelAPI.write(x++, y, latency.longValue());

            y++;
        }

        excelAPI.close();
    }
}
