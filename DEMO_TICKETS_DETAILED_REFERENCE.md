# 📋 DEMO TICKETS - Complete Sample Data Reference

## ✅ Demo Tickets Created Automatically

When you restart the application, the **DataInitializer** creates these 5 demo tickets:

---

## **Ticket #1: Lab PC-05 Monitor Not Working**

```
ID:             1
Title:          Lab PC-05 monitor not working
Description:    Monitor shows no display, power LED blinking. Students cannot use this workstation.
Category:       HARDWARE
Priority:       HIGH
Status:         ASSIGNED
Location:       CSE Lab 1
Created By:     Dr. Rajesh Kumar (Faculty) [rajesh@college.edu]
Assigned To:    Mr. Kumar (Hardware Engineer) [kumar@college.edu]
Created At:     Current timestamp
Updated At:     Current timestamp
```

### View This Ticket In:
- ✅ **Admin Dashboard** - In the "Global Ticket Queue" table
- ✅ **Engineer Dashboard** - In the "My Assigned Tickets" section (if logged in as engineer)
- ✅ **Faculty Dashboard** - In the "Recent Issues" section (if logged in as faculty who created it)

---

## **Ticket #2: WiFi Intermittent in Library**

```
ID:             2
Title:          WiFi intermittent in library
Description:    Students unable to connect to WiFi. Connection drops frequently.
Category:       NETWORK
Priority:       MEDIUM
Status:         IN_PROGRESS
Location:       Library
Created By:     Prof. Anitha M (Faculty) [anitha@college.edu]
Assigned To:    Ms. Priya (Network Engineer) [priya@college.edu]
Created At:     Current timestamp
Updated At:     Current timestamp
```

### View This Ticket In:
- ✅ **Admin Dashboard** - In the "Global Ticket Queue" table
- ✅ **Engineer Dashboard** - Shows as "In Progress" (if logged in as engineer assigned to it)
- ✅ **Faculty Dashboard** - In the "Recent Issues" section (if logged in as faculty who created it)

---

## **Ticket #3: Portal Login Error 500**

```
ID:             3
Title:          Portal login error 500
Description:    Faculty portal showing server error when trying to upload marks.
Category:       PORTAL_WEBSITE
Priority:       HIGH
Status:         OPEN
Location:       Admin Server
Created By:     Dr. Suresh T (Faculty) [suresh@college.edu]
Assigned To:    (Not yet assigned)
Created At:     Current timestamp
Updated At:     Current timestamp
```

### View This Ticket In:
- ✅ **Admin Dashboard** - Shows as "Unassigned" ticket
- ✅ **Engineer Dashboard** - Shows as "Unassigned" (can be assigned by engineers)
- ✅ **Faculty Dashboard** - In "Recent Issues" (can be tracked by creator)

---

## **Ticket #4: Projector Not Displaying**

```
ID:             4
Title:          Projector not displaying
Description:    HDMI connection issue. Tried multiple cables.
Category:       HARDWARE
Priority:       HIGH
Status:         RESOLVED
Location:       EC Classroom 301
Created By:     Dr. Suresh T (Faculty) [suresh@college.edu]
Assigned To:    Mr. Kumar (Hardware Engineer) [kumar@college.edu]
Created At:     Current timestamp
Updated At:     Current timestamp
```

### View This Ticket In:
- ✅ **Admin Dashboard** - Shows as "Resolved" in the queue
- ✅ **Engineer Dashboard** - Shows as completed work
- ✅ **Faculty Dashboard** - Shows as resolved issue

---

## **Ticket #5: Need Additional Chairs for Reading Room**

```
ID:             5
Title:          Need additional chairs for reading room
Description:    Current capacity insufficient during exam season. Need more seating.
Category:       RESOURCE_REQUEST
Priority:       MEDIUM
Status:         PENDING_APPROVAL
Location:       Library Reading Room
Created By:     Prof. Anitha M (Faculty) [anitha@college.edu]
Assigned To:    (Not assigned - awaiting approval)
Created At:     Current timestamp
Updated At:     Current timestamp
```

### View This Ticket In:
- ✅ **Admin Dashboard** - Shows as "Pending Approval"
- ✅ **Faculty Dashboard** - Tracks resource request status
- ✅ **Engineer Dashboard** - (Not assigned to engineers)

---

## How to See These Tickets

### **For Admin (admin@college.edu / admin123):**
1. Login to http://localhost:8080
2. You'll be redirected to `admin-dashboard.html`
3. Look for the **"Global Ticket Queue"** section
4. You should see all 5 tickets in a table

**Expected Display:**
```
ID | Ticket Description              | Priority | Status          | Assigned To        | Actions
----|----------------------------------|----------|-----------------|--------------------|---------
1  | Lab PC-05 monitor not working    | HIGH     | ASSIGNED        | Mr. Kumar          | View
2  | WiFi intermittent in library     | MEDIUM   | IN_PROGRESS     | Ms. Priya          | View
3  | Portal login error 500           | HIGH     | OPEN            | Unassigned         | View
4  | Projector not displaying         | HIGH     | RESOLVED        | Mr. Kumar          | View
5  | Need additional chairs...        | MEDIUM   | PENDING_APPROVAL| Unassigned         | View
```

---

### **For Engineer (priya@college.edu / eng123):**
1. Login to http://localhost:8080
2. You'll be redirected to `engineer-dashboard.html`
3. Look for the **"My Assigned Tickets"** section
4. You should see **Ticket #2** (assigned to Ms. Priya)

**Expected to see:**
- Ticket #2 "WiFi intermittent in library" (Status: IN_PROGRESS)

---

### **For Faculty (rajesh@college.edu / faculty123):**
1. Login to http://localhost:8080
2. You'll be redirected to `faculty-dashboard.html`
3. Look for the **"Recent Issues"** section
4. You should see tickets created by Dr. Rajesh Kumar

**Expected to see:**
- Ticket #1 "Lab PC-05 monitor not working" (Created by you)

---

## Complete Demo Flow

### **Scenario 1: Create a New Ticket (Faculty)**
1. Login as Faculty: `rajesh@college.edu / faculty123`
2. On Faculty Dashboard, fill the "Create New Issue" form
3. Example:
   - Title: "Lab computer crashes"
   - Category: SOFTWARE
   - Priority: HIGH
   - Description: "Computer randomly crashes during lab sessions"
   - Location: CSE Lab 2
4. Click "Submit Ticket"
5. New ticket appears in "Recent Issues"

### **Scenario 2: Assign Ticket to Engineer (Admin)**
1. Login as Admin: `admin@college.edu / admin123`
2. On Admin Dashboard, look for **Ticket #3** (Portal login error 500) - it's unassigned
3. Click the ticket to open details
4. Assign it to an engineer
5. Status changes to "ASSIGNED"

### **Scenario 3: Update Ticket Status (Engineer)**
1. Login as Engineer: `priya@college.edu / eng123`
2. On Engineer Dashboard, find **Ticket #2** (WiFi intermittent)
3. Click "Update" button
4. Change status from "IN_PROGRESS" to "RESOLVED"
5. Add resolution notes: "Firmware updated. WiFi stable now."
6. Faculty creator gets notification of resolution

### **Scenario 4: View All Tickets (Admin)**
1. Login as Admin: `admin@college.edu / admin123`
2. Admin Dashboard shows all 5 demo tickets
3. Can see:
   - Open tickets (need assignment)
   - Assigned tickets (in progress)
   - Resolved tickets (completed)
   - Statistics (SLA breaches, unassigned count)

---

## Database Verification

If you want to verify tickets in MySQL directly:

```sql
-- Check all tickets
SELECT id, title, status, priority, created_by_id, assigned_to_id FROM tickets;

-- Check specific ticket
SELECT * FROM tickets WHERE id = 1;

-- Check all tickets by status
SELECT * FROM tickets WHERE status = 'OPEN';

-- Count total tickets
SELECT COUNT(*) as total_tickets FROM tickets;
```

**Expected Output:**
```
id | title                               | status          | priority | created_by_id | assigned_to_id
---|-------------------------------------|-----------------|----------|---------------|---------------
1  | Lab PC-05 monitor not working       | ASSIGNED        | HIGH     | 5             | 2
2  | WiFi intermittent in library        | IN_PROGRESS     | MEDIUM   | 6             | 3
3  | Portal login error 500              | OPEN            | HIGH     | 7             | NULL
4  | Projector not displaying            | RESOLVED        | HIGH     | 7             | 2
5  | Need additional chairs...           | PENDING_APPROVAL| MEDIUM   | 6             | NULL
```

---

## Key Statistics You'll See

### In Admin Dashboard:
- **SLA Breaches:** 0 (none breached yet)
- **Unassigned Tickets:** 2 (Tickets #3 and #5)
- **Open Tickets:** 1 (Ticket #3)
- **Resolved Tickets:** 1 (Ticket #4)

---

## Understanding the Ticket Flow

```
┌─────────────────────────────────────────────────────┐
│ 1. FACULTY CREATES TICKET                            │
│    (rajesh@college.edu creates Ticket #1)           │
│    Status: OPEN                                      │
└────────────┬────────────────────────────────────────┘
             │
             ▼
┌─────────────────────────────────────────────────────┐
│ 2. ADMIN VIEWS IN DASHBOARD                          │
│    (admin@college.edu sees all tickets)             │
│    Can assign to engineers                          │
└────────────┬────────────────────────────────────────┘
             │
             ▼
┌─────────────────────────────────────────────────────┐
│ 3. ENGINEER RECEIVES ASSIGNMENT                      │
│    (kumar@college.edu sees assigned ticket)         │
│    Status: ASSIGNED                                 │
└────────────┬────────────────────────────────────────┘
             │
             ▼
┌─────────────────────────────────────────────────────┐
│ 4. ENGINEER WORKS ON TICKET                          │
│    Updates status to: IN_PROGRESS                   │
│    Adds progress comments                           │
└────────────┬────────────────────────────────────────┘
             │
             ▼
┌─────────────────────────────────────────────────────┐
│ 5. ENGINEER RESOLVES TICKET                          │
│    Updates status to: RESOLVED                      │
│    Adds resolution notes                            │
└────────────┬────────────────────────────────────────┘
             │
             ▼
┌─────────────────────────────────────────────────────┐
│ 6. FACULTY SEES RESOLUTION                           │
│    In dashboard, ticket shows: RESOLVED             │
│    Can view resolution notes                        │
└─────────────────────────────────────────────────────┘
```

---

## Troubleshooting

### **Issue: No tickets showing in dashboard**

**Solution 1: Restart Application**
```bash
# Stop the app (Ctrl+C)
# Clear any cached data
# Restart the app
mvn clean spring-boot:run
```

**Solution 2: Check Database**
```sql
mysql -u root -p2005
USE itsm_db;
SELECT COUNT(*) FROM tickets;  -- Should show 5
SELECT * FROM tickets;          -- Should show all 5 records
```

**Solution 3: Clear Browser Cache**
- Press `F12` (Developer Tools)
- Application → Local Storage → Clear All
- Refresh page

### **Issue: Tickets show but can't see all 5**

**Check your login role:**
- Admin can see ALL 5 tickets
- Engineers can see tickets ASSIGNED to them (2 tickets)
- Faculty can see tickets they CREATED (2-3 tickets)

### **Issue: Getting API errors in console**

**Check REST endpoint is working:**
```bash
# Test the API directly
curl http://localhost:8080/api/tickets
# Should return JSON array with 5 tickets
```

---

## Summary

✅ **5 Demo Tickets** are automatically created when app starts
✅ **Admin** can see all tickets in dashboard
✅ **Engineers** can see assigned tickets
✅ **Faculty** can see their created tickets
✅ **All roles** can view, update, and manage tickets

**Just restart your application and login to see the demo tickets in action!** 🎉
