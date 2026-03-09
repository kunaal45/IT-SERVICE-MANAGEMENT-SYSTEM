# ✅ FIX POWERSHELL ISSUE - RUN YOUR PROJECT

## 🎯 THE PROBLEM

You're using **PowerShell** which doesn't recognize `mvnw.cmd` directly.

**Error you got:**
```
mvnw.cmd : The term 'mvnw.cmd' is not recognized as the name of a cmdlet
```

---

## ✅ THE SOLUTION (3 OPTIONS)

### **Option 1: Use `.\` prefix (RECOMMENDED)**

**In PowerShell, run:**
```powershell
.\mvnw.cmd spring-boot:run
```

The dot-slash `.\` tells PowerShell to run the file in the current directory.

**This is the easiest fix!**

---

### **Option 2: Switch to Command Prompt (CMD)**

**Instead of PowerShell, use CMD:**

1. Close PowerShell
2. Press **Windows + R**
3. Type: `cmd`
4. Press **Enter**
5. Navigate to project: `cd c:\Users\kunaa\IdeaProjects\itsm-system`
6. Run: `mvnw.cmd spring-boot:run`

**Command Prompt recognizes `mvnw.cmd` directly!**

---

### **Option 3: Use Maven instead (if installed)**

**If Maven is installed globally:**
```powershell
mvn spring-boot:run
```

---

## 🚀 RECOMMENDED: USE OPTION 1

**Just add `.\` before the command:**

```powershell
.\mvnw.cmd spring-boot:run
```

---

## ✅ WHAT TO EXPECT AFTER RUNNING

```
[INFO] Scanning for projects...
[INFO] 
[INFO] Building itsm-system 0.0.1-SNAPSHOT
[INFO]
[INFO] *** DOWNLOADING DEPENDENCIES ***
[INFO] Downloading: mysql-connector-j...
... (more downloads)...
[INFO]
[INFO] *** STARTING APPLICATION ***
[INFO] Started ItsmSystemApplication in X.XXX seconds
[INFO] Tomcat started on port(s): 8080
```

When you see: `Tomcat started on port(s): 8080` ✅

**Open browser:** `http://localhost:8080/index.html`

---

## 📋 COMPLETE STEPS

### Step 1: Make Sure You're In Project Root
```powershell
cd c:\Users\kunaa\IdeaProjects\itsm-system
```

**Verify** you see files like `mvnw.cmd`, `pom.xml`, `src/`

### Step 2: Run with `.\` prefix
```powershell
.\mvnw.cmd spring-boot:run
```

### Step 3: Wait for Startup
```
Look for: Tomcat started on port(s): 8080
```

### Step 4: Open Browser
```
http://localhost:8080/index.html
```

### Step 5: Login
```
admin@example.com / adminpass
```

**Done!** 🎉

---

## 🆘 IF STILL HAVING ISSUES

**Option A: Use Command Prompt (CMD) instead**
```
Press Windows + R
Type: cmd
Press Enter
cd c:\Users\kunaa\IdeaProjects\itsm-system
mvnw.cmd spring-boot:run
```

**Option B: Use batch file I created**
```
Double-click: run.bat
```

This will automatically run the project for you.

---

## 🎊 QUICK REFERENCE

| Shell | Command |
|-------|---------|
| **PowerShell** | `.\mvnw.cmd spring-boot:run` |
| **Command Prompt (CMD)** | `mvnw.cmd spring-boot:run` |
| **Batch File** | Double-click `run.bat` |

---

## ✨ SUMMARY

**Problem:** PowerShell doesn't recognize `mvnw.cmd`

**Solution:** Add `.\` before the command

**Command:** `.\mvnw.cmd spring-boot:run`

**That's it!** 🚀

---

**Try it now and your project will start!** ✅
