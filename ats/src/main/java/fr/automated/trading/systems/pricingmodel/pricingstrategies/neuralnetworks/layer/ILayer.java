package fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.layer;

import fr.automated.trading.systems.exception.RemoveNeuronOutOfBounds;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.neuron.INeuron;

public interface ILayer {
	
	public void addNeuron(INeuron neuron);
	
	public void removeNeuron(int count) throws RemoveNeuronOutOfBounds;
	
	public void createNewLayer();
	
	public void freeLayer();
	
	public int getCount();
	
	public INeuron getNeuron(int id);

}
