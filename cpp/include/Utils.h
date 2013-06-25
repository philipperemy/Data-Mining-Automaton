#ifndef UTILS_H
#define UTILS_H

#include <vector>

using namespace std;

class Utils
{
    public:

        static double sigmoid(double value);
        static int charToInt(char c);
        static void displayVectorInt(vector<int> v);
        static const int ARGS_COUNT = 11;
        static const int INT_MAX = 10000000;
        static const int OUTPUT_COUNT = 1;
        static const int LATENCY_ON_DETECTING_FILE_MODIFICATION = 1000;
        static const char SEPARATOR = ';';
        static const int VOLUME = 1;
        static const int MAX_LINES = 5100;
        static const int MAX_COLUMNS = 20;
        static const int MAX_WEIGHTS = 150;

    protected:

    private:
};

#endif // UTILS_H
