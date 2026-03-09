# 🎓 FACULTY PORTAL - QUICK REFERENCE

## Login Credentials

### **Faculty (Dr. Rajesh Kumar)**
```
Email: rajesh@college.edu
Password: faculty123
Area: CSE Lab 1
```
→ Lands on: **faculty-dashboard.html**

### **Engineer (Ms. Priya)**
```
Email: priya@college.edu
Password: eng123
Specialization: NETWORK
```
→ Lands on: **engineer-dashboard.html**

### **Admin**
```
Email: admin@college.edu
Password: admin123
```
→ Lands on: **admin-dashboard.html**

---

## Faculty Portal Features

### **Raise an Issue:**
- ✅ Issue Title
- ✅ Category (7 types)
- ✅ Priority (LOW/MEDIUM/HIGH)
- ✅ Location
- ✅ Description

### **Issue Categories:**
1. **HARDWARE** - PC, printer, projector, AC
2. **SOFTWARE** - Installation, license, updates
3. **NETWORK** - WiFi, internet, LAN
4. **PORTAL_WEBSITE** - Login, server, bugs
5. **INFRASTRUCTURE** - Chairs, tables, electrical
6. **ATTENDANCE_SYSTEM** - Biometric, RFID
7. **RESOURCE_REQUEST** - Additional resources

### **Dashboard Shows:**
- Total Issues Raised
- Open Issues
- Resolved Issues
- My Recent Issues (table)
- Assigned Area

---

## System Flow

```
Faculty Login
    ↓
faculty-dashboard.html
    ↓ Create Issue
    ↓ Select Category
    ↓ Auto-assigned to Engineer with that specialty
    ↓ SLA set: Category + Priority
    ↓ Engineer notified
    ↓ Engineer starts work
    ↓ Status updates
    ↓ Resolved/Closed
```

---

## Files Overview

| File | Purpose | For |
|------|---------|-----|
| `index.html` | Login page | Everyone |
| `faculty-dashboard.html` | Issue management | Faculty |
| `engineer-dashboard.html` | Assigned issues | Engineers |
| `admin-dashboard.html` | System management | Admin |

---

## API Endpoints

```
POST /api/auth/login
{
  "email": "rajesh@college.edu",
  "password": "faculty123"
}

POST /api/tickets
{
  "title": "Issue title",
  "category": "HARDWARE",
  "priority": "HIGH",
  "location": "CSE Lab 1",
  "description": "Details..."
}

GET /api/tickets
GET /api/tickets/by-category/HARDWARE
PUT /api/tickets/{id}/status
```

---

## Common Actions

### **As Faculty:**
1. Login → faculty-dashboard.html
2. Fill "Raise an Issue" form
3. Select category & priority
4. Submit
5. Issue auto-assigned to engineer
6. Track status in table

### **As Engineer:**
1. Login → engineer-dashboard.html
2. See issues assigned to them
3. Click to open details
4. Update status
5. Add resolution notes

### **As Admin:**
1. Login → admin-dashboard.html
2. View all issues
3. Approve resource requests
4. Configure settings

---

## Status Codes

| Status | Meaning |
|--------|---------|
| OPEN | Newly created, awaiting assignment |
| ASSIGNED | Assigned to engineer |
| IN_PROGRESS | Engineer working on it |
| RESOLVED | Issue fixed, awaiting verification |
| CLOSED | Verified and closed by faculty |
| PENDING_APPROVAL | (Resource requests only) |

---

## SLA Times by Category

| Category | Priority | Hours |
|----------|----------|-------|
| HARDWARE | HIGH | 2 |
| HARDWARE | MEDIUM | 8 |
| HARDWARE | LOW | 24 |
| NETWORK | HIGH | 1 |
| NETWORK | MEDIUM | 6 |
| NETWORK | LOW | 24 |
| SOFTWARE | HIGH | 4 |
| SOFTWARE | MEDIUM | 12 |
| SOFTWARE | LOW | 48 |

---

## Demo Data

**Faculty Members:**
- Dr. Rajesh Kumar (rajesh@college.edu) - CSE Lab 1
- Prof. Anitha M (anitha@college.edu) - Library
- Dr. Suresh T (suresh@college.edu) - EC Department

**Support Engineers:**
- Mr. Kumar (kumar@college.edu) - HARDWARE
- Ms. Priya (priya@college.edu) - NETWORK
- Mr. Arun (arun@college.edu) - SOFTWARE

---

**Everything is ready! Just login and start creating issues.** 🚀
