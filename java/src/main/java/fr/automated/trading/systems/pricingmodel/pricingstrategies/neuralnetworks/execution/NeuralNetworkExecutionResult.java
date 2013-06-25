package fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.execution;

import java.util.Map;

public class NeuralNetworkExecutionResult {

    private int bestEpoch;
    private boolean isTradable;
    private Map<String, Integer> modelErrors;
    private double probabilityOk;
    private double expectedDirection;
    private double expectedVariation;

    public void setExpectedStockVariation(double expectedVariation) {
        this.expectedVariation = expectedVariation;
    }

    public void setExpectedStockDirection(double expectedDirection) {
        this.expectedDirection = expectedDirection;
    }

    public void setOkProbability(double probabilityOk) {
        this.probabilityOk = probabilityOk;
    }

    public void setModelError(Map<String, Integer> modelErrors) {
        this.modelErrors = modelErrors;
    }

    public void setBestEpoch(int bestEpoch) {
        this.bestEpoch = bestEpoch;
    }

    public int getBestEpoch() {
        return bestEpoch;
    }

    public boolean isTradable() {
        return isTradable;
    }

    public void setTradable(boolean tradable) {
        isTradable = tradable;
    }

    public Map<String, Integer> getModelErrors() {
        return modelErrors;
    }

    public double getProbabilityOk() {
        return probabilityOk;
    }

    public double getExpectedDirection() {
        return expectedDirection;
    }

    public double getExpectedVariation() {
        return expectedVariation;
    }

}
