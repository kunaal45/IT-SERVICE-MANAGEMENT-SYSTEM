@echo off
echo.
echo ============================================
echo   ITSM System - Clean Build and Run
echo ============================================
echo.
echo Step 1: Cleaning old files...
call mvnw.cmd clean

echo.
echo Step 2: Starting application...
echo (This may take 30-60 seconds)
echo.
call mvnw.cmd spring-boot:run

pause
