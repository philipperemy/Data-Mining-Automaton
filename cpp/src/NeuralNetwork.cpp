#include "../include/NeuralNetwork.h"
#include <iostream>
#include <fstream>
#include <limits.h>
#include <math.h>
#include <windows.h>

#define WRITE_INTO_FILE 0
#define WRITE_INTO_STDOUT 1

#define IN_PROGRESS 0
#define TERMINATED 1

using namespace std;

NeuralNetwork::NeuralNetwork()
{
    init();
}

void NeuralNetwork::init()
{
    _counter = 0;
    _layers.reserve(10);
    _layerCount = 0;

    for(int i=0;i<Utils::MAX_WEIGHTS;i++)
        for (int j=0;j<Utils::MAX_WEIGHTS;j++)
            weights[i][j] = (double) (rand() / (RAND_MAX + 1.0) - 0.5);

}

void NeuralNetwork::saveWeights()
{
    for(int i=0;i<Utils::MAX_WEIGHTS;i++)
        for (int j=0;j<Utils::MAX_WEIGHTS;j++)
            _savedWeights[i][j] = weights[i][j];
}

void NeuralNetwork::loadSavedWeights()
{
    for(int i=0;i<Utils::MAX_WEIGHTS;i++)
        for (int j=0;j<Utils::MAX_WEIGHTS;j++)
            weights[i][j] = _savedWeights[i][j];
}

NeuralNetwork::NeuralNetwork(int inputCount, int hiddenCount, int outputCount)
{
    init();
    createInputLayer(inputCount);
    createHiddenLayer(hiddenCount);
    createOutputLayer(outputCount);

    _weightsContainer = new WeightsContainer(hiddenCount+1,inputCount+1);
    _weightsContainer->addWeights(weights);

    cout << "Neural Network created" << endl;
}

void NeuralNetwork::debugWeights()
{
    string debugNormalizedHandler = "debug/weights.DEBUG";
    ofstream fileHandler(debugNormalizedHandler.c_str(), ios::out | ios::trunc);

    for(int i=0;i<Utils::MAX_WEIGHTS;i++) {

        for (int j=0;j<Utils::MAX_WEIGHTS;j++) {
            weights[i][j] = (double) (rand() / (RAND_MAX + 1.0) - 0.5);
            fileHandler << weights[i][j] << ";";
        }

        fileHandler << endl;
    }

}

void NeuralNetwork::debug()
{
    cout << "Call to NeuralNetwork()::debug()" << endl;
    cout << "inputCount : " << getLayers().at(0).getCount() << endl;
    cout << "hiddenCount : " << getLayers().at(1).getCount() << endl;
    cout << "outputCount : " << getLayers().at(2).getCount() << endl;
    cout << "Number of layers : " << _layerCount << endl;
    cout << "Counter : " << _counter << endl;

    if(_counter % 100 == 0)
        debugWeights();
}


void NeuralNetwork::debug(CSVParser csvparser)
{
    debug();

    if(_counter % 100 == 0)
        csvparser.debug();

}

void NeuralNetwork::resetWeights()
{
    cout << "Reset weights in progress" << endl;

    _weightsContainer->reset();

    for(int i=0;i<Utils::MAX_WEIGHTS;i++)
        for (int j=0;j<Utils::MAX_WEIGHTS;j++)
            weights[i][j] = (double) (rand() / (RAND_MAX + 1.0) - 0.5);

    _weightsContainer->addWeights(weights);
}

int NeuralNetwork::getLayerCount()
{
    return _layerCount;
}

double NeuralNetwork::train(double* inputValues, double trueValue, double learningRate, double momentum)
{
    double net[1024];
    double signet[1024];
    double input[1024];
    double deltas[1024];
    double out;

    int inputCount = getLayers().at(0).getCount();
    int hiddenCount = getLayers().at(1).getCount();

    for(int inputIt = 0; inputIt < inputCount; inputIt++)
    {
        input[inputIt] = inputValues[inputIt];
    }

    for(int i = 0; i < hiddenCount; i++)
    {
        net[i] = 1 * weights[0][i];

        for(int j = 0; j < inputCount; j++)

            net[i] += input[j] * weights[j+1][i];   // j+1 car bias
    }


    for(int i = 0; i < hiddenCount; i++)

        signet[i] = Utils::sigmoid(net[i]);


    out = 1 * weights[0][hiddenCount];

    for(int i = 0; i < hiddenCount; i++)

        out += signet[i] * weights[i+1][hiddenCount]; //i+1 a cause du bias

    out = Utils::sigmoid(out);

    //DELTAS

    deltas[hiddenCount] = out*(1-out)*(trueValue-out);

    for(int i = 0; i < hiddenCount; i++)
    {
        deltas[i] = signet[i] * (1-signet[i]) * weights[i+1][hiddenCount] * deltas[hiddenCount];
    }

    //UPDATE BEFORE LEARNING -- ADDWEIGHT()
    _weightsContainer->addWeights(weights);

    //LEARNING PROCESS

    for(int i = 0 ; i <= hiddenCount ; i++) {

    //i ranges between 0 and hiddenCount

        if(i == hiddenCount)
        {

            //weights[0][hiddenCount] += learningRate * 1 * deltas[hiddenCount];
            weights[0][hiddenCount] = weights[0][hiddenCount] + (1-momentum) * learningRate * 1 * deltas[hiddenCount] + momentum * (weights[0][hiddenCount] - _weightsContainer->getLastWeightsValue(0, hiddenCount));

            for(int j = 0; j < hiddenCount; j++)
            {
                //weights[j+1][hiddenCount] += learningRate * signet[j] * deltas[hiddenCount];
                weights[j+1][hiddenCount] = weights[j+1][hiddenCount] + (1-momentum) * learningRate * signet[j] * deltas[hiddenCount] + momentum * (weights[j+1][hiddenCount] - _weightsContainer->getLastWeightsValue(j+1, hiddenCount));
            }

        }
        else
        {

            //weights[0][i] += learningRate * 1 * deltas[i];
            weights[0][i] = weights[0][i] + (1-momentum) * learningRate * 1 * deltas[i] + momentum * (weights[0][i] - _weightsContainer->getLastWeightsValue(0,i));

            for(int k = 0; k < inputCount; k++)
            {
                //weights[k+1][i] += learningRate * input[k] * deltas[i]; // k+1 a cause du bias
                weights[k+1][i] = weights[k+1][i] + (1-momentum) * learningRate * input[k] * deltas[i] + momentum * (weights[k+1][i] -  _weightsContainer->getLastWeightsValue(k+1, i));
            }

        }

    }

    return out;
}

double NeuralNetwork::run(double* inputValues)
{
    double* net = new double[1024];
    double* signet = new double[1024];
    double* input = new double[1024];
    double out;

    int inputCount = getLayers().at(0).getCount();
    int hiddenCount = getLayers().at(1).getCount();

    for(int i=0; i<inputCount; i++) {
        input[i] = inputValues[i];
    }

    for(int i=0; i<hiddenCount; i++) {
        net[i] = 1 * weights[0][i];
        for(int j=0; j<inputCount; j++) {
            net[i] += input[j] * weights[j+1][i];   // j+1 car bias
        }

    }

    for(int i=0; i<hiddenCount; i++) {
        signet[i] = Utils::sigmoid(net[i]);
    }

    out  = 1 * weights[0][hiddenCount];

    for(int i=0; i<hiddenCount; i++) {
        out += signet[i] * weights[i+1][hiddenCount]; //i+1 a cause du bias
    }

    out = Utils::sigmoid(out);

    delete(net);
    delete(signet);
    delete(input);

    return out;
}

NeuralNetwork::~NeuralNetwork()
{
}

double NeuralNetwork::error(int delimiter, CSVParser& csvparser, int deltaTime, vector<int> desiredInputs, int mainColumnId, double mainColumnIdMaxValue)
{
    int end = csvparser.countLines()-deltaTime;
    double input[1024];
    int desiredInputsCountSize = desiredInputs.size();
    int tmpVar;
    double runningValue = 0.0, trueValue = 0.0, errorRate = 0.0;
    int inputIteratorAll = 0;

    for(int i=delimiter; i<end; i++) {

        for(int j=0; j < desiredInputsCountSize; j++) {
            tmpVar = desiredInputs.at(j)-1;

            for(int inputIterator=0; inputIterator<desiredInputs.at(j); inputIterator++) {

                if(csvparser.isNormalized())
                    input[inputIteratorAll] = csvparser.getNormalizedValue(i-tmpVar, j);
                else {
                    cout << "errorProcess() - ERROR : isNormalized() = false" << endl;
                    exit(0);
                }

                tmpVar--;
                inputIteratorAll++;
            }
        }

        runningValue = run(input)*mainColumnIdMaxValue;

        if(csvparser.isFilled())
            trueValue = csvparser.getValue(i+deltaTime, mainColumnId);
        else {
            cout << "errorProcess() - ERROR : isFilled() = false" << endl;
            exit(0);
        }

        errorRate += fabs(trueValue-runningValue);
        inputIteratorAll = 0;
    }

    errorRate /= (end-delimiter);
    return errorRate;
}

double NeuralNetwork::runningProcess(int delimiter, vector<int> desiredInputs, CSVParser& csvparser, int mainColumnId, double mainColumnIdMaxValue)
{
    double input[1024];
    int tmpVar;
    double runningValue = 0;
    int inputIteratorAll = 0;
    int desiredInputsCountSize = desiredInputs.size();

    for(int j = 0; j < desiredInputsCountSize; j++) {
        tmpVar = desiredInputs.at(j)-1;

        for(int inputIterator = 0; inputIterator < desiredInputs.at(j); inputIterator++) {

            if(csvparser.isNormalized())
                input[inputIteratorAll] = csvparser.getNormalizedValue(delimiter-tmpVar, j);
            else {
                cout << "runningProcess() - ERROR : isNormalized() = false" << endl;
                exit(0);
            }

            tmpVar--;
            inputIteratorAll++;
        }
    }

    runningValue = run(input)*mainColumnIdMaxValue;
    inputIteratorAll = 0;

    return runningValue;
}

NeuralNetwork* NeuralNetwork::createInputLayer(int inputCount)
{
    Layer inputLayer;

    for(int i=0; i<inputCount; i++) {
        InputNeuron neuron;
        inputLayer.addNeuron(neuron);
    }

    addLayer(inputLayer);

    return this;
}

NeuralNetwork* NeuralNetwork::createHiddenLayer(int hiddenCount)
{
    Layer hiddenLayer;

    for(int i=0; i<hiddenCount; i++) {
        HiddenNeuron neuron;
        hiddenLayer.addNeuron(neuron);
    }

    addLayer(hiddenLayer);

    return this;
}

NeuralNetwork* NeuralNetwork::createOutputLayer(int outputCount)
{
    Layer outputLayer;

    for(int i=0; i<outputCount; i++) {
        OutputNeuron neuron;
        outputLayer.addNeuron(neuron);
    }

    addLayer(outputLayer);

    return this;
}

void NeuralNetwork::addLayer(Layer layer)
{
    _layers.push_back(layer);
    _layerCount++;
    cout << "Layer added" << endl;
}

vector<Layer> NeuralNetwork::getLayers()
{
    return _layers;
}

long long NeuralNetwork::getCounter()
{
    return _counter;
}

void NeuralNetwork::trainProcess(int delimiter, vector<int> desiredInputs, CSVParser& csvparser, int mainColumnId, int deltaTime, double learningRate, double momentum)
{
    double input[1024];
    int tmpVar;
    int inputIteratorAll = 0;
    int maxDesiredInputs = 0;
    int desiredInputsCountSize = desiredInputs.size();

    for(int i = 0; i < desiredInputsCountSize; i++)
        if(desiredInputs.at(i) > maxDesiredInputs)
            maxDesiredInputs = desiredInputs.at(i);

    for(int i = maxDesiredInputs; i < delimiter; i++)
    {
        _counter++;

        for(int j = 0; j < desiredInputsCountSize; j++)
        {
            tmpVar = desiredInputs.at(j)-1;

            for(int inputIterator = 0; inputIterator < desiredInputs.at(j); inputIterator++)
            {
                if(csvparser.isNormalized())
                    input[inputIteratorAll] = csvparser.getNormalizedValue(i-tmpVar, j);
                else {
                    cout << "trainProcess() - ERROR : isNormalized() = false" << endl;
                    exit(0);
                }

                tmpVar--;
                inputIteratorAll++;
            }
        }

        if(csvparser.isFilled())
            train(input, csvparser.getNormalizedValue(i+deltaTime, mainColumnId), learningRate, momentum);
        else {
            cout << "trainProcess() - ERROR : isFilled() = false" << endl;
            exit(0);
        }

        inputIteratorAll = 0;
    }
}

void NeuralNetwork::toString()
{
    cout << "Neural Network" << endl;
}
