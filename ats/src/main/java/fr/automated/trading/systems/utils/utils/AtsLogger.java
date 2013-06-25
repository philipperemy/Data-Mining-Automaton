package fr.automated.trading.systems.utils.utils;

import org.apache.log4j.xml.DOMConfigurator;

public class AtsLogger {

    static {
        DOMConfigurator.configure("xml/ATSlog4j.xml");
    }

    private static final org.apache.log4j.Logger logger =  org.apache.log4j.Logger.getRootLogger();

    public static void log(Double log) {
        logger.debug(log);
    }

    public static void log(String log) {
        logger.debug(log);
    }

    public static void logException(String errorMessage, Throwable e) {
        logger.debug(errorMessage, e);
    }


}
