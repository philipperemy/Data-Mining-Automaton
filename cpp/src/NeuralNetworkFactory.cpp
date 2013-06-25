#include "../include/NeuralNetworkFactory.h"

NeuralNetwork* NeuralNetworkFactory::createInstance()
{
    NeuralNetwork* neuralnetwork = new NeuralNetwork();
    return neuralnetwork;
}
