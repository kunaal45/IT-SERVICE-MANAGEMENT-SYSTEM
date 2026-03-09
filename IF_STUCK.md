# ✅ PROJECT STUCK - QUICK FIXES

## 🎯 WHAT TO DO IF PROJECT IS STUCK

### Option 1: Force Stop & Clean (RECOMMENDED)

**Press:** `Ctrl+C` in PowerShell to stop the current process

Then run:
```powershell
.\mvnw.cmd clean
```

Then run:
```powershell
.\mvnw.cmd spring-boot:run
```

---

### Option 2: Skip Tests (Faster)

```powershell
.\mvnw.cmd spring-boot:run -DskipTests
```

This skips running tests and just starts the app.

---

### Option 3: Offline Mode (If Internet Issues)

```powershell
.\mvnw.cmd spring-boot:run -o
```

---

### Option 4: Use Command Prompt Instead of PowerShell

Close PowerShell and open Command Prompt (CMD):

1. Press `Windows + R`
2. Type: `cmd`
3. Press `Enter`
4. Run:
```cmd
cd c:\Users\kunaa\IdeaProjects\itsm-system
mvnw.cmd spring-boot:run
```

---

## ⏱️ TYPICAL DOWNLOAD TIME

**First run:** 5-10 minutes (downloading ~150 MB of libraries)
**Subsequent runs:** 30-60 seconds (no downloads)

If it's been stuck for **more than 15 minutes**, try Option 1 or 4.

---

## 🔍 WHAT TO LOOK FOR

While running, you should see:
```
[INFO] Downloading...
[INFO] Downloaded...
[INFO] Compiling...
[INFO] Building...
[INFO] Started ItsmSystemApplication...
[INFO] Tomcat started on port(s): 8080
```

If you see **none of these messages** for 5+ minutes, it's stuck.

---

## ✅ AFTER IT WORKS

Once you see:
```
Tomcat started on port(s): 8080
```

Open browser:
```
http://localhost:8080/index.html
```

---

**Try one of these fixes now!** 🚀
