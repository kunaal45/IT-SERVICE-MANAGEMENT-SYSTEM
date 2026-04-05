@echo off
cd /d %~dp0

echo ============================================
echo   ITSM System - Loading .env
echo ============================================

REM Load .env file into environment variables
for /f "usebackq tokens=1,* delims==" %%A in (".env") do (
    REM Skip comments and empty lines
    if not "%%A"=="" (
        set "line=%%A"
        if not "!line:~0,1!"=="#" (
            set "%%A=%%B"
        )
    )
)

REM Simpler approach - set each variable explicitly from .env parsing via PowerShell
for /f "tokens=*" %%i in ('powershell -NoProfile -Command "Get-Content .env | Where-Object { $_ -notmatch '^#' -and $_ -match '=' } | ForEach-Object { $parts = $_ -split '=',2; \"SET $($parts[0].Trim())=$($parts[1].Trim())\" }"') do (
    %%i
)

echo DB_URL     = %DB_URL%
echo DB_USERNAME= %DB_USERNAME%
echo JWT loaded : %JWT_SECRET:~0,8%...

echo.
echo ============================================
echo   Starting ITSM System on port 8080...
echo   URL: http://localhost:8080
echo ============================================
echo.

call .\mvnw.cmd spring-boot:run

pause
