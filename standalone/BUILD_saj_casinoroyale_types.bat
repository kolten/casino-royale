@echo off
echo cd %~dp0
cd %~dp0
setlocal

set JARFILE=saj_casinoroyale_types.jar
set MANIFEST=saj_casinoroyale_types.manifest

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
del /f/s/q CasinoRoyaleData\*.java  2>nul
del /f/s/q classes\CasinoRoyaleData\*.class 2>nul

IF /I "%1"=="clean" GOTO end

:build

REM
REM Generate java classes from IDL
REM
echo Processing ..\idl\CasinoRoyaleData.idl....
echo "C:\HDE\x86_64.win64\bin\idlpp" -I "C:\HDE\x86_64.win64\etc\idl" -l java ..\idl\CasinoRoyaleData.idl
"C:\HDE\x86_64.win64\bin\idlpp" -I "C:\HDE\x86_64.win64\etc\idl" -l java ..\idl\CasinoRoyaleData.idl
IF NOT %ERRORLEVEL% == 0 (
  ECHO:
  ECHO *** Compilation of ..\idl\CasinoRoyaleData.idl failed
  ECHO:
  GOTO error
)


REM
REM Compile java code
REM
echo Creating class output dir classes\....
if not exist classes\ echo mkdir classes\
if not exist classes\ mkdir classes\
echo Compiling Java classes....
echo javac -cp "classes\;..\dcpssaj.jar;" -d classes\ CasinoRoyaleData\*.java
javac -cp "classes\;..\dcpssaj.jar;" -d classes\ CasinoRoyaleData\*.java
IF NOT %ERRORLEVEL% == 0 (
  ECHO:
  ECHO *** Java compilation of CasinoRoyaleData\*.java failed
  ECHO:
  GOTO error
)

REM
REM Build a jar file
REM
set JARFLAGS=cvfm
echo Building a jar file....
echo echo Class-Path: ..\dcpssaj.jar ^> classes\%MANIFEST%
echo Class-Path: ..\dcpssaj.jar > classes\%MANIFEST%
echo pushd classes\ ^& jar %JARFLAGS% %JARFILE% %MANIFEST%  CasinoRoyaleData\*.class ^& popd
pushd classes\ & jar %JARFLAGS% %JARFILE% %MANIFEST%  CasinoRoyaleData\*.class & popd
echo move /y classes\%JARFILE% .
move /y classes\%JARFILE% .
IF NOT %ERRORLEVEL% == 0 (
  ECHO:
  ECHO *** Building jar file %JARFILE% failed
  ECHO:
  GOTO error
)

GOTO end

:error
ECHO An error occurred, exiting now
:end
