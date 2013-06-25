#ifndef TRADE_H
#define TRADE_H

#include <string>

using namespace std;

class Trade
{
    public:

        Trade(string ref, double priceIn, double pricePredicted, unsigned int volume, string position);
        virtual ~Trade();
        void setPosition(string position);
        string getPosition();
        string getRef();
        void setRef(string ref);
        double getPriceIn();
        void setPriceIn(double priceIn);
        double getPriceOut();
        void setPriceOut(double priceOut);
        unsigned int getVolume();
        void setVolume(unsigned int volume);
        void setPricePredicted(double pricePredicted);
        void debug();
        bool isClosed();
        void setClosed(bool isClosed);
        Trade();

    protected:

    private:
        string _ref;
        double _priceIn;
        double _priceOut;
        double _pricePredicted;
        unsigned int _volume;
        string _position;
        bool _isClosed;
};

#endif // TRADE_H
