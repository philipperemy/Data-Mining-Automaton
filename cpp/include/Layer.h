#include <iostream>
#include <string>
#include <vector>
#include "../include/Neuron.h"

#ifndef LAYER_H
#define LAYER_H

using std::vector;

class Layer
{
    public:
        Layer();
        virtual ~Layer();
        int addNeuron(Neuron neuron);
        void removeNeuron(int count);
        Neuron getNeuron(int id);
        void freeLayer();
        int getCount();

    protected:

    private:
        vector<Neuron> _neurons;
        unsigned int _count;
};

#endif // LAYER_H
