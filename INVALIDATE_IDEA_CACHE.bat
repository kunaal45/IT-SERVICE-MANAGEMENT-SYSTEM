@echo off
REM This script invalidates IntelliJ IDEA caches which can cause compilation issues

echo.
echo ========================================
echo   Invalidating IntelliJ IDEA Cache
echo ========================================
echo.

REM Get the user profile path
set IDEA_CONFIG=%USERPROFILE%\.IntelliJIdea2024\system
set IDEA_CONFIG_ALT=%USERPROFILE%\.IntelliJIdea2023\system
set IDEA_CONFIG_ALT2=%USERPROFILE%\.IntelliJIdea2025\system

echo Checking for IntelliJ cache directories...
echo.

if exist "%IDEA_CONFIG%" (
    echo Found: %IDEA_CONFIG%
    echo.
    echo Backing up and clearing cache...
    if exist "%IDEA_CONFIG%\cache" rmdir /s /q "%IDEA_CONFIG%\cache" 2>nul
    if exist "%IDEA_CONFIG%\caches" rmdir /s /q "%IDEA_CONFIG%\caches" 2>nul
    echo Cache cleared.
) else if exist "%IDEA_CONFIG_ALT%" (
    echo Found: %IDEA_CONFIG_ALT%
    echo.
    echo Backing up and clearing cache...
    if exist "%IDEA_CONFIG_ALT%\cache" rmdir /s /q "%IDEA_CONFIG_ALT%\cache" 2>nul
    if exist "%IDEA_CONFIG_ALT%\caches" rmdir /s /q "%IDEA_CONFIG_ALT%\caches" 2>nul
    echo Cache cleared.
) else if exist "%IDEA_CONFIG_ALT2%" (
    echo Found: %IDEA_CONFIG_ALT2%
    echo.
    echo Backing up and clearing cache...
    if exist "%IDEA_CONFIG_ALT2%\cache" rmdir /s /q "%IDEA_CONFIG_ALT2%\cache" 2>nul
    if exist "%IDEA_CONFIG_ALT2%\caches" rmdir /s /q "%IDEA_CONFIG_ALT2%\caches" 2>nul
    echo Cache cleared.
) else (
    echo Could not find IntelliJ cache directory.
    echo Tried: %IDEA_CONFIG%
    echo Tried: %IDEA_CONFIG_ALT%
    echo Tried: %IDEA_CONFIG_ALT2%
)

echo.
echo ========================================
echo   Done!
echo ========================================
echo.
echo Next steps:
echo 1. Close IntelliJ IDEA completely
echo 2. Run REBUILD_CLEAN.bat
echo 3. Reopen IntelliJ IDEA
echo.
pause
