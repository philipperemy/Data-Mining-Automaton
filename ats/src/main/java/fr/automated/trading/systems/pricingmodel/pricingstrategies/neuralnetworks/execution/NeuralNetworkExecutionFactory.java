package fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.execution;

import fr.automated.trading.systems.utils.utils.SharedMemory;
import fr.automated.trading.systems.utils.utils.UIMode;

public class NeuralNetworkExecutionFactory {

    private NeuralNetworkExecutionFactory() {}
	
	public static NeuralNetworkExecution createInstance() {
        NeuralNetworkExecution neuralNetworkExecution = null;

        if(SharedMemory.uiMode == UIMode.GUI_MODE) {
            neuralNetworkExecution = new NeuralNetworkExecutionGUI();
        } else if(SharedMemory.uiMode == UIMode.CLI_MODE) {
            neuralNetworkExecution = new NeuralNetworkExecutionCLI();
        }

        return neuralNetworkExecution;
	}
}
