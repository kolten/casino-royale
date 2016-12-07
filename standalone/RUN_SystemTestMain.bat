@echo off
echo Running SystemTestMain
java -cp "%OSPL_HOME%\jar\dcpssaj.jar";.;junit-4.12.jar;hamcrest-core-1.3.jar;classes SystemTestMain
pause
echo on