package fr.automated.trading.systems.exception;

import fr.automated.trading.systems.utils.utils.AtsLogger;

public class ErrorValuesException extends Exception {

    public ErrorValuesException() {
        AtsLogger.logException("Error values exception", this);
    }
}
