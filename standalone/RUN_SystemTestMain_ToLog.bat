@echo off
echo All output will be saved to SystemTestLog.txt
echo Running SystemTestMain
java -cp "%OSPL_HOME%\jar\dcpssaj.jar";.;junit-4.12.jar;hamcrest-core-1.3.jar;classes SystemTestMain >> SystemTestLog.txt
REM type SystemTestLog.txt
pause
echo on