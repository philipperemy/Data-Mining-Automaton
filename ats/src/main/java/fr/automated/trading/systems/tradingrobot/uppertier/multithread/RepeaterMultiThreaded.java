package fr.automated.trading.systems.tradingrobot.uppertier.multithread;

import fr.automated.trading.systems.marketdatas.symbols.YahooCac40Symbols;
import fr.automated.trading.systems.utils.utils.AtsLogger;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RepeaterMultiThreaded {

    public RepeaterMultiThreaded() {}

    public static void main(String[] args) {
        run();
    }

    public static void run() {

        long start = System.currentTimeMillis();

        Map<String, String> mappingSymbols = (new YahooCac40Symbols()).getMapping();
        Collection<String> symbols = mappingSymbols.values();

        ExecutorService  executorService = Executors.newFixedThreadPool(2);

        for(String symbol : symbols) {
            executorService.submit(new UnitaryRepeater(symbol));
        }

        executorService.shutdown();

        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            AtsLogger.logException("", e);
        }

        AtsLogger.log("time elapsed : " + (System.currentTimeMillis()-start));
        ExcelWriter.write();
    }
}
