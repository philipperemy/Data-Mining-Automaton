#include "../include/Utils.h"
#include <math.h>
#include <iostream>

double Utils::sigmoid(double value)
{
    return (double)(1/(1+exp(-value)));
}

int Utils::charToInt(char c)
{
    if (c < '0' || c > '9') return -1;
    return c - '0';
}

void Utils::displayVectorInt(vector<int> v)
{
    cout << "Displaying vector : ";
    for(int i=0; i<v.size(); i++) {
        if(i == v.size()-1)
            cout << v.at(i) << endl;
        else
            cout << v.at(i) << SEPARATOR;
    }
}
