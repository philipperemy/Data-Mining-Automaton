#ifndef PRICESRECORD_H
#define PRICESRECORD_H

#include <iostream>
#include <string>
#include <vector>
#include "../include/Record.h"

using namespace std;

class PricesRecord
{
    public:
        PricesRecord();
        virtual ~PricesRecord();
        void addPrice(double price);
        vector<double> * getPrices();
        double getPrice(int id);
        int getCount();
        double getLastPrice();
        double getPreviousPrice();
        void registerObserver(Record* observer);
        void notifyObservers();
        void debug();

    protected:

    private:
        vector<double> * _prices;
        int _count;
        vector<Record> * _observers;
};

#endif // PRICESRECORD_H
