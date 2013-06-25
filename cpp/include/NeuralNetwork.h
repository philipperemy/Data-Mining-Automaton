#include <math.h>
#include <time.h>
#include <stdlib.h>
#include "CSVParser.h"
#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>
#include <numeric>
#include <sstream>
#include <string>
#include <stdio.h>
#include <string.h>

#ifndef NEURAL_NETWORK
#define NEURAL_NETWORK

#include <iostream>
#include "Layer.h"
#include "Neuron.h"
#include "InputNeuron.h"
#include "HiddenNeuron.h"
#include "OutputNeuron.h"
#include "Utils.h"
#include "WeightsContainer.h"

class NeuralNetwork
{
    public:
        NeuralNetwork();
        NeuralNetwork(int inputCount, int hiddenCount, int outputCount);
        double run(double* inputValues);
        void trainProcess(int delimiter, vector<int> desiredInputs, CSVParser& csvparser, int mainColumnId, int deltaTime, double learningRate, double momentum);
        double runningProcess(int delimiter, vector<int> desiredInputs, CSVParser& csvparser, int mainColumnId, double mainColumnIdMaxValue);
        double train(double* inputValues, double trueValue, double learningRate, double momentum);
        void init();

        NeuralNetwork* createInputLayer(int inputCount);
        NeuralNetwork* createHiddenLayer(int hiddenCount);
        NeuralNetwork* createOutputLayer(int outputCount);

        virtual ~NeuralNetwork();
        void addLayer(Layer layer);
        vector<Layer> getLayers();
        double error(int delimiter, CSVParser& csvparser, int deltaTime, vector<int> desiredInputs, int mainColumnId, double mainColumnIdMaxValue);
        int getLayerCount();
        void resetWeights();
        //void addWeights(double** weightsAdd);
        void toString();
        void debugWeights();
        void debug();
        void debug(CSVParser csvparser);
        long long getCounter();
        void saveWeights();
        void loadSavedWeights();

    protected:

    private:
        vector<Layer> _layers;
        int _layerCount;
        double weights[Utils::MAX_WEIGHTS][Utils::MAX_WEIGHTS];
        double _savedWeights[Utils::MAX_WEIGHTS][Utils::MAX_WEIGHTS];
        int _weightsListCurrent;
        vector<double[Utils::MAX_WEIGHTS][Utils::MAX_WEIGHTS]> _weightsList;
        long long _counter;
        WeightsContainer * _weightsContainer;
};


#endif

