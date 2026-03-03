Write-Host ""
Write-Host "═══════════════════════════════════════════════════════════" -ForegroundColor Green
Write-Host "            ITSM SYSTEM - ERROR AUTO-FIX (PowerShell)" -ForegroundColor Green
Write-Host "═══════════════════════════════════════════════════════════" -ForegroundColor Green
Write-Host ""
Write-Host "This script will fix TWO critical errors:" -ForegroundColor Yellow
Write-Host ""
Write-Host " [1] Database: role column too small (Data truncated error)" -ForegroundColor White
Write-Host " [2] Compilation: Old .class files with wrong package" -ForegroundColor White
Write-Host ""
Write-Host "═══════════════════════════════════════════════════════════" -ForegroundColor Green
Write-Host ""

# Check if MySQL is in PATH
$mysqlFound = $false
try {
    $null = Get-Command mysql -ErrorAction Stop
    $mysqlFound = $true
} catch {
    $mysqlFound = $false
}

Write-Host "───────────────────────────────────────────────────────────" -ForegroundColor Cyan
Write-Host " STEP 1 OF 3: Fixing Database Schema" -ForegroundColor Cyan
Write-Host "───────────────────────────────────────────────────────────" -ForegroundColor Cyan
Write-Host ""

if ($mysqlFound) {
    Write-Host "Running: ALTER TABLE users MODIFY COLUMN role VARCHAR(20)" -ForegroundColor Yellow
    Write-Host ""
    
    & mysql -u root -p2005 -e "USE itsm_db; ALTER TABLE users MODIFY COLUMN role VARCHAR(20) NOT NULL;"
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host ""
        Write-Host "✓ Database schema fixed successfully!" -ForegroundColor Green
    } else {
        Write-Host ""
        Write-Host "⚠ WARNING: MySQL command failed!" -ForegroundColor Red
        Write-Host ""
        Write-Host "MANUAL FIX REQUIRED:" -ForegroundColor Yellow
        Write-Host "--------------------------------------------------" -ForegroundColor Yellow
        Write-Host "1. Open MySQL Workbench" -ForegroundColor White
        Write-Host "2. Connect to your database" -ForegroundColor White
        Write-Host "3. Run this SQL:" -ForegroundColor White
        Write-Host ""
        Write-Host "   USE itsm_db;" -ForegroundColor Cyan
        Write-Host "   ALTER TABLE users MODIFY COLUMN role VARCHAR(20) NOT NULL;" -ForegroundColor Cyan
        Write-Host ""
        Write-Host "--------------------------------------------------" -ForegroundColor Yellow
        Write-Host ""
        Write-Host "Press any key after you have run the SQL commands..." -ForegroundColor Yellow
        $null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
    }
} else {
    Write-Host "MySQL command not found in PATH." -ForegroundColor Yellow
    Write-Host ""
    Write-Host "MANUAL FIX REQUIRED:" -ForegroundColor Yellow
    Write-Host "--------------------------------------------------" -ForegroundColor Yellow
    Write-Host "1. Open MySQL Workbench" -ForegroundColor White
    Write-Host "2. Connect to your database (root / 2005)" -ForegroundColor White
    Write-Host "3. Run this SQL:" -ForegroundColor White
    Write-Host ""
    Write-Host "   USE itsm_db;" -ForegroundColor Cyan
    Write-Host "   ALTER TABLE users MODIFY COLUMN role VARCHAR(20) NOT NULL;" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "OR simply open and execute: fix-role-column.sql" -ForegroundColor White
    Write-Host "--------------------------------------------------" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Press any key after you have run the SQL commands..." -ForegroundColor Yellow
    $null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
}

Write-Host ""
Write-Host "───────────────────────────────────────────────────────────" -ForegroundColor Cyan
Write-Host " STEP 2 OF 3: Cleaning Old Compiled Files" -ForegroundColor Cyan
Write-Host "───────────────────────────────────────────────────────────" -ForegroundColor Cyan
Write-Host ""
Write-Host "Deleting target/ directory..." -ForegroundColor Yellow
Write-Host ""

& .\mvnw.cmd clean

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "✓ Old compiled files deleted successfully!" -ForegroundColor Green
} else {
    Write-Host ""
    Write-Host "✗ ERROR: Maven clean failed!" -ForegroundColor Red
    Write-Host "Please check Maven is installed correctly." -ForegroundColor Red
    Write-Host ""
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host ""
Write-Host "───────────────────────────────────────────────────────────" -ForegroundColor Cyan
Write-Host " STEP 3 OF 3: Compiling and Starting Application" -ForegroundColor Cyan
Write-Host "───────────────────────────────────────────────────────────" -ForegroundColor Cyan
Write-Host ""
Write-Host "This may take 30-60 seconds..." -ForegroundColor Yellow
Write-Host "Watch for: 'Started ItsmSystemApplication'" -ForegroundColor Yellow
Write-Host ""

& .\mvnw.cmd spring-boot:run

Write-Host ""
Write-Host "═══════════════════════════════════════════════════════════" -ForegroundColor Green
Write-Host " If you see 'Started ItsmSystemApplication' above," -ForegroundColor Green
Write-Host " the application is now running on http://localhost:8080" -ForegroundColor Green
Write-Host "═══════════════════════════════════════════════════════════" -ForegroundColor Green
Write-Host ""
Read-Host "Press Enter to exit"
