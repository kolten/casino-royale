@echo off

echo JUnit included in compile script

call BUILD_saj_casinoroyale_pub_EDIT.bat %*

cd %~dp0

call BUILD_saj_casinoroyale_sub_EDIT.bat %*

cd %~dp0

pause