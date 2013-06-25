#ifndef FILEMONITORROBOT_H
#define FILEMONITORROBOT_H

#include "../Header.h"

class TradingRobot;

class FileMonitorRobot : public FileMonitor
{
    public:
        FileMonitorRobot(string file, TradingRobot& tradingrobot);
        virtual ~FileMonitorRobot();
        virtual void onChange();

    protected:
        FileMonitorRobot();

    private:
        TradingRobot& _tradingrobot;
};

#endif // FILEMONITORROBOT_H
