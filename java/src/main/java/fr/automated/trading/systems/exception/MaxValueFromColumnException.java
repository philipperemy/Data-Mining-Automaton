package fr.automated.trading.systems.exception;

import fr.automated.trading.systems.utils.utils.AtsLogger;

public class MaxValueFromColumnException extends Exception {

    public MaxValueFromColumnException() {
        AtsLogger.logException("Max value from column exception", this);
    }
}
