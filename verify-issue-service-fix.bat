@echo off
REM ===================================================
REM   ITSM System - Verify IssueService Fix
REM ===================================================

echo.
echo ===================================================
echo   COMPILING ITSM SYSTEM - VERIFYING FIX
echo ===================================================
echo.

echo [STEP 1] Cleaning previous builds...
call mvn clean -q
if errorlevel 1 (
    echo ERROR: Clean failed!
    pause
    exit /b 1
)
echo DONE ✓

echo.
echo [STEP 2] Compiling all 76 source files...
call mvn compile

if errorlevel 1 (
    echo.
    echo ===================================================
    echo   COMPILATION FAILED!
    echo ===================================================
    echo.
    pause
    exit /b 1
)

echo.
echo ===================================================
echo   ✅ COMPILATION SUCCESSFUL!
echo ===================================================
echo.
echo IssueService has been fixed and compiled successfully!
echo.
echo Next steps:
echo   1. Run: mvn spring-boot:run
echo   2. Or: Press Shift+F10 in IntelliJ
echo   3. Push: Ctrl+Shift+K in IntelliJ
echo.

pause
