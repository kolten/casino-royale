@echo off
cd src/
javadoc *.java -d  ../javadoc
cd ../
echo Ignore import CR, import DDS errors since we don't want javadocs for those.
pause
echo on
REM This attempts to generate javadocs from all .java files in /src
REM Generated javadocs are placed in /javadoc in the root directory