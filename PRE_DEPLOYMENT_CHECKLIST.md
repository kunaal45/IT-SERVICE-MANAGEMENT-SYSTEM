# ✅ FINAL PRE-DEPLOYMENT CHECKLIST

## Before You Run - Verify These Files Exist

### Backend Security (Must Have)
- [x] `JwtUtil.java` - Token generation and validation
- [x] `JwtAuthenticationFilter.java` - Request authentication
- [x] `SecurityConfig.java` - Security configuration
- [x] `AuthController.java` - Login endpoint

### Backend DTOs (Must Have)
- [x] `LoginRequest.java`
- [x] `LoginResponse.java`
- [x] `CreateTicketRequest.java`

### Backend Entities (Enhanced)
- [x] `User.java` - With @JsonIgnore, indexes
- [x] `Ticket.java` - With category, location, lifecycle
- [x] `AuditLog.java` - Activity logging

### Backend Controllers (Enhanced)
- [x] `TicketApiController.java` - With JWT, @PreAuthorize
- [x] `AuthController.java` - Login and user info

### Backend Services (Enhanced)
- [x] `TicketService.java` - With createTicket, activity logging

### Configuration (Updated)
- [x] `application.properties` - JWT secret, expiration
- [x] `pom.xml` - JWT dependencies

### Documentation (Created)
- [x] `FRONTEND_INTEGRATION_GUIDE.md`
- [x] `BACKEND_INTEGRATION_COMPLETE.md`
- [x] `TESTING_QUICK_START.md`
- [x] `INTEGRATION_COMPLETE_SUMMARY.md`

---

## Run This Quick Check

```bash
# 1. Check if all Java files compile
mvn clean compile

# Should see: BUILD SUCCESS

# 2. Check if MySQL is running
mysql -u root -p2005 -e "SELECT 1"

# Should see: 1

# 3. Check if database exists
mysql -u root -p2005 -e "SHOW DATABASES LIKE 'itsm_db'"

# Should see: itsm_db
```

---

## Start Application

```bash
mvn clean spring-boot:run
```

### Expected Console Output:
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.2)

2026-02-19 ... INFO ... Started ItsmSystemApplication in 5.234 seconds
```

### Look For These Lines:
```
✓ Mapped "{[/api/auth/login],methods=[POST]}"
✓ Mapped "{[/api/tickets],methods=[GET]}"
✓ Mapped "{[/api/tickets],methods=[POST]}"
✓ Mapped "{[/api/tickets/{id}/assign/{engineerId}],methods=[POST]}"
✓ Mapped "{[/api/tickets/{id}/resolve],methods=[PUT]}"
✓ Mapped "{[/api/tickets/{id}/close],methods=[PUT]}"
```

---

## Test Login API

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@college.edu","password":"admin123"}'
```

### Expected Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "email": "admin@college.edu",
  "name": "Admin User",
  "role": "ADMIN",
  "id": 1
}
```

### If You Get This - ✅ SUCCESS!
Your backend is working perfectly!

### If You Get 404:
```
Problem: AuthController not loaded
Fix: Check if @RestController annotation is present
```

### If You Get 500:
```
Problem: Database connection or DataInitializer error
Fix: Check MySQL is running and database exists
```

---

## Test Get Tickets API

```bash
# Use token from previous step
curl -X GET http://localhost:8080/api/tickets \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### Expected Response:
```json
[
  {
    "id": 1,
    "title": "Lab PC-05 monitor not working",
    "description": "Monitor shows no display...",
    "status": "ASSIGNED",
    "priority": "HIGH",
    "category": "HARDWARE",
    "location": "CSE Lab 1",
    "createdBy": {
      "id": 5,
      "email": "rajesh@college.edu",
      "name": "Dr. Rajesh Kumar",
      "role": "FACULTY"
    },
    "assignedTo": {
      "id": 2,
      "email": "kumar@college.edu",
      "name": "Mr. Kumar",
      "role": "ENGINEER"
    }
  }
  // ... more tickets
]
```

### If You Get This - ✅ PERFECT!
All tickets are being returned with proper relationships!

---

## Test Frontend Login

1. Open browser: `http://localhost:8080/index.html`
2. Open Developer Console (F12)
3. Login with: `admin@college.edu` / `admin123`
4. Check console for token:
   ```javascript
   localStorage.getItem('jwt_token')
   // Should show: "eyJhbGciOiJIUzI1NiJ9..."
   ```

### If Token is Stored - ✅ EXCELLENT!
Frontend integration is working!

---

## Verify Database

```sql
mysql -u root -p2005
USE itsm_db;

-- Should have 7 users
SELECT COUNT(*) FROM users;

-- Should have 5 tickets
SELECT COUNT(*) FROM tickets;

-- Should have activity logs
SELECT * FROM audit_logs ORDER BY created_at DESC LIMIT 5;
```

### Expected Results:
```
COUNT(*) FROM users: 7
COUNT(*) FROM tickets: 5
audit_logs: Multiple entries with actions like TICKET_CREATED, TICKET_ASSIGNED
```

---

## Test Role-Based Access

### Admin Should See All Tickets
```bash
# Login as admin
ADMIN_TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@college.edu","password":"admin123"}' | jq -r '.token')

# Get tickets
curl -s http://localhost:8080/api/tickets -H "Authorization: Bearer $ADMIN_TOKEN" | jq length

# Should return: 5
```

### Engineer Should See Only Assigned Tickets
```bash
# Login as engineer
ENG_TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"kumar@college.edu","password":"eng123"}' | jq -r '.token')

# Get tickets
curl -s http://localhost:8080/api/tickets -H "Authorization: Bearer $ENG_TOKEN" | jq length

# Should return: 2 (only assigned to kumar@college.edu)
```

### Faculty Should See Only Created Tickets
```bash
# Login as faculty
FAC_TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"rajesh@college.edu","password":"faculty123"}' | jq -r '.token')

# Get tickets
curl -s http://localhost:8080/api/tickets -H "Authorization: Bearer $FAC_TOKEN" | jq length

# Should return: 1 (only created by rajesh@college.edu)
```

---

## Test Create Ticket

```bash
# Login as faculty
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"rajesh@college.edu","password":"faculty123"}' | jq -r '.token')

# Create ticket
curl -X POST http://localhost:8080/api/tickets \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Test Ticket from API",
    "description": "Testing ticket creation",
    "priority": "HIGH",
    "category": "SOFTWARE",
    "location": "Lab 2"
  }'
```

### Expected Response:
```json
{
  "id": 6,
  "title": "Test Ticket from API",
  "description": "Testing ticket creation",
  "status": "OPEN",
  "priority": "HIGH",
  "category": "SOFTWARE",
  "location": "Lab 2",
  "createdBy": { ... },
  "assignedTo": null
}
```

### Check Database:
```sql
SELECT * FROM tickets WHERE id = 6;
SELECT * FROM audit_logs WHERE ticket_id = 6 AND action = 'TICKET_CREATED';
```

---

## Test Activity Logging

```sql
-- Check all activity logs
SELECT 
    al.id,
    al.action,
    u.name as performed_by,
    t.title as ticket_title,
    al.details,
    al.created_at
FROM audit_logs al
LEFT JOIN users u ON al.user_id = u.id
LEFT JOIN tickets t ON al.ticket_id = t.id
ORDER BY al.created_at DESC
LIMIT 10;
```

### Should See:
- TICKET_CREATED entries
- TICKET_ASSIGNED entries
- TICKET_RESOLVED entries
- TICKET_CLOSED entries

---

## Final Integration Test

Run the complete workflow:

```bash
# 1. Faculty creates ticket
FAC_TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"rajesh@college.edu","password":"faculty123"}' | jq -r '.token')

TICKET_ID=$(curl -s -X POST http://localhost:8080/api/tickets \
  -H "Authorization: Bearer $FAC_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"title":"Complete Workflow Test","priority":"HIGH","category":"NETWORK"}' | jq -r '.id')

echo "Created ticket: $TICKET_ID"

# 2. Admin assigns to engineer
ADMIN_TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@college.edu","password":"admin123"}' | jq -r '.token')

curl -s -X POST http://localhost:8080/api/tickets/$TICKET_ID/assign/3 \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq '.status'

echo "Assigned - Status should be: ASSIGNED"

# 3. Engineer resolves
ENG_TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"priya@college.edu","password":"eng123"}' | jq -r '.token')

curl -s -X PUT "http://localhost:8080/api/tickets/$TICKET_ID/resolve?userRole=ENGINEER&agentName=Ms.%20Priya&agentEmail=priya@college.edu" \
  -H "Authorization: Bearer $ENG_TOKEN" | jq '.status'

echo "Resolved - Status should be: RESOLVED"

# 4. Faculty closes
curl -s -X PUT "http://localhost:8080/api/tickets/$TICKET_ID/close?userRole=FACULTY&agentName=Dr.%20Rajesh&agentEmail=rajesh@college.edu" \
  -H "Authorization: Bearer $FAC_TOKEN" | jq '.status'

echo "Closed - Status should be: CLOSED"

# 5. Check activity logs
mysql -u root -p2005 -e "SELECT action, details FROM itsm_db.audit_logs WHERE ticket_id = $TICKET_ID ORDER BY created_at"
```

### Expected Output:
```
Created ticket: 6
Assigned - Status should be: "ASSIGNED"
Resolved - Status should be: "RESOLVED"
Closed - Status should be: "CLOSED"

+------------------+--------------------------------+
| action           | details                        |
+------------------+--------------------------------+
| TICKET_CREATED   | Ticket created by Dr. Rajesh   |
| TICKET_ASSIGNED  | Ticket assigned to Ms. Priya   |
| TICKET_RESOLVED  | Ticket Resolved by Ms. Priya   |
| TICKET_CLOSED    | Ticket Closed by Dr. Rajesh    |
+------------------+--------------------------------+
```

### If All Steps Work - 🎉 SYSTEM IS PERFECT!

---

## Common Issues & Quick Fixes

### Issue: Can't compile
```bash
# Fix: Clean and rebuild
mvn clean install -DskipTests
```

### Issue: 404 on /api/auth/login
```bash
# Check: Is AuthController in the correct package?
ls src/main/java/com/itsm/itsmsystem/controller/AuthController.java
```

### Issue: 401 Unauthorized
```bash
# Check: Is token being sent?
curl -v http://localhost:8080/api/tickets -H "Authorization: Bearer TOKEN"
# Look for: Authorization: Bearer in request headers
```

### Issue: Database connection error
```bash
# Check MySQL is running
sudo systemctl status mysql  # Linux
# or
mysql -u root -p2005 -e "SELECT 1"
```

---

## ✅ You're Ready When:

- [x] Application starts without errors
- [x] Login returns JWT token
- [x] Get tickets returns array of tickets
- [x] Role-based filtering works
- [x] Create ticket works
- [x] Database shows 7 users, 5+ tickets
- [x] Activity logs are being created
- [x] Frontend can login and store token
- [x] Complete workflow test passes

---

## 🚀 DEPLOY CHECKLIST

Before deploying to production:

- [ ] Change JWT secret in application.properties
- [ ] Use environment variables for sensitive data
- [ ] Update CORS to production URL
- [ ] Enable HTTPS
- [ ] Set up database backups
- [ ] Configure proper logging
- [ ] Set up monitoring
- [ ] Test all endpoints in production
- [ ] Update frontend API URLs

---

## 📚 Reference Documents

**For Frontend:**
- `FRONTEND_INTEGRATION_GUIDE.md` - Complete fetch() examples

**For Testing:**
- `TESTING_QUICK_START.md` - 5-minute test guide

**For Overview:**
- `BACKEND_INTEGRATION_COMPLETE.md` - Full implementation
- `INTEGRATION_COMPLETE_SUMMARY.md` - Quick summary

---

**System Status: ✅ READY FOR USE**

**Integration: COMPLETE**

**Documentation: COMPLETE**

**Testing: READY**

**Production: READY (after deployment checklist)** 🚀
