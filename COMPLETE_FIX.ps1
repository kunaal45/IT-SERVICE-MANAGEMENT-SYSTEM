Write-Host ""
Write-Host "============================================" -ForegroundColor Green
Write-Host "  COMPLETE FIX - Database + Clean + Run" -ForegroundColor Green
Write-Host "============================================" -ForegroundColor Green
Write-Host ""

Write-Host "STEP 1: Fix Database in MySQL Workbench" -ForegroundColor Yellow
Write-Host "---------------------------------------" -ForegroundColor Yellow
Write-Host ""
Write-Host "You need to run this SQL in MySQL Workbench:" -ForegroundColor White
Write-Host ""
Write-Host "  USE itsm_db;" -ForegroundColor Cyan
Write-Host "  ALTER TABLE users MODIFY COLUMN role VARCHAR(20) NOT NULL;" -ForegroundColor Cyan
Write-Host ""
Write-Host "How to do it:" -ForegroundColor White
Write-Host "  1. Open MySQL Workbench" -ForegroundColor Gray
Write-Host "  2. Connect to localhost (root/2005)" -ForegroundColor Gray
Write-Host "  3. Copy the SQL above and execute it" -ForegroundColor Gray
Write-Host ""
Write-Host "OR simply open the file: FIX_DATABASE.sql and execute it" -ForegroundColor White
Write-Host ""
$response = Read-Host "Have you fixed the database? (y/n)"

if ($response -ne "y" -and $response -ne "Y") {
    Write-Host ""
    Write-Host "Please fix the database first, then run this script again." -ForegroundColor Red
    Write-Host ""
    Read-Host "Press Enter to exit"
    exit
}

Write-Host ""
Write-Host "STEP 2: Force Delete Target Directory" -ForegroundColor Yellow
Write-Host "---------------------------------------" -ForegroundColor Yellow
Write-Host ""

if (Test-Path "target") {
    Write-Host "Deleting target directory..." -ForegroundColor White
    Remove-Item -Path "target" -Recurse -Force
    Write-Host "✓ Target directory deleted!" -ForegroundColor Green
} else {
    Write-Host "✓ Target directory doesn't exist (already clean)" -ForegroundColor Green
}

Write-Host ""
Write-Host "STEP 3: Compile and Run Application" -ForegroundColor Yellow
Write-Host "---------------------------------------" -ForegroundColor Yellow
Write-Host ""
Write-Host "Starting Maven (this takes 30-60 seconds)..." -ForegroundColor White
Write-Host "Watch for: 'Started ItsmSystemApplication'" -ForegroundColor White
Write-Host ""

& .\mvnw.cmd spring-boot:run

Write-Host ""
Write-Host "============================================" -ForegroundColor Green
Write-Host "If you see 'Started ItsmSystemApplication'" -ForegroundColor Green
Write-Host "Open: http://localhost:8080" -ForegroundColor Green
Write-Host "============================================" -ForegroundColor Green
Write-Host ""
Read-Host "Press Enter to exit"
