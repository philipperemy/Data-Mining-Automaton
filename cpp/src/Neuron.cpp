#include "../include/Neuron.h"
#include <iostream>

using namespace std;

Neuron::Neuron() : _value(0)
{
}

Neuron::~Neuron()
{
}

double Neuron::getValue()
{
    return _value;
}

void Neuron::setValue(double value)
{
    _value = value;
}
