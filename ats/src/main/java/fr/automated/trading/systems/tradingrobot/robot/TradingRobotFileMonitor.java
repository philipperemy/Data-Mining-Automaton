package fr.automated.trading.systems.tradingrobot.robot;

import fr.automated.trading.systems.tradingrobot.monitor.filemonitoring.FileMonitorRobot;
import fr.automated.trading.systems.utils.utils.Constants;
import fr.automated.trading.systems.utils.utils.SharedMemory;

import java.io.File;
import java.util.TimerTask;


public class TradingRobotFileMonitor extends TradingRobot {

    @Override
    public void loop() {
        try {
            TimerTask task = new FileMonitorRobot(new File(SharedMemory.filename), this);

            while(true) {
                task.run();
                Thread.sleep(Constants.LATENCY_ON_DETECTING_FILE_MODIFICATION);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
