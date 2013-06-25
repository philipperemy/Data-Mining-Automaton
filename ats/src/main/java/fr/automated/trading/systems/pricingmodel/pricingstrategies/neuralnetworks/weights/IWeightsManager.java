package fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.weights;


public interface IWeightsManager {

    void setWeights(int x, int y, double value);

    void saveWeights();

    void loadWeights();

    void resetWeights();

    void addWeights(Weights weight);

    Weights getLastWeights();

    Weights getCurrentWeights();

    Weights getPreviousLastWeights();

}
