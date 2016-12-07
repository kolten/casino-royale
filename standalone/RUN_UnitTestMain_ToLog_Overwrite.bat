@echo off
echo Output stream directed to UnitTestLog.txt
echo Running UnitTestMain
java -cp "%OSPL_HOME%\jar\dcpssaj.jar";.;junit-4.12.jar;hamcrest-core-1.3.jar;classes UnitTestMain > UnitTestLog.txt
pause
echo on