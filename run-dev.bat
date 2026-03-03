@echo off
cd /d %~dp0
echo Starting itsm-system (logs -> app.log)...
echo URL: http://localhost:8080/
echo Credentials: admin / ChangeMe123!
REM Run using Maven wrapper in the project (foreground) so you can see logs in the terminal
.\mvnw.cmd spring-boot:run > app.log 2>&1
REM If you want to tail the log after starting, open a new terminal and run:
REM powershell -Command "Get-Content -Path .\app.log -Wait -Tail 200"
pause
