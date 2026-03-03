@echo off
REM ITSM System - Pre-Flight Check Script (Windows)
REM This script verifies your environment is ready to run the project

echo.
echo ============================================
echo   ITSM System - Pre-Flight Verification
echo ============================================
echo.

REM Check Java Installation
echo [1/4] Checking Java Installation...
java -version 2>&1 | find "17" >nul
if %errorlevel% equ 0 (
    echo   ✓ Java 17 is installed
) else (
    echo   ✗ Java 17 NOT found!
    echo     Please install JDK 17 from https://adoptopenjdk.net/
    pause
    exit /b 1
)

REM Check Maven Installation
echo.
echo [2/4] Checking Maven...
mvn -version >nul 2>&1
if %errorlevel% equ 0 (
    echo   ✓ Maven is installed
) else (
    echo   ✓ Maven not globally installed (will use mvnw.cmd wrapper)
)

REM Check Port 8080
echo.
echo [3/4] Checking if Port 8080 is available...
netstat -ano | findstr :8080 >nul
if %errorlevel% equ 0 (
    echo   ✗ Port 8080 is already in use!
    echo     Change server.port in src/main/resources/application.properties
    pause
    exit /b 1
) else (
    echo   ✓ Port 8080 is available
)

REM Check Project Files
echo.
echo [4/4] Checking Project Files...
if exist "src\main\java\com\itsm\itsmsystem\ItsmSystemApplication.java" (
    echo   ✓ Main application file found
) else (
    echo   ✗ Main application file NOT found!
    exit /b 1
)

if exist "src\main\resources\static\index.html" (
    echo   ✓ Frontend files found
) else (
    echo   ✗ Frontend files NOT found!
    exit /b 1
)

if exist "pom.xml" (
    echo   ✓ Maven configuration found
) else (
    echo   ✗ Maven configuration NOT found!
    exit /b 1
)

echo.
echo ============================================
echo   ✓ ALL CHECKS PASSED!
echo ============================================
echo.
echo Ready to run? Press Enter to start the application...
echo.
pause

REM Start Application
echo Starting ITSM System...
echo.
mvnw.cmd spring-boot:run

REM If mvnw.cmd fails, try mvn
if %errorlevel% neq 0 (
    echo Trying with 'mvn' command...
    mvn spring-boot:run
)
