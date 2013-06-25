@echo off
echo Retrieving Market Datas
echo %1
echo %2
echo %3
if /i %2==FCHI goto CAC40
if /i %2==DJI goto DOWJONES
"C:\Program Files\Java\jdk1.7.0_02\bin\java.exe" -jar marketdatas.jar %1 %2 %3 > NUL
exit
:CAC40
echo CAC40 detected
"C:\Program Files\Java\jdk1.7.0_02\bin\java.exe" -jar marketdatas.jar %1 "^FCHI" %3 > NUL
exit
:DOWJONES
echo DOWJONES detected
"C:\Program Files\Java\jdk1.7.0_02\bin\java.exe" -jar marketdatas.jar %1 "^DJI" %3 > NUL
exit