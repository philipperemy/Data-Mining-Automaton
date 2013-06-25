package fr.automated.trading.systems.exception;

import fr.automated.trading.systems.utils.utils.AtsLogger;

public class PropertiesException extends Exception {

	public PropertiesException() {
        AtsLogger.logException("Error detected in properties file.", this);
    }
	
	public PropertiesException(String message) {
		AtsLogger.logException("Error detected in properties file. Message : " + message, this);
	}
}
