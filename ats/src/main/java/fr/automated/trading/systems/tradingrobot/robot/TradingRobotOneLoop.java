package fr.automated.trading.systems.tradingrobot.robot;

public class TradingRobotOneLoop extends TradingRobot {

    @Override
    public void loop() {
        onChange();
    }

}
