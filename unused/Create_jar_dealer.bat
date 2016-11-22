@echo off
echo Creating jar file with Dealer as main class in build folder

cd build
jar cfe dealer.jar cr.bjp.Dealer cr
cd ../
