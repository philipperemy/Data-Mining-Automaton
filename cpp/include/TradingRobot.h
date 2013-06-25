#ifndef TRADINGROBOT_H
#define TRADINGROBOT_H

#include "../Header.h"

class TradingRobot
{
    public:
        TradingRobot();
        TradingRobot(string filename, vector<int> desiredInputs, int hiddenCount,
                           int deltaTime, int mainColumnId, int percentage, int epochs,
                           double learningRate, double momentum);
        virtual ~TradingRobot();
        virtual void onChange();
        virtual void run();
        virtual NeuralNetworkExecution createExecution();

        TradingRobot* setFilename(string filename);
        TradingRobot* setDesiredInputs(vector<int> desiredInputs);
        TradingRobot* setHiddenCount(int hiddenCount);
        TradingRobot* setDeltaTime(int deltaTime);
        TradingRobot* setMainColumnId(int mainColumnId);
        TradingRobot* setPercentage(int percentage);
        TradingRobot* setEpochs(int epochs);
        TradingRobot* setLearningRate(double learningRate);
        TradingRobot* setMomentum(double momentum);
        void debug();

    protected:

    private:

        string _filename;
        vector<int> _desiredInputs;
        int _hiddenCount;
        int _deltaTime;
        int _mainColumnId;
        int _percentage;
        int _epochs;
        double _learningRate;
        double _momentum;
};

#endif // TRADINGROBOT_H
