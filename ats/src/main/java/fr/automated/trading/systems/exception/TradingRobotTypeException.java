package fr.automated.trading.systems.exception;

import fr.automated.trading.systems.utils.utils.AtsLogger;

public class TradingRobotTypeException extends Exception {

    public TradingRobotTypeException() {
        AtsLogger.logException("Wrong trading robot type", this);
    }
}
