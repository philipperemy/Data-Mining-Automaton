#include "../include/WeightsContainer.h"

WeightsContainer::WeightsContainer()
{
    cout << "WeightsContainer::WeightsContainer()" << endl;
    _weightsCurrentId = 0;
    _xmax = Utils::MAX_WEIGHTS;
    _ymax = Utils::MAX_WEIGHTS;
}

WeightsContainer::WeightsContainer(int xmax, int ymax)
{
    cout << "WeightsContainer::WeightsContainer()" << endl;
    _weightsCurrentId = 0;
    _xmax = xmax;
    _ymax = ymax;
}

void WeightsContainer::setXmax(int xmax)
{
    _xmax = xmax;
}

void WeightsContainer::setYmax(int ymax)
{
    _ymax = ymax;
}

WeightsContainer::~WeightsContainer()
{
    cout << "WeightsContainer::~WeightsContainer()" << endl;
}

int WeightsContainer::addWeights(double weights[Utils::MAX_WEIGHTS][Utils::MAX_WEIGHTS])
{
    if(_weightsCurrentId >= (ARRAY_SIZE-1))
        _weightsCurrentId = 0;
    else
        _weightsCurrentId++;

    for(int i=0; i<_ymax; i++)
        for(int j=0; j<_xmax; j++)
            _weightsArray[_weightsCurrentId][i][j] = weights[i][j];

    return _weightsCurrentId;
}

//TODO a regarder sur internet pour l'initialisation
double** WeightsContainer::getLastWeights()
{
    int lastId = getLastCurrentId();
    return getWeights(lastId);
}

//NOT WORKING
double** WeightsContainer::getWeights(int id)
{
    double weights[Utils::MAX_WEIGHTS][Utils::MAX_WEIGHTS];

    for(int i=0; i<_ymax; i++)
        for(int j=0; j<_xmax; j++)
            weights[i][j] = _weightsArray[id][i][j];

    return (double**) weights;

}

int WeightsContainer::getLastInsertedId()
{
    return _weightsCurrentId;
}

double WeightsContainer::getWeightsValue(int id, int x, int y)
{
    return _weightsArray[id][x][y];
}

double WeightsContainer::getLastWeightsValue(int x, int y)
{
    int lastId = getLastCurrentId();
    return _weightsArray[lastId][x][y];
}

int WeightsContainer::getLastCurrentId()
{
    if(_weightsCurrentId == 0)
        return (ARRAY_SIZE-1);
    else
        return(_weightsCurrentId - 1);
}

void WeightsContainer::reset()
{
    _weightsCurrentId = 0;
}

void WeightsContainer::bluePrintWeights(int id)
{
    string debugHandler = "weightsPrint_" + intToString(id) + ".DEBUG";
    ofstream fileHandler(debugHandler.c_str(), ios::out | ios::trunc);

    for(int i=0; i<_ymax; i++) {
        for(int j=0; j<_xmax; j++)
            fileHandler << getWeightsValue(id,i,j) << ";";
        fileHandler << endl;
    }

    fileHandler.close();
}

string WeightsContainer::intToString(int integer)
{
    ostringstream oss;
    oss << integer;
    return oss.str();
}
