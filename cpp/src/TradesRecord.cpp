#include "../include/TradesRecord.h"
#include "../include/Trade.h"

TradesRecord::TradesRecord()
{
    _count = 0;
    _trades = new vector<Trade>;
}

TradesRecord::~TradesRecord()
{
    delete(_trades);
}

void TradesRecord::saveTrade(string ref, double priceIn, double pricePredicted, int volume, string position)
{
    Trade trade(ref, priceIn, pricePredicted, volume, position);
    _trades->push_back(trade);
    _count++;
}

double TradesRecord::getProfitAndLoss()
{
    double profitLoss = 0.0;
    for(int i=0; i<_count; i++)
    {
        if(_trades->at(i).isClosed())
        {
            if(_trades->at(i).getPosition() == "BUY")
                profitLoss += ( _trades->at(i).getPriceOut() - _trades->at(i).getPriceIn() ) * _trades->at(i).getVolume();
            else
                profitLoss += ( _trades->at(i).getPriceIn() - _trades->at(i).getPriceOut() ) * _trades->at(i).getVolume();
        }
    }
    return profitLoss;
}

void TradesRecord::update(double price)
{
    updateLastTrade(price);
}

void TradesRecord::updateLastTrade(double priceOut)
{
    if(_count > 1)
    {
        _trades->at(_count-1).setPriceOut(priceOut);
    }
}

void TradesRecord::debug()
{
    for(int i=0; i<_count; i++)
        _trades->at(i).debug();
}
