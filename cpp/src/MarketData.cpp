#include "../include/MarketData.h"


///system("cmd.exe /c script.bat AAPL FCHI dir");

using namespace std;

MarketData::MarketData()
{
}

string MarketData::getMarketDatas(string symbol, int horizon)
{
    ostringstream oss;
    oss << horizon;
    string horizonString = oss.str();

    string filename = "files/" + symbol + "_" + horizonString + ".csv";
    string command = "cmd.exe /c script.bat " + filename + " " + symbol + " " + horizonString;
    cout << command << endl;
    system(command.c_str());

    _filename = filename;
    return filename;
}

MarketData::~MarketData()
{
    //dtor
}

string MarketData::getLastDay()
{
    string lastDayFilename = _filename + "_last_day.txt";
    std::ifstream fileHandler(lastDayFilename.c_str(), ios::in);

    if(fileHandler)
    {
        string lastDay;
        getline(fileHandler, lastDay);
        fileHandler.close();
        return lastDay;
    }
    else
    {
        cerr << "WARNING - Unable to fetch last day information " << endl;
        return "NULL";
    }
}

vector<string> MarketData::getUpdateMarketDataManually()
{
    cout << "Last fixing was on " << getLastDay() << endl;
    string choice = "N";
    cout << "Would you like to update manually the last day (Y/N) ? ";
    cin >> choice;

    vector<string> updatePriceVector;

    if(choice == "N")
        return updatePriceVector;

    string latestOpen;
    string latestHigh;
    string latestLow;
    string latestClose;
    string latestVolume;

    cout << "Open = ";
    cin >> latestOpen;

    cout << "High = ";
    cin >> latestHigh;

    cout << "Low = ";
    cin >> latestLow;

    cout << "Close = ";
    cin >> latestClose;

    cout << "Volume = ";
    cin >> latestVolume;

    updatePriceVector.push_back(latestOpen);
    updatePriceVector.push_back(latestHigh);
    updatePriceVector.push_back(latestLow);
    updatePriceVector.push_back(latestClose);
    updatePriceVector.push_back(latestVolume);

    return updatePriceVector;
}
