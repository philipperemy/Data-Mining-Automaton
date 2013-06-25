package fr.automated.trading.systems.utils.utils;

public class Constants {

	private Constants() {}
	
	//If this constant is changed, the neural kernel has to refactored.
	public static final int OUTPUT_COUNT = 1;
	
	//Can be changed to fasten slightly the algorithm. Loop is going to be active and UC near to 100% if the latency is too low.
	public static final int LATENCY_ON_DETECTING_FILE_MODIFICATION = 1000;

	public static final String SEPARATOR = ";";
	
	//Neural network parameter.
	public static final int MAX_WEIGHTS = 20;

    public static int DISP_REFRESH_EPOCHS = 5;

    public static final int OPEN_IDENTIFIER = 0;
    public static final int HIGH_IDENTIFIER = 1;
    public static final int LOW_IDENTIFIER = 2;
    public static final int CLOSE_IDENTIFIER = 3;

}