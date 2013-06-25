package fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.weights;

import fr.automated.trading.systems.utils.utils.CircularList;

public class WeightsManager implements IWeightsManager {

    private Weights weights;
    private Weights savedWeights;
    private final CircularList<Weights> weightsList = new CircularList<>();

    public WeightsManager(int sizeX, int sizeY) {
        savedWeights = new Weights(sizeX, sizeY);
        weights = new Weights(sizeX, sizeY);
    }

    public synchronized void setWeights(int x, int y, double value) {
        weights.setWeight(x, y, value);
    }

    public void saveWeights() {
        savedWeights = weights.clone();
    }

    public synchronized void loadWeights() {
        weights = savedWeights.clone();
    }

    public synchronized void resetWeights() {
        weightsList.clear();
        savedWeights.random();
        weights.random();
    }

    public synchronized void addWeights(Weights weight) {
        weightsList.add(weight);
    }

    public synchronized Weights getLastWeights() {
        return weightsList.getLast();
    }

    public Weights getCurrentWeights() {
        return weights;
    }

    public synchronized Weights getPreviousLastWeights() {
        return weightsList.getLastPrevious();
    }
    
}
