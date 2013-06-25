#include "../include/NeuralNetworkExecution.h"

NeuralNetworkExecution::NeuralNetworkExecution(double learningRate, double momentum,
int epochs, int percentage, CSVParser& csvparser, int deltaTime, vector<int> desiredInputs,
int mainColumnId, NeuralNetwork& neuralnetwork, double mainColumnIdMaxValue)
{
    _csvparser = csvparser;
    _neuralnetwork = neuralnetwork;
    _learningRate = learningRate;
    _momentum = momentum;
    _epochs = epochs;

    if(percentage >= 100 || percentage <= 0) {
        cout << "Invalid percentage" << endl;
        exit(0);
    }

    _delimiter = (_csvparser.countLines() * percentage) / 100;
    _percentage = percentage;
    _deltaTime = deltaTime;
    _desiredInputs = desiredInputs;
    _mainColumnId = mainColumnId;
    _mainColumnIdMaxValue = mainColumnIdMaxValue;
    debug();
}

void NeuralNetworkExecution::debug()
{
    cout << "Call to NeuralNetworkExecution::debug()" << endl;
    cout << "Delimiter : " << _delimiter << endl;
    cout << "Main column id : " << _mainColumnId << endl;
    cout << "Delta time : " << _deltaTime << endl;
    cout << "Learning rate : " << _learningRate << endl;
    cout << "Momentum : " << _momentum << endl;
    cout << "Epochs : " << _epochs << endl;
    cout << "Percentage : " << _percentage << endl;
    cout << "Main column id max value : " << _mainColumnIdMaxValue << endl;
    Utils::displayVectorInt(_desiredInputs);
}

NeuralNetworkExecution::~NeuralNetworkExecution()
{
}

void NeuralNetworkExecution::exec()
{
    cout << "Call to NeuralNetworkExecution::exec()" << endl;

    double error = 0.0;
    double errorMin = (double) Utils::INT_MAX;
    int epochsIt = 0;
    int bestEpoch = 0;

    while(epochsIt < _epochs)
    {
        _neuralnetwork.trainProcess(_delimiter, _desiredInputs, _csvparser, _mainColumnId, _deltaTime, _learningRate, _momentum);
        error = _neuralnetwork.error(_delimiter, _csvparser, _deltaTime, _desiredInputs, _mainColumnId, _mainColumnIdMaxValue);

        if(error < errorMin) {
            errorMin = error;
            bestEpoch = epochsIt;
            _neuralnetwork.saveWeights();
        }

        if(epochsIt % 100 == 0)
            cout << "error : " << error << " EUR - error min : " << errorMin << " EUR at epoch : " << bestEpoch << " epochs : " << epochsIt << " passes : " << _neuralnetwork.getCounter() <<endl;

        epochsIt++;
    }

    _neuralnetwork.loadSavedWeights();
    double predictedPrice = _neuralnetwork.runningProcess( _csvparser.countLines()-1, _desiredInputs, _csvparser, _mainColumnId, _mainColumnIdMaxValue);

    ///Last line = csvparser.countLines() - 1
    int end = _csvparser.countLines()-1-_deltaTime;
    double bin_error = 0;
    double success_count = 0;
    double failure_count = 0;

    for(int i=_delimiter; i<=end; i++) {
        double computedValue = _neuralnetwork.runningProcess(i, _desiredInputs, _csvparser, _mainColumnId, _mainColumnIdMaxValue);
        double trueValue = _csvparser.getValue(i+_deltaTime, _mainColumnId);

        if(computedValue>0.5)
            computedValue = 1;
        else
            computedValue = 0;

        bin_error += fabs(computedValue-trueValue);

        if(fabs(computedValue-trueValue) == 0)
            success_count++;
        else if(fabs(computedValue-trueValue) == 1)
            failure_count++;

        cout << "line = " << i << " computed value = " << computedValue << " true value = " << trueValue << endl;
    }

    double real_error = bin_error/= (end-_delimiter);
    cout << "success count = " << success_count << endl;
    cout << "failure count = " << failure_count << endl;
    cout << "real error price : " << real_error << endl;
    cout << "FUTURE PRICE : " << _neuralnetwork.runningProcess(_csvparser.countLines()-1, _desiredInputs, _csvparser, _mainColumnId, _mainColumnIdMaxValue) << endl;
    cout << "last csv line = " << _csvparser.debugLine(_csvparser.countLines()-1) << endl;
    cout << "Error min (at epoch : " << bestEpoch << ") : " << errorMin << endl;
    cout << "Future price (delta : " << _deltaTime << ") : " << predictedPrice << endl;
    exit(0);
}

double NeuralNetworkExecution::execGetValue()
{
}

double NeuralNetworkExecution::execGetValueWithLowestError()
{
}

NeuralNetworkExecution NeuralNetworkExecution::setLearningRate(double learningRate)
{
    _learningRate = learningRate;
    return *this;
}

NeuralNetworkExecution NeuralNetworkExecution::setMomentum(double momentum)
{
    _momentum = momentum;
    return *this;
}

NeuralNetworkExecution NeuralNetworkExecution::setEpochs(int epochs)
{
    _epochs = epochs;
    return *this;
}

NeuralNetworkExecution NeuralNetworkExecution::setDelimiter(int percentage)
{
    if(percentage >= 100 || percentage <= 0)
    {
        cout << "Invalid percentage" << endl;
        exit(0);
    }

    _delimiter = (_csvparser.countLines() * percentage) / 100;
    _percentage = percentage;

    return *this;
}

NeuralNetworkExecution NeuralNetworkExecution::setParser(CSVParser& csvparser)
{
    _csvparser = csvparser;
    return *this;
}

NeuralNetworkExecution NeuralNetworkExecution::setDeltaTime(int deltaTime)
{
    _deltaTime = deltaTime;
    return *this;
}

NeuralNetworkExecution NeuralNetworkExecution::setDesiredInputs(vector<int> desiredInputs)
{
    _desiredInputs = desiredInputs;
    return *this;
}

NeuralNetworkExecution NeuralNetworkExecution::setMainColumnId(int mainColumnId)
{
    _mainColumnId = mainColumnId;
    return *this;
}

NeuralNetworkExecution NeuralNetworkExecution::setNeuralNetwork(NeuralNetwork& neuralnetwork)
{
    _neuralnetwork = neuralnetwork;
    return *this;
}

NeuralNetworkExecution NeuralNetworkExecution::setMaximumOfMainColumnId(double mainColumnIdMaxValue)
{
    _mainColumnIdMaxValue = mainColumnIdMaxValue;
    return *this;
}

CSVParser& NeuralNetworkExecution::getParser()
{
    return _csvparser;
}

int NeuralNetworkExecution::getPercentage()
{
    return _percentage;
}

NeuralNetwork& NeuralNetworkExecution::getNeuralNetwork()
{
    return _neuralnetwork;
}

int NeuralNetworkExecution::getDeltaTime()
{
    return _deltaTime;
}

int NeuralNetworkExecution::getMainColumnId()
{
    return _mainColumnId;
}

double NeuralNetworkExecution::getLearningRate() { return _learningRate; }
double NeuralNetworkExecution::getMomentum() { return _momentum; }
int NeuralNetworkExecution::getDelimiter() { return _delimiter; }
vector<int> NeuralNetworkExecution::getDesiredInputs() { return _desiredInputs; }
double NeuralNetworkExecution::getMainColumndIdMaxValue() { return _mainColumnIdMaxValue; }
