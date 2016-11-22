@echo off
echo Creating jar file with Snooper as main class in build folder

cd build
jar cfe snooper.jar cr.bjp.Snooper cr
cd ../
