# QUICK FIX COMMANDS - Copy and Paste

## Option 1: Run Auto-Fix Script (EASIEST)
```cmd
auto-fix.bat
```

## Option 2: Manual Commands (if auto-fix doesn't work)

### Step 1: Fix Database
```cmd
mysql -u root -p2005 -e "USE itsm_db; ALTER TABLE users MODIFY COLUMN role VARCHAR(20) NOT NULL;"
```

### Step 2: Clean and Run
```cmd
mvnw.cmd clean && mvnw.cmd spring-boot:run
```

## Option 3: If MySQL command not found

### Step 1: Open MySQL Workbench and run:
```sql
USE itsm_db;
ALTER TABLE users MODIFY COLUMN role VARCHAR(20) NOT NULL;
```

### Step 2: Then run in PowerShell/CMD:
```cmd
mvnw.cmd clean
mvnw.cmd spring-boot:run
```

---

## What These Commands Do:

1. **Database Fix**: Expands the `role` column from its current size to VARCHAR(20)
2. **mvnw.cmd clean**: Deletes all old compiled `.class` files
3. **mvnw.cmd spring-boot:run**: Recompiles everything and starts the application

---

## Expected Output (Success):

```
Started ItsmSystemApplication in X.XXX seconds
```

If you see this, the application is running successfully on http://localhost:8080 🎉
