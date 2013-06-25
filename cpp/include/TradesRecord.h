#ifndef TRADESRECORD_H
#define TRADESRECORD_H

#include "Trade.h"
#include "Record.h"
#include <vector>
#include <iostream>

using namespace std;

class TradesRecord : Record
{
    public:
        TradesRecord();
        virtual ~TradesRecord();
        virtual void saveTrade(string ref, double priceIn, double pricePredicted, int volume, string position);
        virtual void updateLastTrade
        (double priceOut);
        virtual void update(double price);
        double getProfitAndLoss();
        void debug();

    protected:

    private:
        vector<Trade> * _trades;
        int _count;
};

#endif // TRADESRECORD_H
