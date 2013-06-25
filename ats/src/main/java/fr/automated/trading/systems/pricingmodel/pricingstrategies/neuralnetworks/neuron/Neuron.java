package fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.neuron;

public abstract class Neuron implements INeuron {

	private double value;

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	public Neuron() {
	}

}
