@echo off
echo Running UnitTestMain
java -cp "%OSPL_HOME%\jar\dcpssaj.jar";.;junit-4.12.jar;hamcrest-core-1.3.jar;classes UnitTestMain
pause
echo on