# 🚀 ITSM System - Quick Startup Checklist

## Pre-Run Checks (5 minutes)

- [ ] JDK 17 is installed
  ```cmd
  java -version
  ```
  Should show: `java version "17.x.x"`

- [ ] Maven is available
  ```cmd
  mvn -version
  ```
  (or use `mvnw.cmd` if Maven not installed)

- [ ] Port 8080 is free
  ```cmd
  netstat -ano | findstr :8080
  ```
  Should return nothing (or change port in application.properties)

- [ ] Navigate to project root
  ```cmd
  cd c:\Users\kunaa\IdeaProjects\itsm-system
  ```

## Run Application

### Copy & Paste This Command:
```cmd
mvnw.cmd spring-boot:run
```

### Wait for this message:
```
Started ItsmSystemApplication in X.XXX seconds
```

## Access Frontend

Once you see "Started ItsmSystemApplication", open in browser:
```
http://localhost:8080/index.html
```

## Test Login

Use any of these credentials:

| User Type | Email | Password |
|-----------|-------|----------|
| Admin | admin@example.com | adminpass |
| Engineer | engineer@example.com | engineerpass |
| Student | student@example.com | studentpass |

## Expected Behavior

✅ After login, you should be redirected to a dashboard
✅ Dashboard should load tickets from API
✅ You can create, view, and manage tickets
✅ All pages load from `http://localhost:8080/`

## If Something Goes Wrong

1. **Port in use?** Change `server.port=8080` to `8081` in `src/main/resources/application.properties`
2. **Java not found?** Install JDK 17
3. **Maven error?** Use `mvnw.cmd` instead of `mvn`
4. **API 404 error?** Ensure backend is running (check console for "Started ItsmSystemApplication")
5. **Login fails?** Use credentials from above, check browser console (F12)

## Quick API Test (Optional)

```cmd
curl http://localhost:8080/api/tickets
```

Should return a JSON array of tickets.

---

**Status: ✅ READY TO RUN**

Start the server now! 🚀
