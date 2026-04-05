# run.ps1 - Loads .env and starts the Spring Boot application
# Usage: powershell -ExecutionPolicy Bypass -File run.ps1

Write-Host "============================================" -ForegroundColor Cyan
Write-Host "  ITSM System - Loading .env & Starting App" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan

# Load .env into current process environment
Get-Content ".env" | Where-Object { $_ -notmatch "^#" -and $_ -match "=" } | ForEach-Object {
    $parts = $_ -split "=", 2
    $key   = $parts[0].Trim()
    $value = $parts[1].Trim()
    [System.Environment]::SetEnvironmentVariable($key, $value, "Process")
    Write-Host "  Loaded: $key" -ForegroundColor Green
}

Write-Host ""
Write-Host "Starting Spring Boot on http://localhost:8080 ..." -ForegroundColor Yellow
Write-Host ""

.\mvnw.cmd spring-boot:run
