#include "../include/PricesRecord.h"

PricesRecord::PricesRecord()
{
    _prices = new vector<double>;
    _observers = new vector<Record>;
    _count = 0;
}

PricesRecord::~PricesRecord()
{
    delete(_prices);
    delete(_observers);
}

void PricesRecord::addPrice(double price)
{
    _prices->push_back(price);
    _count++;
    notifyObservers();
}

vector<double> * PricesRecord::getPrices()
{
    return _prices;
}

double PricesRecord::getPrice(int id)
{
    return _prices->at(id);
}

int PricesRecord::getCount()
{
    return _count;
}

double PricesRecord::getLastPrice()
{
    return _prices->at(_count-1);
}

double PricesRecord::getPreviousPrice()
{
    return _prices->at(_count-2);
}

void PricesRecord::registerObserver(Record* observer)
{
    _observers->push_back(*observer);
}

void PricesRecord::notifyObservers()
{
    for(int i=0; i<_observers->size(); i++)
        _observers->at(i).update(getLastPrice());
}

void PricesRecord::debug()
{
    for(size_t i=0; i<_prices->size(); i++)
    {
        cout << "price[" << i << "] = " << _prices->at(i) << endl;
    }
}
