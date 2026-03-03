@echo off
echo.
echo ============================================
echo   ITSM System - Fix and Run
echo ============================================
echo.

echo Step 1: Cleaning old compiled files...
echo ----------------------------------------
call mvnw.cmd clean
if errorlevel 1 (
    echo ERROR: Maven clean failed!
    pause
    exit /b 1
)

echo.
echo Step 2: Fixing database schema...
echo ----------------------------------------
echo Please run the following SQL commands in MySQL:
echo.
echo USE itsm_db;
echo ALTER TABLE users MODIFY COLUMN role VARCHAR(20) NOT NULL;
echo.
echo Press any key after you have run the SQL commands...
pause

echo.
echo Step 3: Compiling and running the application...
echo ----------------------------------------
call mvnw.cmd spring-boot:run

pause
