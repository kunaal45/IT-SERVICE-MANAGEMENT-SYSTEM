@echo off
REM ==============================================
REM   ITSM System - Quick Compilation Test
REM ==============================================

echo.
echo ==========================================
echo   Testing ITSM System Compilation
echo ==========================================
echo.

echo [1/3] Cleaning previous builds...
call mvn clean -q
echo      Done!

echo.
echo [2/3] Compiling project...
call mvn compile

if errorlevel 1 (
    echo.
    echo ==========================================
    echo   COMPILATION FAILED!
    echo ==========================================
    echo.
    echo Please check the errors above.
    pause
    exit /b 1
)

echo.
echo ==========================================
echo   COMPILATION SUCCESSFUL!
echo ==========================================
echo.
echo All 10 errors have been fixed:
echo   - SLAController ApiResponse type inference
echo   - TicketController method signature mismatches
echo   - Missing method parameters
echo   - Incorrect method names
echo.
echo [3/3] Ready to run!
echo.
echo Options:
echo   1. Run application:    mvn spring-boot:run
echo   2. Run in IntelliJ:    Press Shift+F10
echo   3. Push to GitHub:     git push origin main
echo.

pause
