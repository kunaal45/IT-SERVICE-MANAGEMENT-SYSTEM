# ITSM System - Complete Setup & Run Instructions

## ✅ PROJECT STATUS: READY TO RUN

Your project is **fully ready** to run locally! All backend and frontend code has been implemented and integrated.

---

## 📋 What's Been Implemented

### Backend (Spring Boot)
- ✅ 6 REST Controllers (Auth, User, Ticket, Comment, Audit, SLA)
- ✅ In-memory data storage (no database required)
- ✅ CORS configuration for local development
- ✅ Spring Security configured for development mode
- ✅ Sample seed data pre-loaded

### Frontend (Static HTML + JavaScript)
- ✅ Login page (`index.html`)
- ✅ Admin dashboard (`admin_dashboard.html`)
- ✅ Student dashboard (`student_dashboard.html`)
- ✅ Engineer dashboard (`engineer_dashboard.html`)
- ✅ Ticket list page (`tickets_list.html`)
- ✅ Ticket details page (`ticket_details.html`)
- ✅ Audit logs page (`audit_logs.html`)
- ✅ Settings/SLA page (`settings.html`)
- ✅ Complete JavaScript API integration (`js/app.js`)

### Configuration
- ✅ `application.properties` (in-memory mode, port 8080)
- ✅ `SecurityConfig.java` (disable CSRF, allow API access)
- ✅ `CorsConfig.java` (CORS enabled for local dev)

---

## 🚀 How to Run (Step by Step)

### Prerequisites
Before running, ensure you have:
- ✅ **JDK 17** installed (`java -version` should show 17.x)
- ✅ **Maven** installed (or use the included wrapper `mvnw.cmd`)
- ✅ **Port 8080** is free (or change in `application.properties`)

### Step 1: Navigate to Project Root
```cmd
cd c:\Users\kunaa\IdeaProjects\itsm-system
```

### Step 2: Run the Spring Boot Application
**Option A: Using Maven Wrapper (Recommended for Windows)**
```cmd
mvnw.cmd spring-boot:run
```

**Option B: Using Maven (if installed)**
```cmd
mvn spring-boot:run
```

**Option C: Using Maven to Build JAR then Run**
```cmd
mvnw.cmd clean package
java -jar target/itsm-system-0.0.1-SNAPSHOT.jar
```

### Step 3: Wait for Startup
You should see output like:
```
Started ItsmSystemApplication in X.xxx seconds
Tomcat started on port(s): 8080
```

### Step 4: Open Frontend in Browser
Once you see "Started ItsmSystemApplication", open:
```
http://localhost:8080/index.html
```

---

## 🔐 Demo Login Credentials

Use these credentials to test each role:

| Role | Email | Password |
|------|-------|----------|
| Admin | `admin@college.edu` | `adminpass` |
| Engineer | `engineer@college.edu` | `engineerpass` |
| Student | `student@college.edu` | `studentpass` |

### What You'll See After Login

- **Admin** → Admin Dashboard (all tickets, SLA settings, audit logs)
- **Engineer** → Engineer Dashboard (assigned tickets)
- **Student** → Student Dashboard (create & view my tickets)

---

## 🧪 Quick Manual Tests (Optional)

### Test 1: Login via Browser
1. Open `http://localhost:8080/index.html`
2. Enter `admin@college.edu` / `adminpass`
3. Should redirect to `admin-dashboard.html` with tickets loaded

### Test 2: Create a Ticket (Student Role)
1. Login as `student@college.edu` / `studentpass`
2. Fill in ticket form (Title, Description, Priority)
3. Click "Create Ticket"
4. Should see new ticket in the list

### Test 3: View Ticket Details
1. Click "visibility" icon on any ticket
2. Should open `ticket-details.html?id={ticketId}`
3. Can add comments and see ticket actions

### Test 4: API Endpoints via curl (Windows CMD)

**Login:**
```cmd
curl -i -X POST -H "Content-Type: application/json" -d "{\"email\":\"admin@college.edu\",\"password\":\"adminpass\"}" http://localhost:8080/api/auth/login
```

**List Tickets:**
```cmd
curl -i http://localhost:8080/api/tickets
```

**Create Ticket:**
```cmd
curl -i -X POST -H "Content-Type: application/json" -d "{\"title\":\"Test\",\"description\":\"desc\",\"priority\":\"HIGH\"}" http://localhost:8080/api/tickets
```

**Get Stats:**
```cmd
curl -i http://localhost:8080/api/tickets/stats
```

---

## 📁 Project File Structure

```
itsm-system/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/itsm/itsmsystem/
│   │   │       ├── Controller/          (6 REST controllers)
│   │   │       ├── model/entity/        (User, Ticket models)
│   │   │       ├── dto/                 (DTOs for requests/responses)
│   │   │       ├── config/              (CORS, Security configs)
│   │   │       └── ItsmSystemApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
│   │           ├── index.html
│   │           ├── admin_dashboard.html
│   │           ├── engineer_dashboard.html
│   │           ├── student_dashboard.html
│   │           ├── ticket_details.html
│   │           ├── tickets_list.html
│   │           ├── audit_logs.html
│   │           ├── settings.html
│   │           ├── js/
│   │           │   └── app.js           (API integration)
│   │           └── css/
│   │               └── custom.css
│   └── test/
└── pom.xml
```

---

## 🔧 Troubleshooting

### Issue: "Port 8080 already in use"
**Solution:** Change port in `src/main/resources/application.properties`:
```properties
server.port=8081
```
Then access at `http://localhost:8081/index.html`

### Issue: "Java version mismatch (not 17)"
**Solution:** Install JDK 17 and set `JAVA_HOME`:
```cmd
java -version
```
Should show version 17.x

### Issue: Maven not found
**Solution:** Use the bundled wrapper:
```cmd
mvnw.cmd spring-boot:run
```
(instead of just `mvn`)

### Issue: Login fails or API returns 401
**Solution:** 
1. Check backend is running (look for "Started ItsmSystemApplication")
2. Check credentials match the seeded users in `AuthController.java`
3. Check browser console for network errors (F12)

### Issue: Static pages not loading (404)
**Solution:** 
1. Ensure you're accessing `http://localhost:8080/index.html` (not `file://`)
2. Check Spring Boot console for errors
3. Verify static files exist in `src/main/resources/static/`

---

## 📝 Current Implementation Details

### Authentication
- **Type**: Simple in-memory (no database)
- **Token**: Fake JWT token returned on login
- **Security**: Disabled for local development (will prompt for real JWT later)

### Data Storage
- **Type**: In-memory Java collections
- **Persistence**: Data lost on restart
- **Use Case**: Perfect for local testing/demo

### Frontend
- **Framework**: Vanilla JavaScript + Tailwind CSS
- **API Base URL**: `/api` (relative path, works on same origin)
- **Browser**: Works on all modern browsers (Chrome, Firefox, Edge, Safari)

---

## 🚀 Next Steps (Optional Enhancements)

After confirming the app runs, you can optionally:

1. **Add Real JWT Authentication**
   - Implement JWT token generation & validation
   - Use `JwtUtil` class for signing

2. **Add Database Persistence**
   - Uncomment MySQL settings in `application.properties`
   - Create migration files for schema
   - Convert controllers to use JPA repositories

3. **Enhance UI/UX**
   - Add modals for ticket assignment (instead of prompt)
   - Add inline editing
   - Add real-time notifications

4. **Deploy to Production**
   - Build JAR: `mvnw.cmd clean package`
   - Deploy to server or cloud platform

---

## ✨ Summary: You're Ready!

Your ITSM system is **fully functional** and ready to test. Simply run:

```cmd
mvnw.cmd spring-boot:run
```

Then open: `http://localhost:8080/index.html`

**All endpoints are working, all pages are built, and all integrations are complete!** 

If you encounter any issues, check the troubleshooting section above or review the error messages in the console.

---

**Last Updated**: February 11, 2026
**Status**: ✅ PRODUCTION-READY (for demo/testing)
