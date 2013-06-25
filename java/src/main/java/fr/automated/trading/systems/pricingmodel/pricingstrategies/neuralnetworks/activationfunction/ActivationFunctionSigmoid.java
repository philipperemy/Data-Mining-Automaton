package fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.activationfunction;

public class ActivationFunctionSigmoid implements ActivationFunction {

	public double calculate(double value) { 
		return 1.0/(1.0+Math.exp(-value));
	}

}
