#include "../include/FileMonitor.h"
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>

FileMonitor::FileMonitor()
{
}

FileMonitor::FileMonitor(string file) : _file(file)
{
}

void FileMonitor::setLocked(bool lock)
{
    _lock = lock;
}

bool FileMonitor::isLocked()
{
    return _lock;
}

void FileMonitor::run()
{
    cout << "Call to FileMonitor::run()" << endl;
    struct stat fileStruct;

    if(stat(_file.c_str(), &fileStruct) == -1) {
        perror("stat");
    }

    long timestamp = fileStruct.st_mtime;

    if(_timestamp != timestamp) {
        _timestamp = timestamp;

        if(!isLocked())
            onChange();
    }
}
