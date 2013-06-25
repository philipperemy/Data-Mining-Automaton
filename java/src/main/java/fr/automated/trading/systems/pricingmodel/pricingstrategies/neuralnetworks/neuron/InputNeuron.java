package fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.neuron;

import fr.automated.trading.systems.utils.utils.AtsLogger;

public class InputNeuron extends Neuron implements INeuron {

	public InputNeuron() {
		super();
		AtsLogger.log("Input neuron created");
	}

}
