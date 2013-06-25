#ifndef NEURON_H
#define NEURON_H


class Neuron
{
    public:
        virtual ~Neuron();
        void setValue(double value);
        double getValue();
        Neuron();

    protected:
        double _value;

    private:

};

#endif // NEURON_H
