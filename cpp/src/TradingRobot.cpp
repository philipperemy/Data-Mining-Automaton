#include "../include/TradingRobot.h"
#include "../Header.h"

#define EXEC_RETURN 0
#define EXEC_NO_RETURN 1

TradingRobot::TradingRobot()
{
    //ctor
}

TradingRobot::TradingRobot(string filename, vector<int> desiredInputs, int hiddenCount,
                           int deltaTime, int mainColumnId, int percentage, int epochs,
                           double learningRate, double momentum)
{
    _filename = filename;
    _desiredInputs = desiredInputs;
    _hiddenCount = hiddenCount;
    _deltaTime = deltaTime;
    _mainColumnId = mainColumnId;
    _percentage = percentage;
    _epochs = epochs;
    _learningRate = learningRate;
    _momentum = momentum;
}

TradingRobot::~TradingRobot()
{
}

void TradingRobot::onChange()
{
    cout << endl << "Call to TradingRobot::onChange()" << endl;

    debug();

    CSVParser csvparser(_filename);
    csvparser.normalize();
    csvparser.debug();

    int inputCount = 0;

    for(int i=0; i<_desiredInputs.size(); i++)
        inputCount += _desiredInputs.at(i);

    cout << "inputCount = " << inputCount << endl;

    NeuralNetwork neuralnetwork(inputCount, _hiddenCount, 1);

    NeuralNetworkExecution * neuralnetworkexecution = new NeuralNetworkExecution(_learningRate, _momentum, _epochs, _percentage, csvparser, _deltaTime, _desiredInputs, _mainColumnId, neuralnetwork, csvparser.getMaxValueFromColumn(_mainColumnId));

    if(EXEC_RETURN) {

        double predictedPrice = neuralnetworkexecution->execGetValueWithLowestError();
        double lastPrice = neuralnetworkexecution->getParser().getValue(neuralnetworkexecution->getParser().countLines()-1, neuralnetworkexecution->getMainColumnId());

        cout << " ... [OK]" << endl;
        cout << "Last = " << lastPrice << endl;
        cout << "Predicted = " << predictedPrice << endl;

    }
    else if(EXEC_NO_RETURN)
        neuralnetworkexecution->exec();

    delete(neuralnetworkexecution);
}

void TradingRobot::run()
{
    cout << endl << "Call to TradingRobot::run();" << endl;

    FileMonitorRobot task(_filename, *this);
    cout << "Waiting for file to be modified..." << endl;

    while(true) {
        cout << "..." << endl;
        task.run();
        Sleep(2000);
    }
}


void TradingRobot::debug()
{
    cout << "Call to TradingRobot::debug()" << endl;
    cout << "Percentage : " << _percentage << endl;
    cout << "Main column id : " << _mainColumnId << endl;
    cout << "Delta time : " << _deltaTime << endl;
    cout << "Learning rate : " << _learningRate << endl;
    cout << "Momentum : " << _momentum << endl;
    cout << "Filename : " << _filename << endl;
    cout << "Epochs : " << _epochs << endl;
    cout << "Hidden Count : " << _hiddenCount << endl;
    cout << "Desired Inputs : ";
    for(int i = 0; i < _desiredInputs.size(); i++)
        cout << _desiredInputs[i] << ";";
}

NeuralNetworkExecution TradingRobot::createExecution() {

    cout << "Call to TradingRobot::createExecution()" << endl;

    CSVParser csvparser(_filename);
    csvparser.debug();

    int inputCount = 0;
    for(int i=0; i<_desiredInputs.size(); i++)
        inputCount += _desiredInputs.at(i);

    NeuralNetwork neuralnetwork(inputCount, _hiddenCount, 1);

    NeuralNetworkExecution neuralnetworkexecution(_learningRate, _momentum, _epochs, _percentage, csvparser, _deltaTime, _desiredInputs, _mainColumnId, neuralnetwork, csvparser.getMaxValueFromColumn(_mainColumnId));

    return neuralnetworkexecution;
}

TradingRobot* TradingRobot::setFilename(string filename)
{
    _filename = filename;
    return this;
}

TradingRobot* TradingRobot::setDesiredInputs(vector<int> desiredInputs)
{
    _desiredInputs = desiredInputs;
    return this;
}

TradingRobot* TradingRobot::setHiddenCount(int hiddenCount)
{
    _hiddenCount = hiddenCount;
    return this;
}

TradingRobot* TradingRobot::setDeltaTime(int deltaTime)
{
    _deltaTime = deltaTime;
    return this;
}

TradingRobot* TradingRobot::setMainColumnId(int mainColumnId)
{
    _mainColumnId = mainColumnId;
    return this;
}

TradingRobot* TradingRobot::setPercentage(int percentage)
{
    _percentage = percentage;
    return this;
}

TradingRobot* TradingRobot::setEpochs(int epochs)
{
    _epochs = epochs;
    return this;
}

TradingRobot* TradingRobot::setLearningRate(double learningRate)
{
    _learningRate = learningRate;
    return this;
}

TradingRobot* TradingRobot::setMomentum(double momentum)
{
    _momentum = momentum;
    return this;
}
