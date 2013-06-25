package fr.automated.trading.systems.tradingrobot.robot;

import fr.automated.trading.systems.utils.utils.AtsLogger;

public class TradingRobotStub extends TradingRobot {

    public static int onChangeCount;

    @Override
    public void loop() {
        AtsLogger.log("loop() successful");
    }

    @Override
    public void onChange() {
        AtsLogger.log("onChange() successful");
        onChangeCount++;
    }
}
