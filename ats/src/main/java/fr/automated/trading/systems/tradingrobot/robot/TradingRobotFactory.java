package fr.automated.trading.systems.tradingrobot.robot;

import fr.automated.trading.systems.exception.TradingRobotTypeException;

public class TradingRobotFactory {

    private TradingRobotFactory() {}

    public static TradingRobot createInstance(TradingRobotType tradingRobotType) {

        TradingRobot tradingRobot = null;

        try {

            switch (tradingRobotType) {
                case TRADING_ROBOT_FILE_MONITOR:
                    tradingRobot = new TradingRobotFileMonitor();
                    break;
                case TRADING_ROBOT_MARKET_DATA:
                    tradingRobot = new TradingRobotMarketData();
                    break;
                case TRADING_ROBOT_ONE_LOOP:
                    tradingRobot = new TradingRobotOneLoop();
                    break;
                default:
                    throw new TradingRobotTypeException();
            }

    } catch (TradingRobotTypeException e) {
        e.printStackTrace();
    }

    return tradingRobot;
}
}