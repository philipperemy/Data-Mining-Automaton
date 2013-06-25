package fr.automated.trading.systems.tradingrobot.uppertier.multithread;

import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.execution.NeuralNetworkExecutionResult;

import java.util.ArrayList;
import java.util.List;

public class Collector {

    static List<List<Object>> collection = new ArrayList<>();

    public static synchronized void add(String symbol, NeuralNetworkExecutionResult neuralNetworkExecutionResult, long latency) {
        List<Object> list = new ArrayList<>();
        list.add(symbol);
        list.add(neuralNetworkExecutionResult);
        list.add(latency);
        collection.add(list);
    }

    public static List<List<Object>> getCollection() {
        return collection;
    }
}
