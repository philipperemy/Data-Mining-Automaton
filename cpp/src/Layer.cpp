#include "../include/Layer.h"
#include <stdlib.h>

using namespace std;

Layer::Layer()
{
    _count = 0;
    _neurons.reserve(20);
    cout << "Layer created" << endl;
}

Layer::~Layer()
{
}

int Layer::addNeuron(Neuron neuron)
{
    _neurons.push_back(neuron);
    cout << "neuron added to layer" << endl;
    return _count++;
}

void Layer::removeNeuron(int count)
{
    if(_count > count)
    {
        while(count > 0)
        {
            _neurons.pop_back();
            _count--;
            count--;
        }
    }
    else
    {
        cout << "Invalid count value : " << count << endl;
        exit(0);
    }
}

Neuron Layer::getNeuron(int id)
{
    return _neurons.at(id);
}

void Layer::freeLayer()
{
    return _neurons.clear();
}

int Layer::getCount()
{
    return _count;
}
