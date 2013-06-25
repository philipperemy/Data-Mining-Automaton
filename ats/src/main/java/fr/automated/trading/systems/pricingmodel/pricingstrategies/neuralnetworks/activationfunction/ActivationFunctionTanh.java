package fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.activationfunction;

public class ActivationFunctionTanh implements ActivationFunction {

	public double calculate(double value) {
		return (Math.exp(2*value)-1)/(Math.exp(2*value)+1);
	}

}
