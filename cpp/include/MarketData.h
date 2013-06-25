#ifndef MARKETDATA_H
#define MARKETDATA_H

#include <iostream>
#include <sstream>
#include <vector>
#include <windows.h>
#include <string>
#include <fstream>

using namespace std;

class MarketData
{
    public:
        MarketData();
        virtual ~MarketData();
        string getMarketDatas(string symbol, int horizon);
        string getLastDay();
        vector<string> getUpdateMarketDataManually();
    protected:
    private:
        string _filename;
};

#endif // MARKETDATA_H
