package fr.automated.trading.systems.exception;

import fr.automated.trading.systems.utils.utils.AtsLogger;

public class InvalidNewLineValuesException extends Exception {

	public InvalidNewLineValuesException(int length, int expected) {
        AtsLogger.logException("Invalid New line values. Length parameter : " + length + " instead of expected value : " + expected, this);
	}
}
