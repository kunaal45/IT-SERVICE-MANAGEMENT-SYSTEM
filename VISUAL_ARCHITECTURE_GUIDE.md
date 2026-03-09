# 🎨 VISUAL ARCHITECTURE - Demo Tickets System

## 🏗️ SYSTEM ARCHITECTURE

```
┌─────────────────────────────────────────────────────────────────────────┐
│                         ITSM PORTAL SYSTEM                              │
│                     (With Demo Tickets Enabled)                         │
└─────────────────────────────────────────────────────────────────────────┘

┌──────────────────────────────┐
│   FRONTEND (HTML/JS/CSS)     │
├──────────────────────────────┤
│ ✅ index.html (Login Page)   │
│ ✅ admin-dashboard.html      │
│ ✅ engineer-dashboard.html   │
│ ✅ faculty-dashboard.html    │
│ ✅ app.js (API Calls)        │
└────────────┬─────────────────┘
             │ HTTP/REST
             │ fetchAllTickets()
             │ fetchMyTickets()
             │
             ▼
┌──────────────────────────────┐
│  SPRING BOOT REST API        │
├──────────────────────────────┤
│ ✅ TicketApiController       │
│    - GET /api/tickets        │
│    - GET /api/tickets/{id}   │
│    - POST /assign/{id}/{eng} │
│    - PUT /update/{id}/status │
│                              │
│ ✅ AuthController           │
│    - POST /api/auth/login    │
│    - GET /api/user/info      │
└────────────┬─────────────────┘
             │ JPA Query
             │
             ▼
┌──────────────────────────────┐
│  SERVICE LAYER               │
├──────────────────────────────┤
│ ✅ TicketService             │
│    - getAllTickets()         │
│    - getTicketsCreatedBy()   │
│    - getTicketsAssignedTo()  │
│    - updateTicketStatus()    │
│                              │
│ ✅ UserService               │
│    - login()                 │
│    - getUserById()           │
└────────────┬─────────────────┘
             │ Repository
             │
             ▼
┌──────────────────────────────┐
│  DATA ACCESS LAYER           │
├──────────────────────────────┤
│ ✅ TicketRepository          │
│    - findAll()               │
│    - findById()              │
│    - findByStatus()          │
│    - findByAssignedToId()    │
│    - findByCreatedById()     │
│                              │
│ ✅ UserRepository            │
│    - findByEmail()           │
│    - findById()              │
└────────────┬─────────────────┘
             │ SQL
             │
             ▼
┌──────────────────────────────┐
│   MySQL DATABASE             │
├──────────────────────────────┤
│ 📊 users (7 records)         │
│ 📋 tickets (5 records)       │
│ 💬 comments                  │
│ 📝 audit_logs                │
└──────────────────────────────┘
```

---

## 📊 DATA FLOW DIAGRAM

### **Login Flow**

```
User Opens Portal
       │
       ▼
Enter Credentials
├─ Email: admin@college.edu
├─ Password: admin123
       │
       ▼
Click "Sign In"
       │
       ▼
POST /api/auth/login
       │
       ▼
AuthController.login()
       │
       ▼
UserService.login()
├─ Find user by email
├─ Verify BCrypt password
├─ Generate JWT token
       │
       ▼
Return LoginResponse
├─ token
├─ email
├─ name
├─ role
       │
       ▼
Store in LocalStorage
       │
       ▼
Redirect to Dashboard
├─ ADMIN → admin-dashboard.html
├─ ENGINEER → engineer-dashboard.html
└─ FACULTY → faculty-dashboard.html
```

### **View Tickets Flow**

```
User Opens Dashboard
       │
       ▼
DOMContentLoaded Event
       │
       ▼
Call loadTickets() Function
       │
       ▼
fetch('/api/tickets')
       │
       ▼
TicketApiController.getAllTickets()
       │
       ▼
TicketService.getAllTickets()
       │
       ▼
TicketRepository.findAll()
       │
       ▼
MySQL: SELECT * FROM tickets
       │
       ▼
Return 5 Ticket Objects
       │
       ▼
Format as JSON
       │
       ▼
Return to Frontend
       │
       ▼
JavaScript Loops Through Tickets
       │
       ▼
Create HTML Table Rows
       │
       ▼
Insert into DOM
       │
       ▼
Dashboard Displays Tickets
```

---

## 🔄 TICKET LIFECYCLE

```
┌─────────────────────────────────────────────────────────────┐
│                    TICKET LIFECYCLE                          │
└─────────────────────────────────────────────────────────────┘

STEP 1: CREATION (Faculty)
┌──────────────────────────────────────┐
│ Faculty fills "Create Issue" form    │
│ - Title: "Lab PC-05 monitor broken"  │
│ - Category: HARDWARE                 │
│ - Priority: HIGH                     │
│ - Description: (detailed issue)      │
│ - Location: CSE Lab 1                │
│                                      │
│ Status: 🔵 OPEN                      │
│ Assigned: Nobody yet                 │
└──────┬───────────────────────────────┘
       │
       ▼
STEP 2: ADMIN ASSIGNMENT
┌──────────────────────────────────────┐
│ Admin views in Global Ticket Queue   │
│ - Sees: Lab PC-05 (OPEN)             │
│ - Clicks: Assign button              │
│ - Selects: Mr. Kumar (Engineer)      │
│                                      │
│ Status: 🟣 ASSIGNED                  │
│ Assigned: Mr. Kumar                  │
└──────┬───────────────────────────────┘
       │
       ▼
STEP 3: ENGINEER STARTS WORK
┌──────────────────────────────────────┐
│ Engineer sees in "My Assigned"        │
│ - Views ticket details               │
│ - Clicks: "Update Status"            │
│ - Changes to: IN_PROGRESS            │
│ - Adds: "Checking HDMI cable"        │
│                                      │
│ Status: 🟠 IN_PROGRESS               │
│ Assigned: Mr. Kumar                  │
└──────┬───────────────────────────────┘
       │
       ▼
STEP 4: ENGINEER WORKS & UPDATES
┌──────────────────────────────────────┐
│ Engineer adds progress comments      │
│ - "Replaced faulty monitor"          │
│ - "Testing with new unit"            │
│ - "Hardware working now"             │
│                                      │
│ Status: 🟠 IN_PROGRESS (updated)     │
│ Assigned: Mr. Kumar                  │
└──────┬───────────────────────────────┘
       │
       ▼
STEP 5: ENGINEER RESOLVES
┌──────────────────────────────────────┐
│ Engineer marks complete              │
│ - Changes status: RESOLVED           │
│ - Final notes: "Ticket resolved"     │
│ - Time taken: 2 hours                │
│                                      │
│ Status: ✅ RESOLVED                  │
│ Assigned: Mr. Kumar (Completed)      │
└──────┬───────────────────────────────┘
       │
       ▼
STEP 6: FACULTY SEES RESOLUTION
┌──────────────────────────────────────┐
│ Faculty notified (in dashboard)      │
│ - Ticket status changed to RESOLVED  │
│ - Can view engineer's work           │
│ - Can see resolution notes           │
│                                      │
│ Status: ✅ RESOLVED                  │
│ Issue: CLOSED (Resolved)             │
└──────────────────────────────────────┘
```

---

## 👥 ROLE-BASED VIEW

```
┌────────────────────────────────────────────────────────────┐
│                      5 DEMO TICKETS                        │
└────────────────────────────────────────────────────────────┘

ADMIN VIEW: Can See ALL 5 Tickets
┌────────────────────────────────────────┐
│ 1. Lab PC-05 monitor         ASSIGNED  │
│ 2. WiFi intermittent      IN_PROGRESS  │
│ 3. Portal login error          OPEN    │
│ 4. Projector not displaying RESOLVED   │
│ 5. Need additional chairs    PENDING   │
│                                        │
│ Can: Assign, Update, View All         │
└────────────────────────────────────────┘

ENGINEER VIEW: Can See ASSIGNED to Me
┌────────────────────────────────────────┐
│ As Mr. Kumar:                          │
│ 1. Lab PC-05 monitor         ASSIGNED  │
│ 4. Projector not displaying RESOLVED   │
│                                        │
│ As Ms. Priya:                          │
│ 2. WiFi intermittent      IN_PROGRESS  │
│                                        │
│ Can: Update Status, Add Comments       │
└────────────────────────────────────────┘

FACULTY VIEW: Can See CREATED by Me
┌────────────────────────────────────────┐
│ As Dr. Rajesh:                         │
│ 1. Lab PC-05 monitor         ASSIGNED  │
│                                        │
│ As Prof. Anitha:                       │
│ 2. WiFi intermittent      IN_PROGRESS  │
│ 5. Need additional chairs    PENDING   │
│                                        │
│ As Dr. Suresh:                         │
│ 3. Portal login error          OPEN    │
│ 4. Projector not displaying RESOLVED   │
│                                        │
│ Can: Track, Create, Comment            │
└────────────────────────────────────────┘
```

---

## 📱 DASHBOARD STRUCTURE

### **Admin Dashboard Layout**

```
┌─────────────────────────────────────────────────────────────┐
│                    ADMIN COMMAND CENTER                     │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  Statistics Row:                                            │
│  ┌──────────────┐ ┌──────────────┐ ┌─────────────────┐    │
│  │ SLA Breaches │ │ Unassigned   │ │ Other Stats     │    │
│  │      0       │ │      2       │ │                 │    │
│  └──────────────┘ └──────────────┘ └─────────────────┘    │
│                                                             │
│  Main Content:                                              │
│  ┌─────────────────────────────────────────────────────┐   │
│  │ Global Ticket Queue Table                           │   │
│  ├──────┬──────────────┬──────────┬─────────┬──────┬───┤   │
│  │ ID   │ Description  │ Priority │ Status  │ Asgn │Act│   │
│  ├──────┼──────────────┼──────────┼─────────┼──────┼───┤   │
│  │  1   │ Lab PC-05... │ HIGH     │ ASSIGNED│Kumar │V  │   │
│  │  2   │ WiFi...      │ MEDIUM   │ IN_PROG │Priya │V  │   │
│  │  3   │ Portal...    │ HIGH     │ OPEN    │None  │V  │   │
│  │  4   │ Projector... │ HIGH     │ RESOLVED│Kumar │V  │   │
│  │  5   │ Chairs...    │ MEDIUM   │ PENDING │None  │V  │   │
│  └──────┴──────────────┴──────────┴─────────┴──────┴───┘   │
│                                                             │
│  Sidebar (Right):                                           │
│  - Ticket Volume Chart                                      │
│  - Recent Activity Log                                      │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### **Engineer Dashboard Layout**

```
┌─────────────────────────────────────────────────────────────┐
│                  ENGINEER DASHBOARD                         │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  Header:                                                    │
│  Welcome, Ms. Priya (Network Engineer)                     │
│                                                             │
│  My Assigned Tickets:                                       │
│  ┌──────┬──────────────┬─────────┬──────┬──────────────┐   │
│  │ ID   │ Title        │ Status  │ SLA  │ Actions      │   │
│  ├──────┼──────────────┼─────────┼──────┼──────────────┤   │
│  │  2   │ WiFi intermit│IN_PROG  │2 hrs │[Update] [V] │   │
│  └──────┴──────────────┴─────────┴──────┴──────────────┘   │
│                                                             │
│  My Completed Work:                                         │
│  ┌──────┬──────────────┬─────────┐                         │
│  │ ID   │ Title        │ Status  │                         │
│  ├──────┼──────────────┼─────────┤                         │
│  │  (none yet)                   │                         │
│  └───────────────────────────────┘                         │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### **Faculty Dashboard Layout**

```
┌─────────────────────────────────────────────────────────────┐
│               FACULTY SUPPORT PORTAL                        │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  Statistics:                                                │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐        │
│  │ Total Raised │ │ In Progress  │ │ Resolved     │        │
│  │      1       │ │      1       │ │      0       │        │
│  └──────────────┘ └──────────────┘ └──────────────┘        │
│                                                             │
│  Create New Issue Form:                                     │
│  ┌─────────────────────────────────────────────────────┐   │
│  │ Title:        [______________]                     │   │
│  │ Category:     [HARDWARE ▼]                         │   │
│  │ Priority:     [HIGH ▼]                             │   │
│  │ Location:     [______________]                     │   │
│  │ Description:  [_________________]                  │   │
│  │                               [Submit Button]      │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  Recent Issues:                                             │
│  ┌──────┬──────────────┬──────────┬─────────┬──────────┐   │
│  │ ID   │ Title        │ Category │ Status  │ Engineer │   │
│  ├──────┼──────────────┼──────────┼─────────┼──────────┤   │
│  │  1   │ Lab PC-05... │ HARDWARE │ ASSIGNED│ Mr. Kumar│   │
│  └──────┴──────────────┴──────────┴─────────┴──────────┘   │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## 🗄️ DATABASE SCHEMA

```
┌─────────────────────────────────────────────────────────────┐
│                      DATABASE: itsm_db                      │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────┐
│      users          │
├─────────────────────┤
│ id (PK)             │
│ email (UNIQUE)      │
│ name                │
│ password (BCrypt)   │
│ role                │
│ created_at          │
└─────────────────────┘
  │
  └─────────┬─────────┐
            │         │
            ▼         ▼
┌──────────────────────┐  ┌──────────────────────┐
│ tickets              │  │ comments             │
├──────────────────────┤  ├──────────────────────┤
│ id (PK)              │  │ id (PK)              │
│ title                │  │ ticket_id (FK)       │
│ description          │  │ user_id (FK)         │
│ status               │  │ content              │
│ priority             │  │ created_at           │
│ category             │  └──────────────────────┘
│ created_by_id (FK)   │
│ assigned_to_id (FK)  │
│ created_at           │
│ updated_at           │
└──────────────────────┘

Record Count:
- users: 7 (1 admin, 3 engineers, 3 faculty)
- tickets: 5 (different statuses)
- comments: Linked to tickets
- audit_logs: Track all actions
```

---

## 🔐 Security Architecture

```
┌────────────────────────────────────┐
│     LOGIN SECURITY                  │
├────────────────────────────────────┤
│                                    │
│ Plain Password:                    │
│ "admin123"                         │
│        │                           │
│        ▼                           │
│ BCryptPasswordEncoder.encode()     │
│        │                           │
│        ▼                           │
│ Stored as Hash:                    │
│ "$2a$10$..."                       │
│        │                           │
│        ▼ (during login)            │
│ User enters: "admin123"            │
│        │                           │
│        ▼                           │
│ BCryptPasswordEncoder.matches()    │
│        │                           │
│        ▼                           │
│ ✓ Valid → Generate JWT             │
│ ✗ Invalid → Reject                │
│                                    │
└────────────────────────────────────┘

JWT TOKEN FLOW:
┌────────────────┐
│ Login Success  │
└───────┬────────┘
        │
        ▼
┌──────────────────────────────────┐
│ Generate JWT Token               │
│ Header.Payload.Signature         │
└───────┬──────────────────────────┘
        │
        ▼
┌──────────────────────────────────┐
│ Store in LocalStorage            │
│ localStorage.getItem('itsm_token')
└───────┬──────────────────────────┘
        │
        ▼
┌──────────────────────────────────┐
│ Use in API Calls                 │
│ Authorization: Bearer {token}    │
└──────────────────────────────────┘
```

---

## 📈 STATISTICS FLOW

```
Load Dashboard
        │
        ▼
Fetch All Tickets
├─ SELECT * FROM tickets
        │
        ▼
JavaScript Processes Data
├─ Count SLA Breached tickets
├─ Count Unassigned tickets
├─ Count Open tickets
├─ Count In Progress tickets
├─ Count Resolved tickets
        │
        ▼
Update Statistics Cards
├─ SLA Breaches: X
├─ Unassigned: X
├─ Open: X
├─ Resolved: X
        │
        ▼
Display Dashboard with Stats
```

---

## 🎯 DATA RELATIONSHIPS

```
Dr. Rajesh Kumar (Faculty)
    │
    └─── Creates ──→ Ticket #1 (Lab PC-05)
                          │
                          └─── Assigned to ──→ Mr. Kumar (Engineer)
                                                    │
                                                    └─── Works on ──→ Status changes

Prof. Anitha M (Faculty)
    │
    ├─── Creates ──→ Ticket #2 (WiFi)
    │                    │
    │                    └─── Assigned to ──→ Ms. Priya (Engineer)
    │
    └─── Creates ──→ Ticket #5 (Chairs)
                        │
                        └─── Awaits Approval ──→ Admin

Dr. Suresh T (Faculty)
    │
    ├─── Creates ──→ Ticket #3 (Portal Error)
    │                    │
    │                    └─── Unassigned (Waiting)
    │
    └─── Creates ──→ Ticket #4 (Projector)
                        │
                        └─── Assigned to ──→ Mr. Kumar
                             (Already Resolved)

Admin (admin@college.edu)
    │
    └─── Manages ──→ All Tickets
                     ├─ Assign engineers
                     ├─ Monitor SLA
                     └─ Approve requests
```

---

## ✅ THIS VISUAL GUIDE SHOWS:

✅ System architecture and components
✅ Data flow through the application
✅ Ticket lifecycle from creation to resolution
✅ Role-based dashboard views
✅ Database structure and relationships
✅ Security implementation
✅ Statistics calculation flow

**Everything is interconnected and working together!** 🎉
