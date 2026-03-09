# ITSM System - Architecture Overview

## 🏗️ System Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                         WEB BROWSER                              │
│  http://localhost:8080/index.html                               │
│                                                                   │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │                   FRONTEND (Static)                     │    │
│  │  ┌──────────────────────────────────────────────────┐   │    │
│  │  │ HTML Pages (Tailwind CSS + Material Icons)      │   │    │
│  │  │ • index.html (Login)                            │   │    │
│  │  │ • admin_dashboard.html                          │   │    │
│  │  │ • engineer_dashboard.html                       │   │    │
│  │  │ • student_dashboard.html                        │   │    │
│  │  │ • tickets_list.html                             │   │    │
│  │  │ • ticket_details.html                           │   │    │
│  │  │ • audit_logs.html                               │   │    │
│  │  │ • settings.html                                 │   │    │
│  │  └──────────────────────────────────────────────────┘   │    │
│  │                                                          │    │
│  │  ┌──────────────────────────────────────────────────┐   │    │
│  │  │   app.js (JavaScript API Client)                │   │    │
│  │  │   • Authentication (login/logout)               │   │    │
│  │  │   • Ticket management (CRUD)                    │   │    │
│  │  │   • Comments & audit logs                       │   │    │
│  │  │   • SLA management                              │   │    │
│  │  │   • HTTP calls via Fetch API                    │   │    │
│  │  └──────────────────────────────────────────────────┘   │    │
│  └─────────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ HTTP/REST (JSON)
                              │ API_URL = /api
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                    SPRING BOOT BACKEND                           │
│                    Port: 8080                                    │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │           REST Controllers (6)                         │    │
│  │  ┌──────────────────────────────────────────────────┐   │    │
│  │  │ 1. AuthController                               │   │    │
│  │  │    POST /api/auth/login                         │   │    │
│  │  ├──────────────────────────────────────────────────┤   │    │
│  │  │ 2. UserController                               │   │    │
│  │  │    GET /api/users/engineers                     │   │    │
│  │  ├──────────────────────────────────────────────────┤   │    │
│  │  │ 3. TicketController                             │   │    │
│  │  │    GET/POST /api/tickets                        │   │    │
│  │  │    GET /api/tickets/{id}                        │   │    │
│  │  │    PUT /api/tickets/{id}/assign                 │   │    │
│  │  │    PUT /api/tickets/{id}/status                 │   │    │
│  │  │    PUT /api/tickets/{id}/resolve                │   │    │
│  │  │    PUT /api/tickets/{id}/close                  │   │    │
│  │  │    GET /api/tickets/stats                       │   │    │
│  │  ├──────────────────────────────────────────────────┤   │    │
│  │  │ 4. CommentController                            │   │    │
│  │  │    POST /api/comments                           │   │    │
│  │  │    GET /api/comments/ticket/{id}               │   │    │
│  │  ├──────────────────────────────────────────────────┤   │    │
│  │  │ 5. AuditController                              │   │    │
│  │  │    GET /api/audit/logs                          │   │    │
│  │  │    GET /api/audit/ticket/{id}                  │   │    │
│  │  ├──────────────────────────────────────────────────┤   │    │
│  │  │ 6. SLAController                                │   │    │
│  │  │    GET /api/sla                                 │   │    │
│  │  │    POST /api/sla                                │   │    │
│  │  └──────────────────────────────────────────────────┘   │    │
│  └─────────────────────────────────────────────────────────┘    │
│                                                                   │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │          Model & Entity Classes                        │    │
│  │  • User (id, name, email, role)                       │    │
│  │  • Ticket (id, title, status, priority, SLA, etc)     │    │
│  │  • Comment (id, content, user, timestamp)             │    │
│  │  • AuditLog (id, action, user, details)               │    │
│  └─────────────────────────────────────────────────────────┘    │
│                                                                   │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │          Data Storage (In-Memory)                      │    │
│  │  • LinkedHashMap<Long, Ticket>                        │    │
│  │  • LinkedHashMap<Long, Comment>                       │    │
│  │  • LinkedHashMap<Long, AuditLog>                      │    │
│  │  • HashMap<String, User>                              │    │
│  │  • Map<String, SLARule>                               │    │
│  │                                                         │    │
│  │  ℹ️ NOTE: Data lost on app restart                     │    │
│  │           (Perfect for testing & demo)                │    │
│  └─────────────────────────────────────────────────────────┘    │
│                                                                   │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │          Configuration & Security                      │    │
│  │  • application.properties (port, datasource)          │    │
│  │  • SecurityConfig.java (CSRF disabled)                │    │
│  │  • CorsConfig.java (CORS enabled)                     │    │
│  │  • ItsmSystemApplication.java (main entry)            │    │
│  └─────────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────────┘
```

---

## 📊 Data Flow Diagram

### User Login Flow
```
User Opens Browser
       ↓
http://localhost:8080/index.html
       ↓
app.js: handleLogin(email, password)
       ↓
POST /api/auth/login
       ↓
AuthController.login()
       ↓
Check credentials in static map
       ↓
Return: { token, email, name, role }
       ↓
localStorage.setItem(itsm_token)
localStorage.setItem(itsm_user)
       ↓
redirectToDashboard(role)
       ↓
Load appropriate dashboard page
```

### Ticket Creation Flow
```
Student fills form → Create Ticket button
       ↓
app.js: handleCreateTicket(event)
       ↓
POST /api/tickets { title, description, priority }
       ↓
TicketController.createTicket()
       ↓
Create new Ticket object
Add to in-memory map
Generate new ID
       ↓
Return: Ticket object
       ↓
showSuccess("Ticket created!")
loadTickets() - refresh list
```

### Ticket Status Update Flow
```
Engineer clicks "Start Working"
       ↓
app.js: updateTicketStatus(ticketId, status)
       ↓
PUT /api/tickets/{id}/status?status=IN_PROGRESS
       ↓
TicketController.updateStatus()
       ↓
Get ticket from map
Update ticket.status
       ↓
Return 200 OK
       ↓
showSuccess("Status updated!")
loadTicketDetails() - refresh
```

---

## 🔄 Request-Response Cycle

### Example: Get All Tickets

**Client Side (app.js)**
```javascript
fetch('/api/tickets', {
    headers: getAuthHeaders()
})
.then(response => response.json())
.then(tickets => displayTickets(tickets))
```

**Server Side**
```
1. GET /api/tickets arrives at TicketController
2. @GetMapping → listTickets() method
3. Return ArrayList<Ticket>
4. Spring converts to JSON automatically
5. Client receives JSON array
6. JavaScript parses and renders HTML
```

**Network Request**
```
GET /api/tickets HTTP/1.1
Host: localhost:8080
Authorization: Bearer fake-jwt-token

---

HTTP/1.1 200 OK
Content-Type: application/json

[
  {
    "id": 1,
    "title": "Cannot access portal",
    "status": "OPEN",
    "priority": "HIGH",
    ...
  },
  ...
]
```

---

## 🔐 Authentication Flow (Simple)

```
┌─────────────┐
│   Browser   │
└──────┬──────┘
       │ User enters: admin@example.com / adminpass
       ↓
┌─────────────────────┐
│ app.js:handleLogin()│ POSTs credentials to /api/auth/login
└──────┬──────────────┘
       │
       ↓
┌──────────────────────────────┐
│ AuthController.login()       │
│ Checks static users map:     │
│ {                            │
│   "admin@example.com": {     │
│     password: "adminpass",   │
│     name: "Admin User",      │
│     role: "ADMIN"            │
│   }                          │
│ }                            │
└──────┬───────────────────────┘
       │
       ├─ Match found? ✓
       │
       ↓
┌──────────────────────────────┐
│ Return LoginResponse:        │
│ {                            │
│   token: "fake-jwt-...",     │
│   email: "admin@...",        │
│   name: "Admin User",        │
│   role: "ADMIN"              │
│ }                            │
└──────┬───────────────────────┘
       │
       ↓
┌────────────────────────┐
│ app.js: Save to storage│
│ localStorage.setItem() │
└──────┬─────────────────┘
       │
       ↓
┌──────────────────────────┐
│ redirectToDashboard()    │
│ → admin-dashboard.html   │
└──────────────────────────┘
```

---

## 📦 Dependency Flow

```
pom.xml (Maven)
   ↓
   ├── Spring Boot Starter Web MVC
   │   └── Provides: @RestController, @RequestMapping, etc.
   │
   ├── Spring Boot Starter Data JPA
   │   └── Provides: JPA interfaces (can be added later)
   │
   ├── Spring Boot Starter Security
   │   └── Provides: SecurityConfig, HttpSecurity
   │
   ├── Spring Boot Starter WebMVC
   │   └── Provides: Web MVC configuration
   │
   ├── MySQL Connector
   │   └── For future database integration
   │
   └── Lombok
       └── Optional: Code generation (not used in current code)
```

---

## 🗂️ Database Schema (When Migration to MySQL)

```
Users Table
├── id (Long, PK)
├── name (String)
├── email (String)
├── password (String, hashed)
└── role (String: ADMIN, SUPPORT_ENGINEER, STUDENT)

Tickets Table
├── id (Long, PK)
├── title (String)
├── description (Text)
├── priority (String: HIGH, MEDIUM, LOW)
├── status (String: OPEN, ASSIGNED, IN_PROGRESS, RESOLVED, CLOSED)
├── created_by_id (FK → Users)
├── assigned_to_id (FK → Users)
├── sla_deadline (Timestamp)
├── sla_breached (Boolean)
├── resolution_notes (Text)
└── created_at (Timestamp)

Comments Table
├── id (Long, PK)
├── ticket_id (FK → Tickets)
├── user_id (FK → Users)
├── content (Text)
└── created_at (Timestamp)

AuditLogs Table
├── id (Long, PK)
├── action (String)
├── user_id (FK → Users)
├── ticket_id (FK → Tickets)
├── details (Text)
└── created_at (Timestamp)

SLARule Table
├── id (Long, PK)
├── priority (String: HIGH, MEDIUM, LOW)
└── max_hours (Integer)
```

---

## 🔀 Role-Based Navigation

```
User Logs In
    ↓
What is user.role?
    │
    ├─ ADMIN
    │  └─ admin-dashboard.html
    │     ├─ View all tickets
    │     ├─ Assign tickets
    │     ├─ View SLA rules
    │     └─ View audit logs
    │
    ├─ SUPPORT_ENGINEER
    │  └─ engineer-dashboard.html
    │     ├─ View assigned tickets
    │     ├─ Update ticket status
    │     ├─ Resolve tickets
    │     └─ Add comments
    │
    └─ STUDENT
       └─ student-dashboard.html
          ├─ Create new tickets
          ├─ View my tickets
          ├─ Add comments
          └─ Track resolution
```

---

## ⚡ Performance & Scalability Notes

### Current (In-Memory)
- ✅ Fast (no DB latency)
- ✅ Perfect for development & demos
- ✅ Single JVM instance
- ⚠️ Limited to RAM size
- ⚠️ Data lost on restart

### Future (With MySQL)
- ✅ Persistent storage
- ✅ Scalable to multiple instances
- ✅ ACID compliance
- ⚠️ Network latency
- ⚠️ Requires DB setup

---

## 📞 Support & Documentation

| Document | Purpose |
|----------|---------|
| **START_HERE.md** | Quick overview & final checklist |
| **QUICK_START.md** | 5-minute setup guide |
| **RUN_INSTRUCTIONS.md** | Detailed run instructions |
| **PROJECT_STATUS.md** | Complete verification |
| **run.bat** | Automated startup script |

---

**Architecture designed for:** Simplicity + Quick Testing + Future Scalability

Version: 1.0 | Date: February 11, 2026
