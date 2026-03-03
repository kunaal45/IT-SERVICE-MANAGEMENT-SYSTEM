@echo off
color 0A
echo.
echo ═══════════════════════════════════════════════════════════
echo             ITSM SYSTEM - ERROR AUTO-FIX
echo ═══════════════════════════════════════════════════════════
echo.
echo  This script will fix TWO critical errors:
echo.
echo  [1] Database: role column too small (Data truncated error)
echo  [2] Compilation: Old .class files with wrong package
echo.
echo ═══════════════════════════════════════════════════════════
echo.
echo  Starting in 3 seconds... (Press Ctrl+C to cancel)
timeout /t 3 /nobreak >nul
echo.
echo ───────────────────────────────────────────────────────────
echo  STEP 1 OF 3: Fixing Database Schema
echo ───────────────────────────────────────────────────────────
echo.
echo  Running: ALTER TABLE users MODIFY COLUMN role VARCHAR(20)
echo.

mysql -u root -p2005 -e "USE itsm_db; ALTER TABLE users MODIFY COLUMN role VARCHAR(20) NOT NULL; SELECT '✓ Database role column expanded to VARCHAR(20)' AS Status;"

if errorlevel 1 (
    echo.
    echo  ⚠️  WARNING: MySQL command failed or not in PATH
    echo.
    echo  MANUAL FIX REQUIRED:
    echo  --------------------------------------------------
    echo  1. Open MySQL Workbench
    echo  2. Connect to your database
    echo  3. Run this SQL:
    echo.
    echo     USE itsm_db;
    echo     ALTER TABLE users MODIFY COLUMN role VARCHAR^(20^) NOT NULL;
    echo.
    echo  4. Then come back and press any key to continue...
    echo  --------------------------------------------------
    echo.
    pause
    echo  Assuming you fixed the database manually...
) else (
    echo.
    echo  ✓ Database schema fixed successfully!
)

echo.
echo ───────────────────────────────────────────────────────────
echo  STEP 2 OF 3: Cleaning Old Compiled Files
echo ───────────────────────────────────────────────────────────
echo.
echo  Deleting target/ directory...
echo.

call mvnw.cmd clean

if errorlevel 1 (
    echo.
    echo  ✗ ERROR: Maven clean failed!
    echo  Please check Maven is installed correctly.
    echo.
    pause
    exit /b 1
)

echo.
echo  ✓ Old compiled files deleted successfully!

echo.
echo ───────────────────────────────────────────────────────────
echo  STEP 3 OF 3: Compiling and Starting Application
echo ───────────────────────────────────────────────────────────
echo.
echo  This may take 30-60 seconds...
echo  Watch for: "Started ItsmSystemApplication"
echo.

call mvnw.cmd spring-boot:run

echo.
echo ═══════════════════════════════════════════════════════════
echo  If you see "Started ItsmSystemApplication" above,
echo  the application is now running on http://localhost:8080
echo ═══════════════════════════════════════════════════════════
echo.
pause
