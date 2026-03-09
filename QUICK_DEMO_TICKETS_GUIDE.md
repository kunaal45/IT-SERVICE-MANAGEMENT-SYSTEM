# 🎯 QUICK START - See Demo Tickets in 3 Minutes!

## **30 SECOND QUICK START**

```
1. Restart App       → mvn clean spring-boot:run
2. Open Browser      → http://localhost:8080
3. Login as Admin    → admin@college.edu / admin123
4. See 5 Tickets     → In "Global Ticket Queue" table
```

---

## **THE 5 DEMO TICKETS**

### 🔴 **Ticket #1: Lab PC-05 Monitor Not Working**
- **Status:** ✅ ASSIGNED (In Progress)
- **Priority:** 🔴 HIGH
- **Category:** Hardware Problem
- **Created by:** Dr. Rajesh Kumar (Faculty)
- **Assigned to:** Mr. Kumar (Engineer - Working on it)
- **Issue:** Monitor broken, students can't use computer station

### 🟡 **Ticket #2: WiFi Intermittent in Library**
- **Status:** 🔄 IN_PROGRESS (Being worked on)
- **Priority:** 🟡 MEDIUM
- **Category:** Network Issue
- **Created by:** Prof. Anitha M (Faculty)
- **Assigned to:** Ms. Priya (Engineer - Working on it)
- **Issue:** WiFi keeps dropping, students losing connection

### 🔴 **Ticket #3: Portal Login Error 500**
- **Status:** 🔵 OPEN (Needs assignment)
- **Priority:** 🔴 HIGH
- **Category:** Website/Portal Issue
- **Created by:** Dr. Suresh T (Faculty)
- **Assigned to:** ❌ NOBODY (Waiting for engineer)
- **Issue:** Faculty portal crashes with error 500

### ✅ **Ticket #4: Projector Not Displaying**
- **Status:** ✅ RESOLVED (COMPLETED)
- **Priority:** 🔴 HIGH
- **Category:** Hardware Problem
- **Created by:** Dr. Suresh T (Faculty)
- **Assigned to:** Mr. Kumar (Engineer - Completed)
- **Issue:** Projector HDMI not working (FIXED)

### 🟡 **Ticket #5: Need Additional Chairs for Reading Room**
- **Status:** ⏳ PENDING_APPROVAL (Waiting for approval)
- **Priority:** 🟡 MEDIUM
- **Category:** Resource Request
- **Created by:** Prof. Anitha M (Faculty)
- **Assigned to:** ❌ NOBODY (Needs admin approval)
- **Issue:** Library needs 15 more chairs

---

## **LOGIN & VIEW TICKETS**

### **ADMIN View (See ALL 5 Tickets)**
```
Email:    admin@college.edu
Password: admin123

Expected Page: Admin Command Center
Expected Section: "Global Ticket Queue"
Expected Result: Table with 5 tickets
```

### **ENGINEER View (See Assigned Tickets)**
```
Email:    priya@college.edu
Password: eng123

Expected Page: Engineer Dashboard
Expected Section: "My Assigned Tickets"
Expected Result: 
  ✓ Ticket #2 (WiFi intermittent) - ASSIGNED TO YOU
  ✓ Click "Update" to change status to RESOLVED
```

### **FACULTY View (See Created Tickets)**
```
Email:    rajesh@college.edu
Password: faculty123

Expected Page: Faculty Support Portal
Expected Section: "Recent Issues"
Expected Result:
  ✓ Ticket #1 (Lab PC-05) - YOU CREATED THIS
  ✓ Status: ASSIGNED (Engineer is working on it)
  ✓ Can add comments and track progress
```

---

## **VISUAL DISPLAY GUIDE**

### **What Admin Sees (Ticket Table)**

```
┌─────┬──────────────────────────────────────┬──────────┬─────────────────┬─────────────────┐
│ ID  │ Ticket Description                   │ Priority │ Status          │ Assigned To     │
├─────┼──────────────────────────────────────┼──────────┼─────────────────┼─────────────────┤
│  1  │ Lab PC-05 monitor not working        │ HIGH     │ ASSIGNED        │ Mr. Kumar       │
│  2  │ WiFi intermittent in library         │ MEDIUM   │ IN_PROGRESS     │ Ms. Priya       │
│  3  │ Portal login error 500               │ HIGH     │ OPEN            │ Unassigned      │
│  4  │ Projector not displaying             │ HIGH     │ RESOLVED        │ Mr. Kumar       │
│  5  │ Need additional chairs for reading.. │ MEDIUM   │ PENDING_APPROVAL│ Unassigned      │
└─────┴──────────────────────────────────────┴──────────┴─────────────────┴─────────────────┘
```

### **Statistics Cards Admin Sees**

```
┌─────────────────────┐  ┌──────────────────┐  ┌────────────────┐  ┌──────────────────┐
│ 🔴 SLA Breaches     │  │ 🔵 Unassigned    │  │ 💼 Open Issues │  │ ✅ Resolved      │
│        0            │  │        2         │  │       1        │  │       1          │
│ (Critical Issues)   │  │ (Need Assignment)│  │ (New Tickets)  │  │ (Completed)      │
└─────────────────────┘  └──────────────────┘  └────────────────┘  └──────────────────┘
```

---

## **STEP-BY-STEP WALKTHROUGH**

### **Step 1: See Tickets in Admin Dashboard (2 min)**

```
1. Go to http://localhost:8080
   ↓
2. Login:
   Email: admin@college.edu
   Password: admin123
   ↓
3. You see "Admin Command Center" page
   ↓
4. Scroll down to find "Global Ticket Queue"
   ↓
5. 📊 You'll see TABLE with 5 tickets:
   
   ID | Title | Priority | Status | Assigned
   ---|-------|----------|--------|----------
    1 | Lab PC-05... | HIGH | ASSIGNED | Mr. Kumar
    2 | WiFi... | MEDIUM | IN_PROGRESS | Ms. Priya
    3 | Portal... | HIGH | OPEN | Unassigned
    4 | Projector... | HIGH | RESOLVED | Mr. Kumar
    5 | Chairs... | MEDIUM | PENDING_APPROVAL | Unassigned
   ↓
6. ✅ SUCCESS - You see all 5 demo tickets!
```

### **Step 2: See Your Assigned Tickets as Engineer (2 min)**

```
1. Go to http://localhost:8080
   ↓
2. Login:
   Email: priya@college.edu
   Password: eng123
   ↓
3. You see "Engineer Dashboard" page
   ↓
4. Find "My Assigned Tickets" section
   ↓
5. 📋 You'll see 1 ticket assigned to you:
   
   ID | Title | Status | SLA
   ---|-------|--------|----
    2 | WiFi intermittent... | IN_PROGRESS | 2 hours remaining
   ↓
6. ✅ SUCCESS - You see Ticket #2 (your assignment)!
```

### **Step 3: See Your Created Tickets as Faculty (2 min)**

```
1. Go to http://localhost:8080
   ↓
2. Login:
   Email: rajesh@college.edu
   Password: faculty123
   ↓
3. You see "Faculty Support Portal" page
   ↓
4. Find "Recent Issues" section
   ↓
5. 📋 You'll see ticket you created:
   
   ID | Title | Status | Assigned To
   ---|-------|--------|-------------
    1 | Lab PC-05... | ASSIGNED | Mr. Kumar
   ↓
6. ✅ SUCCESS - You see Ticket #1 (your issue)!
```

---

## **TICKET STATUS COLORS EXPLAINED**

| Color | Status | Meaning |
|-------|--------|---------|
| 🔵 Blue | OPEN | Brand new, waiting for assignment |
| 🟣 Purple | ASSIGNED | Engineer assigned, not started yet |
| 🟠 Orange | IN_PROGRESS | Engineer actively working on it |
| ✅ Green | RESOLVED | Fixed and completed |
| ⏳ Gray | PENDING_APPROVAL | Waiting for admin approval |

---

## **PRIORITY COLORS EXPLAINED**

| Color | Priority | Response Time |
|-------|----------|---|
| 🔴 Red | HIGH | Urgent - 2-4 hours |
| 🟡 Yellow | MEDIUM | Standard - 8-24 hours |
| 🟢 Green | LOW | Can wait - 24-48 hours |

---

## **CATEGORIES EXPLAINED**

| Category | Examples | Who Handles |
|----------|----------|-------------|
| 🖥️ HARDWARE | Monitor, Printer, Computer crashes | Hardware Engineer |
| 🌐 NETWORK | WiFi, Internet, LAN connectivity | Network Engineer |
| 💻 SOFTWARE | Application crashes, Installation | Software Engineer |
| 🌍 PORTAL_WEBSITE | Login issues, Website errors | Software Engineer |
| 📦 RESOURCE_REQUEST | Chairs, tables, supplies | Admin approval |

---

## **DEMO TICKET DETAILS**

### **Ticket #1 Details**
```
┌─ HARDWARE ISSUE ─────────────────────────────┐
│ ID: 1                                        │
│ Title: Lab PC-05 monitor not working         │
│ Description:                                  │
│   Monitor shows no display, power LED        │
│   blinking. Students cannot use this         │
│   workstation.                               │
│ Location: CSE Lab 1                          │
│ Priority: HIGH (Urgent)                      │
│ Status: ASSIGNED                             │
│ Created By: Dr. Rajesh Kumar (Faculty)       │
│ Assigned To: Mr. Kumar (Engineer)            │
│ Notes: Engineer is currently fixing it       │
└──────────────────────────────────────────────┘
```

### **Ticket #2 Details**
```
┌─ NETWORK ISSUE ──────────────────────────────┐
│ ID: 2                                        │
│ Title: WiFi intermittent in library          │
│ Description:                                  │
│   Students unable to connect to WiFi.        │
│   Connection drops frequently.               │
│ Location: Library                            │
│ Priority: MEDIUM                             │
│ Status: IN_PROGRESS                          │
│ Created By: Prof. Anitha M (Faculty)         │
│ Assigned To: Ms. Priya (Engineer)            │
│ Notes: Engineer is working on firmware fix   │
└──────────────────────────────────────────────┘
```

### **Ticket #3 Details**
```
┌─ PORTAL ISSUE ───────────────────────────────┐
│ ID: 3                                        │
│ Title: Portal login error 500                │
│ Description:                                  │
│   Faculty portal showing server error        │
│   when trying to upload marks.               │
│ Location: Admin Server                       │
│ Priority: HIGH (Urgent)                      │
│ Status: OPEN                                 │
│ Created By: Dr. Suresh T (Faculty)           │
│ Assigned To: NOBODY YET!                     │
│ Notes: Waiting for engineer assignment       │
└──────────────────────────────────────────────┘
```

---

## **WHAT YOU CAN DO WITH TICKETS**

### **As Admin:**
- ✅ View all tickets
- ✅ Assign unassigned tickets to engineers
- ✅ Change ticket status
- ✅ View SLA compliance
- ✅ See statistics

### **As Engineer:**
- ✅ View assigned tickets
- ✅ Update ticket status (OPEN → IN_PROGRESS → RESOLVED)
- ✅ Add progress comments
- ✅ Mark tickets as complete

### **As Faculty:**
- ✅ View your created tickets
- ✅ Create new tickets
- ✅ Add comments to tickets
- ✅ Track status and progress
- ✅ See engineer assignments

---

## **EXPECTED RESULTS**

| Action | Expected |
|--------|----------|
| Login as Admin | See Admin Dashboard with 5 tickets |
| Login as Engineer | See Engineer Dashboard with assigned tickets |
| Login as Faculty | See Faculty Dashboard with created tickets |
| Click on ticket | Open ticket details page |
| Assign unassigned | Status changes to ASSIGNED |
| Update status | Immediate update in dashboard |
| Create new ticket | Appears in "Recent Issues" |

---

## **IF TICKETS DON'T APPEAR**

### **Check 1: Database**
```bash
mysql -u root -p2005
USE itsm_db;
SELECT * FROM tickets;
```
Should show 5 rows.

### **Check 2: API**
```bash
curl http://localhost:8080/api/tickets
```
Should return JSON with 5 tickets.

### **Check 3: Browser Console**
Press F12 → Console tab → Look for errors

### **Check 4: Restart Everything**
```bash
# Stop app
# Delete database
# Restart app with clean database
mvn clean spring-boot:run
```

---

## **SUMMARY**

✅ **5 Demo Tickets** automatically created
✅ **3 User Roles** to test (Admin, Engineer, Faculty)
✅ **Different Views** for each role
✅ **Real Workflow** to understand
✅ **Ready to Test** immediately!

**Just restart app and login to see tickets!** 🎉
