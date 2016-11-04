@echo off
echo Compiling all packages
echo Compiling cr.bjd
javac -d build cr/bjd/*.java
echo Compiling cr.bjp
javac -d build cr/bjp/*.java
echo Compiling cr.snooper
javac -d build cr/snooper/*.java


@pause
