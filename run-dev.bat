@echo off
cd /d %~dp0

echo ============================================
echo   Starting ITSM System on port 8080...
echo   (Using .env for TiDB Cloud Connection)
echo   URL: http://localhost:8080
echo ============================================
echo.

call .\mvnw.cmd spring-boot:run

pause
