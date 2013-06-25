package fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.core;

import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.activationfunction.ActivationFunction;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.activationfunction.ActivationFunctionSigmoid;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.layer.Layer;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.neuron.HiddenNeuron;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.neuron.INeuron;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.neuron.InputNeuron;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.neuron.OutputNeuron;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.weights.IWeightsManager;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.weights.Weights;
import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.weights.WeightsManager;
import fr.automated.trading.systems.utils.utils.AtsLogger;
import fr.automated.trading.systems.utils.utils.Constants;

import java.util.ArrayList;

public class NeuralNetwork {

    private final ArrayList<Layer> layers = new ArrayList<>();
    private final IWeightsManager weightsManager = new WeightsManager(Constants.MAX_WEIGHTS, Constants.MAX_WEIGHTS);
    private ActivationFunction activationFunction = new ActivationFunctionSigmoid();

    public void resetWeights() {
        weightsManager.resetWeights();
    }

    public void saveWeights() {
        weightsManager.saveWeights();
    }

    public void loadWeights() {
        weightsManager.loadWeights();
    }

    public NeuralNetwork() {
        AtsLogger.log("Neural Network created");
        //Circular list handles first insertion for getting i and i-1 for every element
        weightsManager.addWeights(new Weights(Constants.MAX_WEIGHTS, Constants.MAX_WEIGHTS));
    }

    public NeuralNetwork(int inputCount, int hiddenCount, int outputCount) {

        this();

        Layer inputLayer = new Layer();
        Layer hiddenLayer = new Layer();
        Layer outputLayer = new Layer();

        for(int i=0; i<inputCount; i++) {
            INeuron neuron = new InputNeuron();
            inputLayer.addNeuron(neuron);
        }

        for(int i=0; i<hiddenCount; i++) {
            INeuron neuron = new HiddenNeuron();
            hiddenLayer.addNeuron(neuron);
        }

        for(int i=0; i<=outputCount; i++) {
            INeuron neuron = new OutputNeuron();
            outputLayer.addNeuron(neuron);
        }

        addLayer(inputLayer);
        addLayer(hiddenLayer);
        addLayer(outputLayer);
    }

    public void train(double[] inputValues, double trueValue, double learningRate, double momentum) {

        double[] net = new double[1024];
        double[] signet = new double[1024];
        double[] input = new double[1024];
        double[] deltas = new double[1024];
        double out;

        int inputCount = getLayers().get(0).getCount();
        int hiddenCount = getLayers().get(1).getCount();

        System.arraycopy(inputValues, 0, input, 0, inputCount);

        for(int i=0; i<hiddenCount; i++) {
            net[i] = 1 * weightsManager.getCurrentWeights().getWeight(0,i);
            for(int j=0; j<inputCount; j++) {
                net[i] += input[j] * weightsManager.getCurrentWeights().getWeight(j+1,i);// j+1 car bias
            }

        }

        for(int i=0; i<hiddenCount; i++) {
            signet[i] = activationFunction.calculate(net[i]);
        }

        out  = 1 * weightsManager.getCurrentWeights().getWeight(0, hiddenCount);

        for(int i=0; i<hiddenCount; i++) {
            out += signet[i] * weightsManager.getCurrentWeights().getWeight(i+1, hiddenCount);//i+1 a cause du bias
        }

        out = activationFunction.calculate(out);

        deltas[hiddenCount] = out*(1-out)*(trueValue-out);

        for(int i=0; i<hiddenCount; i++) {
            deltas[i] = signet[i] * (1-signet[i]) * weightsManager.getCurrentWeights().getWeight(i+1, hiddenCount) * deltas[hiddenCount];
        }

        //Update before learning
        weightsManager.addWeights(weightsManager.getCurrentWeights());

        for(int i=0 ; i<=hiddenCount; i++) {
            //i ranges between 0 and hiddenNeuronsNumber
            double value;
            if(i == hiddenCount) {

                value = weightsManager.getCurrentWeights().getWeight(0, hiddenCount) + (1-momentum) * learningRate * 1 * deltas[hiddenCount] + momentum * (weightsManager.getCurrentWeights().getWeight(0, hiddenCount) - weightsManager.getPreviousLastWeights().getWeight(0, hiddenCount));
                weightsManager.getCurrentWeights().setWeight(0, hiddenCount, value);

                for(int j=0; j <hiddenCount; j++) {
                    value = weightsManager.getCurrentWeights().getWeight(j+1,hiddenCount) + (1-momentum) * learningRate * signet[j] * deltas[hiddenCount] + momentum * (weightsManager.getCurrentWeights().getWeight(j+1,hiddenCount) - weightsManager.getPreviousLastWeights().getWeight(j+1, hiddenCount));
                    weightsManager.getCurrentWeights().setWeight(j+1, hiddenCount, value);
                }
            }
            else {

                value = weightsManager.getCurrentWeights().getWeight(0,i) + (1-momentum) * learningRate * 1 * deltas[i] + momentum * (weightsManager.getCurrentWeights().getWeight(0,i) - weightsManager.getPreviousLastWeights().getWeight(0, i));
                weightsManager.setWeights(0, i, value);

                for(int k = 0; k < inputCount; k++)
                {
                    value = weightsManager.getCurrentWeights().getWeight(k+1,i) + (1-momentum) * learningRate * input[k] * deltas[i] + momentum * (weightsManager.getCurrentWeights().getWeight(k+1,i) -  weightsManager.getPreviousLastWeights().getWeight(k+1,i));
                    weightsManager.setWeights(k+1, i, value);
                }
            }
        }

    }

    public double run(double[] inputValues) {

        double[] net = new double[1024];
        double[] signet = new double[1024];
        double[] input = new double[1024];
        double out;

        int inputCount = getLayers().get(0).getCount();
        int hiddenCount = getLayers().get(1).getCount();

        System.arraycopy(inputValues, 0, input, 0, inputCount);

        for(int i=0; i<hiddenCount; i++) {
            net[i] = 1 * weightsManager.getCurrentWeights().getWeight(0,i);
            for(int j=0; j<inputCount; j++) {
                net[i] += input[j] * weightsManager.getCurrentWeights().getWeight(j+1,i);   // j+1 car bias
            }

        }

        for(int i=0; i<hiddenCount; i++) {
            signet[i] = activationFunction.calculate(net[i]);
        }

        out  = 1 * weightsManager.getCurrentWeights().getWeight(0, hiddenCount);

        for(int i=0; i<hiddenCount; i++) {
            out += signet[i] * weightsManager.getCurrentWeights().getWeight(i+1, hiddenCount); //i+1 a cause du bias
        }

        out = activationFunction.calculate(out);

        return out;
    }

    public NeuralNetwork createInputLayer(int inputCount) {

        Layer inputLayer = new Layer();

        for(int i=0; i<inputCount; i++) {
            INeuron neuron = new InputNeuron();
            inputLayer.addNeuron(neuron);
        }

        addLayer(inputLayer);

        return this;
    }

    public NeuralNetwork createHiddenLayer(int hiddenCount) {

        Layer hiddenLayer = new Layer();

        for(int i=0; i<hiddenCount; i++) {
            INeuron neuron = new HiddenNeuron();
            hiddenLayer.addNeuron(neuron);
        }

        addLayer(hiddenLayer);

        return this;
    }

    public NeuralNetwork createOutputLayer(int outputCount) {

        Layer outputLayer = new Layer();

        for(int i=0; i<outputCount; i++) {
            INeuron neuron = new OutputNeuron();
            outputLayer.addNeuron(neuron);
        }

        addLayer(outputLayer);

        return this;
    }

    public Layer getInputLayer() {
        return layers.get(0);
    }

    public Layer getHiddenLayer() {
        return layers.get(1);
    }

    public Layer getOutputLayer() {
        return layers.get(2);
    }

    public void addLayer(Layer layer) {
        layers.add(layer);
        AtsLogger.log("layer added");
    }

    public ArrayList<Layer> getLayers() {
        return layers;
    }

    public NeuralNetwork addActivationFunction(ActivationFunction activationfunction) {
        this.activationFunction = activationfunction;
        return this;
    }


}