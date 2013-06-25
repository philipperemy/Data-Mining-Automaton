#ifndef FILEMONITOR_H
#define FILEMONITOR_H

#include <iostream>
#include <fstream>
#include <string>

using namespace std;

class FileMonitor
{
    public:
        FileMonitor(string file);
        virtual void run();
        virtual void onChange() = 0;
        void setLocked(bool lock);
        bool isLocked();

    protected:
        FileMonitor();

    private:
        long _timestamp;
        bool _lock;
        string _file;

};

#endif // FILEMONITOR_H
