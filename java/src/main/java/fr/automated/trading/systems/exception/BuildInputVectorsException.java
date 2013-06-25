package fr.automated.trading.systems.exception;

import fr.automated.trading.systems.utils.utils.AtsLogger;

public class BuildInputVectorsException extends Exception {

    public BuildInputVectorsException() {
        AtsLogger.logException("build input vectors exception", this);
    }
}
