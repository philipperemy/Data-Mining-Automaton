package fr.automated.trading.systems.exception;

import fr.automated.trading.systems.utils.utils.AtsLogger;

public class InvalidStateOfParser extends Exception {

	public InvalidStateOfParser() {
        AtsLogger.logException("Invalid State of Parser", this);
	}
}
