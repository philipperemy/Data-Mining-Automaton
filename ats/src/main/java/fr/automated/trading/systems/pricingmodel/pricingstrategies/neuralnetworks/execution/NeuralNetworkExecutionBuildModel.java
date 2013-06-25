package fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.execution;

import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.core.NeuralNetworkBuildModel;


public class NeuralNetworkExecutionBuildModel extends NeuralNetworkExecutionCLI {

    public void buildModel() {
        NeuralNetworkBuildModel neuralNetworkBuildModel = new NeuralNetworkBuildModel();
        neuralNetworkBuildModel.setDataManager(dataManager);
        neuralNetworkBuildModel.setNeuralNetwork(neuralnetwork);
        neuralNetworkBuildModel.fill();
        neuralNetworkBuildModel.printRecords();
    }

}
