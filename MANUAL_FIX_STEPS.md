# 🛠️ MANUAL FIX - Step by Step

Since MySQL is not in your PATH, follow these manual steps:

---

## ⚡ QUICK FIX (3 steps, 2 minutes)

### Step 1: Fix Database Using MySQL Workbench

1. **Open MySQL Workbench**
2. **Connect** to your local MySQL (root / 2005)
3. **Click** on "Query" tab or press Ctrl+T
4. **Copy and paste** this SQL:

```sql
USE itsm_db;
ALTER TABLE users MODIFY COLUMN role VARCHAR(20) NOT NULL;
SELECT 'Database Fixed!' AS Status;
```

5. **Click** the ⚡ lightning bolt icon (or press Ctrl+Enter) to execute
6. **Verify** you see: "Database Fixed!" in the results

---

### Step 2: Clean Old Compiled Files

In PowerShell, run:
```powershell
.\mvnw.cmd clean
```

Wait for: `BUILD SUCCESS`

---

### Step 3: Start the Application

In PowerShell, run:
```powershell
.\mvnw.cmd spring-boot:run
```

Wait for: `Started ItsmSystemApplication`

---

## ✅ Success!

If you see:
```
Started ItsmSystemApplication in X.XXX seconds
```

Your application is running on: **http://localhost:8080** 🎉

---

## 🆘 Alternative: One-Command Fix

If you prefer, run the PowerShell script:
```powershell
.\auto-fix.ps1
```

It will pause and ask you to fix the database manually, then continue automatically.

---

## 📋 What These Steps Do

1. **Database Fix**: Expands the `role` column to fit "ENGINEER" and "FACULTY" values
2. **Clean**: Removes old `.class` files with incorrect package declarations  
3. **Run**: Recompiles everything with correct packages and starts the app

---

## 🎯 Test Login After Startup

**Default Users:**
- Admin: `admin@college.edu` / `admin123`
- Engineer: `priya@college.edu` / `eng123`
- Faculty: `rajesh@college.edu` / `faculty123`

---

**Estimated Time**: 2 minutes  
**Difficulty**: Easy ⭐

Good luck! 🍀
