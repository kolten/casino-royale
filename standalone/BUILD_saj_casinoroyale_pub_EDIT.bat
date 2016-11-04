@echo off

cd %~dp0
setlocal

set MAINCLASS=CasinoRoyaleDataPublisher
set JARFILE=saj_casinoroyale_pub.jar
set MANIFEST=saj_casinoroyale_pub.manifest

IF [%1]==[] GOTO build
IF /I "%1"=="clean" GOTO clean
IF /I "%1"=="rebuild" GOTO clean
ECHO Error: unrecognised option: %1
GOTO error

:clean
REM
REM Clean any previous output
REM
echo Cleaning...
del /f/s/q classes\%MANIFEST% 2>nul
del /f/s/q classes\CasinoRoyaleDataPublisher.class classes\DDSEntityManager.class classes\ErrorHandler.class 2>nul

IF /I "%1"=="clean" GOTO end

:build

REM
REM Compile java code
REM
echo Creating class output dir classes\....
if not exist classes\ echo mkdir classes\
if not exist classes\ mkdir classes\
echo Compiling Java classes....

javac -sourcepath ..\src -cp "saj_casinoroyale_types.jar;classes\;dcpssaj.jar;" -d classes\ ..\src\*.java


REM
REM Build a jar file
REM
set JARFLAGS=cvfm

pushd classes\ & jar %JARFLAGS% %JARFILE% %MANIFEST%  *.class & popd

move /y classes\%JARFILE% .


GOTO end

:error
ECHO An error occurred, exiting now
:end
