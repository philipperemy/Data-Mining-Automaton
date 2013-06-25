#ifndef WEIGHTSCONTAINER_H
#define WEIGHTSCONTAINER_H

#include "Utils.h"

#include <sstream>
#include <iostream>
#include <fstream>

#define ARRAY_SIZE 10

using namespace std;

class WeightsContainer
{
    public:
        WeightsContainer();
        WeightsContainer(int xmax, int ymax);
        virtual ~WeightsContainer();
        int addWeights(double weights[Utils::MAX_WEIGHTS][Utils::MAX_WEIGHTS]);
        double** getLastWeights();
        double** getWeights(int id);
        int getLastInsertedId();
        double getWeightsValue(int id, int x, int y);
        double getLastWeightsValue(int x, int y);
        void bluePrintWeights(int id);
        void reset();
        string intToString(int integer);
        void setXmax(int xmax);
        void setYmax(int ymax);

    protected:

    private:
        int getLastCurrentId();
        double _weightsArray[ARRAY_SIZE][Utils::MAX_WEIGHTS][Utils::MAX_WEIGHTS];
        int _weightsCurrentId;
        int _ymax;
        int _xmax;
};

#endif // WEIGHTSCONTAINER_H
