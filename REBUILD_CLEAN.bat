@echo off
REM Clean Maven build and rebuild the entire project
REM This fixes classpath issues between compile and test-compile phases

echo.
echo ========================================
echo   ITSM System - Clean Rebuild Script
echo ========================================
echo.

REM Change to project directory
cd /d "%~dp0"

echo [STEP 1] Clearing Maven cache and target directory...
call mvn clean -q
if errorlevel 1 (
    echo ERROR: Maven clean failed. Make sure Maven is installed and in your PATH.
    pause
    exit /b 1
)
echo [OK] Target directory cleaned.

echo.
echo [STEP 2] Compiling main source code...
call mvn compile -q
if errorlevel 1 (
    echo ERROR: Main compilation failed. Check the error messages above.
    pause
    exit /b 1
)
echo [OK] Main source code compiled successfully.

echo.
echo [STEP 3] Compiling test source code...
call mvn test-compile -q
if errorlevel 1 (
    echo ERROR: Test compilation failed. Check the error messages above.
    pause
    exit /b 1
)
echo [OK] Test source code compiled successfully.

echo.
echo ========================================
echo   Build Successful!
echo ========================================
echo.
echo Next step: Run the application with:
echo   mvn spring-boot:run
echo.
pause
