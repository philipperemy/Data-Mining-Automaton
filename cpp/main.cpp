#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>
#include <numeric>
#include <sstream>
#include <string>
#include <sstream>
#include <stdio.h>
#include <string.h>
#include "include/Utils.h"
#include "include/TradingRobot.h"
#include "include/TradingRobotFactory.h"
#include "include/Utils.h"
#include "include/WeightsContainer.h"
#include "include/MarketData.h"

using namespace std;
/**
    Binary : FCHI 4;4;4;4 4 0.9 0.1 10000 70 0 1 1
**/
int main(int argc, char ** argv)
{
    cout << "Starting Trading Robot - default mode" << endl;

    if(argc != Utils::ARGS_COUNT) {
        cout << endl << "Wrong argument count" << endl;
        cout << "Program will exit" << endl;
        exit(0);
    }

    string filename = argv[1];
    string symbol = filename;
    int hiddenCount = atoi(argv[3]);
    double momentum = atof(argv[4]);
    double learningRate = atof(argv[5]);
    int epochs = atoi(argv[6]);
    int percentage = atoi(argv[7]);
    int mainColumnId = atoi(argv[8]);
    int deltaTime = atoi(argv[9]);
    int horizon = atoi(argv[10]);

    //CAC-5d.txt 12 12 0.9 0.1 10000 90 0 7
    cout << endl;
    cout << "symbol : " << symbol << endl;
    cout << "filename : " << filename << endl;
    cout << "desired inputs : " << argv[2] << endl;
    cout << "hidden count : " << hiddenCount << endl;
    cout << "momentum : " << momentum << endl;
    cout << "learning rate : " << learningRate << endl;
    cout << "epochs : " << epochs << endl;
    cout << "delimiter percentage : " << percentage << endl;
    cout << "main column id : " << mainColumnId << endl;
    cout << "delta time : " << deltaTime << endl;
    cout << "horizon (blocks split) : " << horizon << endl;

    MarketData marketdataprovider;
    filename = marketdataprovider.getMarketDatas(symbol, horizon);
    vector<string> updates = marketdataprovider.getUpdateMarketDataManually();

    cout << "updates vector size = " << updates.size() << endl;

    vector<int> desiredInputs;
    desiredInputs.reserve(Utils::MAX_WEIGHTS);

    string myText(argv[2]);
    istringstream iss(myText);
    string token;

    while(getline(iss, token, Utils::SEPARATOR)) {
      istringstream ss(token);
      int tmp = 0;
      ss >> tmp;
      desiredInputs.push_back(tmp);
    }

    Utils::displayVectorInt(desiredInputs);

    ///Update CSV transformation -> .UPDATED
    string updatedFilename = filename + ".UPDATED";
    CSVParser * csvparser = new CSVParser(filename);
    csvparser->concatenate(updates);
    csvparser->debug(updatedFilename);
    delete(csvparser);

    ///Binary CSV transformation -> .BINARY
    string binFilename;
    CSVParser * csvparserBinary = new CSVParser(updatedFilename);
    binFilename = csvparserBinary->binaryTransformation(deltaTime, filename);
    delete(csvparserBinary);

    cout << "binary file = " << binFilename << endl;

    TradingRobot tradingrobot(binFilename, desiredInputs, hiddenCount,
                              deltaTime, mainColumnId, percentage,
                              epochs, learningRate, momentum);

    tradingrobot.run();
    return EXIT_SUCCESS;
}

