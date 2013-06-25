package fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.neuron;

import fr.automated.trading.systems.utils.utils.AtsLogger;

public class HiddenNeuron extends Neuron implements INeuron {

	public HiddenNeuron() {
		super();
		AtsLogger.log("Hidden neuron created");
	}

}
