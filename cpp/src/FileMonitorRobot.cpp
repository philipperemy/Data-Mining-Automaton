#include "../include/FileMonitorRobot.h"
#include "../include/TradingRobot.h"

#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>

FileMonitorRobot::FileMonitorRobot(string file, TradingRobot& tradingrobot) : FileMonitor(file), _tradingrobot(tradingrobot)
{
    setLocked(false);
}

FileMonitorRobot::~FileMonitorRobot()
{
}

void FileMonitorRobot::onChange()
{
    cout << "File has changed... Proceeding..." << endl;
    setLocked(true);
    _tradingrobot.onChange();
    setLocked(false);
}
