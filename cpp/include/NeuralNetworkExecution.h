#ifndef NEURALNETWORKEXECUTION_H
#define NEURALNETWORKEXECUTION_H

#include <iostream>
#include "CSVParser.h"
#include "NeuralNetwork.h"
#include "Utils.h"

using namespace std;

class NeuralNetworkExecution
{
    public:
        NeuralNetworkExecution(double learningRate, double momentum, int epochs, int percentage, CSVParser& csvparser,
                               int deltaTime, vector<int> desiredInputs, int mainColumnId, NeuralNetwork& neuralnetwork,
                               double mainColumnIdMaxValue);
        virtual ~NeuralNetworkExecution();

        void exec();
        double execGetValue();
        double execGetValueWithLowestError();

        NeuralNetworkExecution setLearningRate(double learningRate);
        NeuralNetworkExecution setMomentum(double momentum);
        NeuralNetworkExecution setEpochs(int epochs);
        NeuralNetworkExecution setDelimiter(int percentage);
        NeuralNetworkExecution setParser(CSVParser& csvparser);
        NeuralNetworkExecution setDeltaTime(int deltaTime);
        NeuralNetworkExecution setDesiredInputs(vector<int> desiredInputs);
        NeuralNetworkExecution setMainColumnId(int mainColumnId);
        NeuralNetworkExecution setNeuralNetwork(NeuralNetwork& neuralnetwork);
        NeuralNetworkExecution setMaximumOfMainColumnId(double mainColumnIdMaxValue);

        CSVParser& getParser();
        int getPercentage();
        NeuralNetwork& getNeuralNetwork();
        int getDeltaTime();
        int getMainColumnId();
        double getLearningRate();
        double getMomentum();
        int getDelimiter();
        vector<int> getDesiredInputs();
        double getMainColumndIdMaxValue();
        void debug();

    protected:

    private:
        double _learningRate;
        double _momentum;
        int _epochs;
        int _delimiter;
        int _deltaTime;
        int _percentage;
        vector<int> _desiredInputs;
        int _mainColumnId;
        NeuralNetwork _neuralnetwork;
        CSVParser _csvparser;
        double _mainColumnIdMaxValue;

};

#endif // NEURALNETWORKEXECUTION_H
