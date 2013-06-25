package fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.layer;

import fr.automated.trading.systems.exception.RemoveNeuronOutOfBounds;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.neuron.INeuron;
import fr.automated.trading.systems.utils.utils.AtsLogger;

import java.util.ArrayList;

/**
 * Container for neurons. Layer implements ILayer.
 * @author Philippe REMY
 */
public class Layer implements ILayer {

	private final ArrayList<INeuron> neurons = new ArrayList<>();

	public void addNeuron(INeuron neuron) {
		neurons.add(neuron);
	}

	public void removeNeuron(int count) throws RemoveNeuronOutOfBounds {
		
		if(neurons.size() >= count)
		{
			for(int i=0; i<count; i++) {
                neurons.remove(i);
            }
		}
		else
			throw new RemoveNeuronOutOfBounds();
	}

	public INeuron getNeuron(int id) {
		return neurons.get(id);
	}

	public void freeLayer() {
		neurons.removeAll(neurons);
	}

	public void createNewLayer() {
		AtsLogger.log("layer created");
    }

	public int getCount() {
		return neurons.size();
	}

	public Layer() {
		createNewLayer();
	}

}
