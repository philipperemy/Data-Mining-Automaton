package fr.automated.trading.systems.tradingrobot.monitor.filemonitoring;

import fr.automated.trading.systems.tradingrobot.robot.TradingRobot;

import java.io.File;

public class FileMonitorRobot extends FileMonitor {
	
	private final TradingRobot tradingrobot;

	public FileMonitorRobot(File file, TradingRobot tradingrobot) {
		super(file);
		this.tradingrobot = tradingrobot;
	}
	
	@Override
    public void onChange() {
		
		setLocked(true);
		tradingrobot.onChange();
		setLocked(false);
	
	}
}
