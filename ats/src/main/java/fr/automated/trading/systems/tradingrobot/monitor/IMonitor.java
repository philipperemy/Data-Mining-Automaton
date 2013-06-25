package fr.automated.trading.systems.tradingrobot.monitor;

public interface IMonitor extends Runnable {

    void run();

    void onChange();

}
