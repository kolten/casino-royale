@echo off
echo Creating jar file with Player as main class in build folder

cd build
jar cfe player.jar cr.bjp.Player cr
cd ../
