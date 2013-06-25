package fr.automated.trading.systems.utils.utils;

import fr.automated.trading.systems.exception.PropertiesException;
import fr.automated.trading.systems.tradingrobot.robot.TradingRobotType;

public class SharedMemory {

    public static UIMode uiMode = UIMode.CLI_MODE;
    public static String filename = "files/AAPL.csv";
    public static int inputCount = 16;
    public static int hiddenCount = 4;
    public static double momentum = 0.9;
    public static double learningRate = 0.1;
    public static int epochs = 300;
    public static int percentage = 70;
    public static TradingRobotType tradingRobotType;
    public static int lastDelimiter = 0;
    public static NeuralExecutionMode neuralNetworkExecutionMode = NeuralExecutionMode.DISP;

    public static void check() throws PropertiesException {
        if(SharedMemory.filename == null
        		|| hiddenCount == 0
                || inputCount == 0
        		|| momentum == 0.0
        		|| learningRate == 0.0
        		|| epochs == 0
        		|| percentage == 0)
            throw new PropertiesException();
    }

    public static void debug() {
        AtsLogger.log("filename = " + filename);
        AtsLogger.log("hiddenCount = " + hiddenCount);
        AtsLogger.log("inputCount = " + inputCount);
        AtsLogger.log("momentum = " + momentum);
        AtsLogger.log("learningRate = " + learningRate);
        AtsLogger.log("epochs = " + epochs);
        AtsLogger.log("percentage = " + percentage + " %");
        AtsLogger.log("uiMode = " + uiMode.name());
    }
}
