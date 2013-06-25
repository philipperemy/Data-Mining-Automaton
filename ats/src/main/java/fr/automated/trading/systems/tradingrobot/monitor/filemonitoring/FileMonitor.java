package fr.automated.trading.systems.tradingrobot.monitor.filemonitoring;

import fr.automated.trading.systems.tradingrobot.monitor.IMonitor;

import java.io.File;
import java.util.TimerTask;

public abstract class FileMonitor extends TimerTask implements IMonitor {
	
	private long timeStamp = 0;
	private boolean lock = false;
	private final File file;
	
	public FileMonitor(File file) {
		this.file = file;
	}
	
	public void run() {

		long timeStamp = file.lastModified();

		if(this.timeStamp != timeStamp) {
			this.timeStamp = timeStamp;

			if(!isLocked())
				onChange();
	    }
	}

	public abstract void onChange();
	
	protected void setLocked(boolean lock) {
		this.lock = lock;
	}

	protected boolean isLocked() {
		return lock;
	}
}