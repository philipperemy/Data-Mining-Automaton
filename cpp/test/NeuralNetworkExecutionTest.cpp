/*#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>
#include <numeric>
#include <sstream>
#include <string>
#include <stdio.h>
#include <string.h>
#include "include/Utils.h"
#include "include/TradingRobot.h"
#include "include/TradingRobotFactory.h"
#include "include/Utils.h"
#include "include/NeuralNetworkExecution.h"

using namespace std;

int main(int argc, char ** argv)
{
    cout << "NeuralNetwork Execution test" << endl;

    CSVParser csvparser("credit_agricole_2.csv");
    csvparser.debug();

    NeuralNetwork neuralnetwork(4,4,1);
    cout << "Neural network created" << endl;

    vector<int> desiredInputs;
    desiredInputs.push_back(1);
    desiredInputs.push_back(1);
    desiredInputs.push_back(1);
    desiredInputs.push_back(1);

    NeuralNetworkExecution neuralnetworkexecution(0.1, 0.9, 10000, 90, csvparser, 1, desiredInputs, 3, neuralnetwork, 5);

    neuralnetworkexecution.getNeuralNetwork().toString();

    return EXIT_SUCCESS;
}
*/
