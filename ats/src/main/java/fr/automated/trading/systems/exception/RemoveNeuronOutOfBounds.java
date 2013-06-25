package fr.automated.trading.systems.exception;

import fr.automated.trading.systems.utils.utils.AtsLogger;

public class RemoveNeuronOutOfBounds extends Exception {

	public RemoveNeuronOutOfBounds() {
		AtsLogger.logException("You specified a value too big. The layer does not contain such a number of neurons", this);
	}

}
