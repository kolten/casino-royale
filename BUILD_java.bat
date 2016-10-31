@echo off
echo call corba/BUILD_cj_casinoroyale_types.bat %*
call corba/BUILD_cj_casinoroyale_types.bat %*
IF NOT %ERRORLEVEL% == 0 (
ECHO:
ECHO *** Error building corba/BUILD_cj_casinoroyale_types.bat
ECHO:
GOTO error
)
cd %~dp0
echo call corba/BUILD_cj_casinoroyale_pub.bat %*
call corba/BUILD_cj_casinoroyale_pub.bat %*
IF NOT %ERRORLEVEL% == 0 (
ECHO:
ECHO *** Error building corba/BUILD_cj_casinoroyale_pub.bat
ECHO:
GOTO error
)
cd %~dp0
echo call corba/BUILD_cj_casinoroyale_sub.bat %*
call corba/BUILD_cj_casinoroyale_sub.bat %*
IF NOT %ERRORLEVEL% == 0 (
ECHO:
ECHO *** Error building corba/BUILD_cj_casinoroyale_sub.bat
ECHO:
GOTO error
)
cd %~dp0
echo call standalone/BUILD_saj_casinoroyale_types.bat %*
call standalone/BUILD_saj_casinoroyale_types.bat %*
IF NOT %ERRORLEVEL% == 0 (
ECHO:
ECHO *** Error building standalone/BUILD_saj_casinoroyale_types.bat
ECHO:
GOTO error
)
cd %~dp0
echo call standalone/BUILD_saj_casinoroyale_pub.bat %*
call standalone/BUILD_saj_casinoroyale_pub.bat %*
IF NOT %ERRORLEVEL% == 0 (
ECHO:
ECHO *** Error building standalone/BUILD_saj_casinoroyale_pub.bat
ECHO:
GOTO error
)
cd %~dp0
echo call standalone/BUILD_saj_casinoroyale_sub.bat %*
call standalone/BUILD_saj_casinoroyale_sub.bat %*
IF NOT %ERRORLEVEL% == 0 (
ECHO:
ECHO *** Error building standalone/BUILD_saj_casinoroyale_sub.bat
ECHO:
GOTO error
)
cd %~dp0
GOTO end
:error
ECHO An error occurred, exiting now
:end
