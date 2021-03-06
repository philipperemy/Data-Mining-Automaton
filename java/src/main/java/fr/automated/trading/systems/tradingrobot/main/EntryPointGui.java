package fr.automated.trading.systems.tradingrobot.main;

import fr.automated.trading.systems.tradingrobot.robot.TradingRobot;
import fr.automated.trading.systems.tradingrobot.robot.TradingRobotFactory;
import fr.automated.trading.systems.utils.utils.SharedMemory;
import fr.automated.trading.systems.utils.utils.UIMode;
import fr.automated.trading.systems.utils.utils.Utils;

public class EntryPointGui {

    public static void run(String propertiesFilename) {

        SharedMemory.uiMode = UIMode.GUI_MODE;

        try {
            Utils.loadProperties(propertiesFilename);
            TradingRobot tradingRobot = TradingRobotFactory.createInstance(SharedMemory.tradingRobotType);
            tradingRobot.run();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

}