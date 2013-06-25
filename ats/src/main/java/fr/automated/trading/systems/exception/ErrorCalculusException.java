package fr.automated.trading.systems.exception;

import fr.automated.trading.systems.utils.utils.AtsLogger;

public class ErrorCalculusException extends Exception {

    public ErrorCalculusException() {
        AtsLogger.logException("Error calculus exception", this);
    }
}
