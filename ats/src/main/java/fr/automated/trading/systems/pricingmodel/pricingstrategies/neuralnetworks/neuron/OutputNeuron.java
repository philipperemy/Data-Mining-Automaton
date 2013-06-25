package fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.neuron;

import fr.automated.trading.systems.utils.utils.AtsLogger;

public class OutputNeuron extends Neuron implements INeuron {

	public OutputNeuron() {
		super();
		AtsLogger.log("Output neuron created");
	}

}
