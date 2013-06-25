package fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.core;

public class NeuralNetworkFactory {

    private NeuralNetworkFactory() {}

	public static NeuralNetwork createInstance() {
        return new NeuralNetwork();
	}
}
