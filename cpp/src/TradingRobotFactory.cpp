#include "../include/TradingRobotFactory.h"

TradingRobot* TradingRobotFactory::createInstance()
{
    TradingRobot* tradingrobot = new TradingRobot();
    return tradingrobot;
}
