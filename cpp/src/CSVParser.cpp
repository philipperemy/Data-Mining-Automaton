#include "../include/CSVParser.h"
#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>
#include <numeric>
#include <sstream>
#include <string>
#include <stdio.h>
#include <string.h>
#include <iomanip>

using namespace std;

CSVParser::CSVParser() {}
int CSVParser::countLines() { return _countLines; }
int CSVParser::countColumns() { return _countColumns; }
bool CSVParser::isFilled() { return _isFilled; }
void CSVParser::setFilled(bool isFilled) { _isFilled = isFilled; }
bool CSVParser::isNormalized() { return _isNormalized; }
void CSVParser::setNormalized(bool isNormalized) { _isNormalized = isNormalized; }

double CSVParser::getValue(int x, int y)
{
    double returnedValue = 0.0;
    bool valid = false;

    if(x >= 0 && x <= countLines())
        if(y >= 0 && y <= countColumns()) {
            return _values[x][y];
            valid = true;
        }

    if(valid)
        return returnedValue;
    else {
        cout << "value : " << x << "," << y << " out of bounds" << endl;
        exit(1);
    }
}

double CSVParser::getNormalizedValue(int x, int y)
{
    double returnedValue = 0.0;
    bool valid = false;

    if(x >= 0 && x <= countLines())
        if(y >= 0 && y <= countColumns()) {
            return _normalizedValues[x][y];
            valid = true;
        }

    if(valid)
        return returnedValue;
    else {
        cout << "normalized value : " << x << "," << y << " out of bounds" << endl;
        exit(1);
    }
}

double CSVParser::getMaxValueFromColumn(int y)
{
    if(y >= 0 && y < countColumns()) {
        double maxValue = getValue(0, y);
        for(int x = 0; x < countLines(); x++) {
            if(getValue(x,y) > maxValue)
                maxValue = getValue(x,y);
        }
        return maxValue;
    }
    else
    {
        cout << "Invalid column : " << y << endl;
        exit(0);
    }
}

CSVParser::~CSVParser()
{

}

CSVParser::CSVParser(string filename)
{
    _filename = filename;
    ifstream fileHandler(_filename.c_str(), ios::in);

    bool hasSemicolon = false;
    if(fileHandler) {
        string line;
        size_t found;

        for(int i = 0; i < 10; i++) {
            getline(fileHandler, line);
            found = line.find(';');
            if(found != string::npos)
                hasSemicolon = true;
        }
    }
    else {
        cout << "File " << _filename << " does not exist." << endl;
        exit(1);
    }

    fileHandler.close();

    if(hasSemicolon)
        this->_separator = ';';
    else
        this->_separator = ',';

    cout << "Separator is " << _separator << endl;

    fileHandler.open(_filename.c_str(), ios::in);

    int tmp_countLines = 0;
    string line;
    while(getline(fileHandler, line))
        if(line != "")
            tmp_countLines++;

    fileHandler.close();

    _countLines = tmp_countLines;

    cout << "CountLines = " << _countLines << endl;

    fileHandler.open(_filename.c_str(), ios::in);

    int k = 0;
    int * countColumnsTmp = new int[_countLines+1];

    while(!fileHandler.eof()) {
        string line;
        getline(fileHandler, line);
        istringstream iss(line);
        string token;

        int j = 0;
        while(getline(iss, token, _separator)) {
            _values[k][j] = atof(token.c_str());
            j++;
        }
        countColumnsTmp[k] = j;
        j=0;
        k++;
    }

    int countColumnsTmpMax = 0;

    for(int i=0; i<countLines(); i++)
        if(countColumnsTmpMax <= countColumnsTmp[i])
            countColumnsTmpMax = countColumnsTmp[i];

    fileHandler.close();
    _countColumns = countColumnsTmpMax;
    cout << "CountColumns = " << _countColumns << endl;
    setFilled(true);
}

int CSVParser::concatenate(vector<string> inputs)
{
    if(inputs.size() != _countColumns) {
        cout << "CSVParser::concatenate() : sizes do not match" << endl;
        return -1;
    }

    for(int i=0; i<inputs.size(); i++) {
        _values[_countLines][i] = atof(inputs.at(i).c_str());
    }

    _countLines++;
    return 0;
}

void CSVParser::debug()
{
    string debugHandler = _filename + ".DEBUG";
    debug(debugHandler);
}

string CSVParser::debugLine(int line)
{
    string result;
    result.append(line + " - ");

    for(int i=0; i<countColumns(); i++)
    {
        std::ostringstream oss;
        oss << getNormalizedValue(line, i);

        if(i!=countColumns()-1)
            result.append(oss.str() + ";");
        else
            result.append(oss.str());
    }

    return result;
}

void CSVParser::debug(string filename)
{
    string debugHandler = filename;
    ofstream fileHandler(debugHandler.c_str(), ios::out | ios::trunc);

    for(int i = 0; i < countLines(); i++)
        for(int j = 0; j < countColumns(); j++) {
            if(j == (countColumns()-1)) {
                if(i != countLines()-1)
                    fileHandler << setiosflags(ios::fixed) << getValue(i,j) << endl;
                else
                    fileHandler << setiosflags(ios::fixed) << getValue(i,j);
            }
            else {
                fileHandler << setiosflags(ios::fixed) << getValue(i,j) << ";";
            }
        }

    fileHandler.close();
}

void CSVParser::normalizedDebug()
{
    string debugNormalizedHandler = _filename + ".DEBUG.NORMALIZED";
    ofstream fileHandler(debugNormalizedHandler.c_str(), ios::out | ios::trunc);

    for(int i = 0; i < countLines(); i++)
        for(int j = 0; j < countColumns(); j++) {
            if(j == (countColumns()-1)) {
                fileHandler << getNormalizedValue(i,j) << endl;
            }
            else {
                fileHandler << getNormalizedValue(i,j) << ";";
            }
        }

    fileHandler.close();
}

void CSVParser::normalizedDebug(string debugFilename)
{
    string debugNormalizedHandler = debugFilename;
    ofstream fileHandler(debugNormalizedHandler.c_str(), ios::out | ios::trunc);

    bool lastLine = false;
    for(int i = 0; i < countLines(); i++) {

        if(i == countLines() - 1)
            lastLine = true;

        for(int j = 0; j < countColumns(); j++) {
            if(j == (countColumns()-1)) {
                if(lastLine)
                    fileHandler << getNormalizedValue(i,j);
                else
                    fileHandler << getNormalizedValue(i,j) << endl;
            }
            else {
                fileHandler << getNormalizedValue(i,j) << ";";
            }
        }
    }
    fileHandler.close();
}

void CSVParser::reload()
{
    cout << "Reloading CSV file" << endl;
    cout << "Nothing to be done" << endl;
    cout << "System is going to exit" << endl;
    exit(0);
}

void CSVParser::normalize()
{
    for(int y=0; y<countColumns(); y++)
        for(int x=0; x<countLines(); x++)
            _normalizedValues[x][y] = (double) (getValue(x,y) / getMaxValueFromColumn(y));

    setNormalized(true);
}

string CSVParser::binaryTransformation(int deltaTime, string symbol)
{
    for(int y=0; y<countColumns(); y++) {
        for(int x=0; x<countLines(); x++) {
            if(x<deltaTime)
                _normalizedValues[x][y] = 0;
            else {
                // x >= deltaTime
                double diff = getValue(x,y) - getValue(x-deltaTime,y);
                //Si diff>0 alors x,y > x-deltaTime,y et la valeur a monté
                if(diff > 0)
                    _normalizedValues[x][y] = 1;
                else
                    _normalizedValues[x][y] = 0;
            }
        }
    }

    normalizedDebug(symbol + ".BINARY");
    setNormalized(true);

    return symbol + ".BINARY";
}


string CSVParser::binaryTransformationOpenVsClose(string symbol)
{
    for(int x=0; x<countLines(); x++) {
        ///Close - Open
        ///If greater than 0, Close>Open => 1
        ///ELSE : Close<Open => 0
        int diff = getValue(x,3)-getValue(x,0);
        if(diff > 0)
            _normalizedValues[x][0] = 1;
        else
            _normalizedValues[x][0] = 0;
    }

    normalizedDebug(symbol + ".BINARY");
    setNormalized(true);

    return symbol + ".BINARY";
}
