package fr.automated.trading.systems.exception;

import fr.automated.trading.systems.utils.utils.AtsLogger;

public class InvalidCSVFile extends Exception {

	public InvalidCSVFile() {
        AtsLogger.logException("Invalid CSV File", this);
	}
}
