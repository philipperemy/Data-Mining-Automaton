#ifndef RECORD_H
#define RECORD_H
#include <string>
using namespace std;

class Record
{
    public:
        virtual void saveTrade(string ref, double priceIn, double pricePredicted, int count, string position);
        virtual void updateLastTrade(double priceOut);
        virtual void update(double price);
        virtual ~Record();

    protected:
        Record();

    private:


};

#endif // RECORD_H
