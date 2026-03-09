═══════════════════════════════════════════════════════════════════
                    QUICK FIX INSTRUCTIONS
═══════════════════════════════════════════════════════════════════

YOUR APPLICATION HAS 2 ERRORS. HERE'S THE FIX:

───────────────────────────────────────────────────────────────────
STEP 1: Fix Database (use MySQL Workbench)
───────────────────────────────────────────────────────────────────

1. Open MySQL Workbench
2. Connect to: localhost (root / 2005)
3. Open file: FIX_DATABASE.sql
4. Click Execute (⚡ lightning icon)
5. Wait for "Database schema fixed successfully!" message

───────────────────────────────────────────────────────────────────
STEP 2: Start Application (use PowerShell)
───────────────────────────────────────────────────────────────────

Open PowerShell in this folder and run:

    .\mvnw.cmd clean

Then run:

    .\mvnw.cmd spring-boot:run

OR use the batch file:

    .\START_APP.bat

───────────────────────────────────────────────────────────────────
ALTERNATIVE: PowerShell Script
───────────────────────────────────────────────────────────────────

Run in PowerShell:

    .\auto-fix.ps1

(It will pause for you to fix the database, then continue)

───────────────────────────────────────────────────────────────────
EXPECTED RESULT
───────────────────────────────────────────────────────────────────

You should see:

    Started ItsmSystemApplication in X.XXX seconds

Then open browser: http://localhost:8080

───────────────────────────────────────────────────────────────────
DEFAULT LOGIN CREDENTIALS
───────────────────────────────────────────────────────────────────

Admin:    admin@college.edu    / admin123
Engineer: priya@college.edu    / eng123
Faculty:  rajesh@college.edu   / faculty123

═══════════════════════════════════════════════════════════════════
                        GOOD LUCK! 🍀
═══════════════════════════════════════════════════════════════════

For detailed explanation, see: MANUAL_FIX_STEPS.md
