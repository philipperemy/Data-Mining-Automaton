#include "../include/Trade.h"
#include <iostream>
#include <stdlib.h>

using namespace std;

Trade::Trade(string ref, double priceIn, double pricePredicted, unsigned int volume, string position)
: _ref(ref), _priceIn(priceIn), _pricePredicted(pricePredicted), _volume(volume), _position(position)
{
    setClosed(false);
}

Trade::~Trade()
{

}

void Trade::setPosition(string position)
{
    if(position == "BUY" || position == "SELL")
        _position = position;
    else
    {
        cout << "Invalid position" << endl;
        exit(0);
    }
}

string Trade::getPosition()
{
    return _position;
}

string Trade::getRef()
{
    return _ref;
}

void Trade::setRef(string ref)
{
    _ref = ref;
}

double Trade::getPriceIn()
{
    return _priceIn;
}

void Trade::setPriceIn(double priceIn)
{
    _priceIn = priceIn;
}

double Trade::getPriceOut()
{
    return _priceOut;
}

void Trade::setPriceOut(double priceOut)
{
    _priceOut = priceOut;
}

unsigned int Trade::getVolume()
{
    return _volume;
}

void Trade::setVolume(unsigned int volume)
{
    _volume = volume;
}

void Trade::setPricePredicted(double pricePredicted)
{
    _pricePredicted = pricePredicted;
}

void Trade::debug()
{
    cout << "Trade " << _ref << " position : " << _position;
    cout << " priceIn : " << _priceIn;
    cout << " pricePredicted : " << _pricePredicted;
    cout << " priceOut : " << _priceOut;
    cout << " volume : " << _volume << endl;
}

bool Trade::isClosed()
{
    return _isClosed;
}

void Trade::setClosed(bool isClosed)
{
    _isClosed = isClosed;
}
