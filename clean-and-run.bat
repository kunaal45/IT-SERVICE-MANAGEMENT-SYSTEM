@echo off
echo.
echo ============================================
echo   ITSM System - Clean and Run
echo ============================================
echo.
echo Cleaning old compiled files...
call mvnw.cmd clean

echo.
echo Compiling and running the application...
call mvnw.cmd spring-boot:run

pause
