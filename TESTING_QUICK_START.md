# 🚀 Quick Start Testing Guide

## Test the Complete Integration in 5 Minutes

### Step 1: Start the Application (1 min)

```bash
mvn clean spring-boot:run
```

Wait for:
```
Started ItsmSystemApplication
```

---

### Step 2: Test Login API (1 min)

Open a new terminal or use Postman:

#### Test Admin Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"admin@college.edu\",\"password\":\"admin123\"}"
```

**Expected Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJzdWIiOiJhZG1pbkBjb2xsZWdlLmVkdSIsImlhdCI6MTcwODM0...",
  "email": "admin@college.edu",
  "name": "Admin User",
  "role": "ADMIN",
  "id": 1
}
```

Copy the token for next steps.

---

### Step 3: Test Get Tickets API (1 min)

Replace `YOUR_TOKEN` with the token from Step 2:

```bash
curl -X GET http://localhost:8080/api/tickets \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Expected Response:**
```json
[
  {
    "id": 1,
    "title": "Lab PC-05 monitor not working",
    "status": "ASSIGNED",
    "priority": "HIGH",
    "createdBy": { "name": "Dr. Rajesh Kumar", "email": "rajesh@college.edu" },
    "assignedTo": { "name": "Mr. Kumar", "email": "kumar@college.edu" }
  },
  // ... 4 more tickets
]
```

---

### Step 4: Test Create Ticket (1 min)

Login as Faculty first:

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"rajesh@college.edu\",\"password\":\"faculty123\"}"
```

Then create a ticket with the faculty token:

```bash
curl -X POST http://localhost:8080/api/tickets \
  -H "Authorization: Bearer FACULTY_TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"title\":\"Test Ticket\",\"description\":\"Testing API\",\"priority\":\"HIGH\",\"category\":\"SOFTWARE\",\"location\":\"Lab 1\"}"
```

**Expected Response:**
```json
{
  "id": 6,
  "title": "Test Ticket",
  "description": "Testing API",
  "status": "OPEN",
  "priority": "HIGH",
  "category": "SOFTWARE",
  "location": "Lab 1",
  "createdBy": { "name": "Dr. Rajesh Kumar" },
  "assignedTo": null
}
```

---

### Step 5: Test Frontend Login (1 min)

1. Open browser: `http://localhost:8080/index.html`
2. Login with:
   - Email: `admin@college.edu`
   - Password: `admin123`
3. Should redirect to Admin Dashboard
4. Check browser console (F12) for stored token:
   ```javascript
   localStorage.getItem('jwt_token')
   ```

---

## Verify Database

Check that everything is stored:

```sql
mysql -u root -p2005
USE itsm_db;

-- Check users
SELECT id, email, name, role FROM users;

-- Check tickets
SELECT id, title, status, priority FROM tickets;

-- Check activity logs
SELECT id, action, details, created_at FROM audit_logs ORDER BY created_at DESC LIMIT 10;
```

---

## Test All Roles

### Test Admin
```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"admin@college.edu\",\"password\":\"admin123\"}"

# Get all tickets (admin sees all)
curl -X GET http://localhost:8080/api/tickets \
  -H "Authorization: Bearer ADMIN_TOKEN"
```

### Test Engineer
```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"priya@college.edu\",\"password\":\"eng123\"}"

# Get tickets (engineer sees only assigned)
curl -X GET http://localhost:8080/api/tickets \
  -H "Authorization: Bearer ENGINEER_TOKEN"
```

### Test Faculty
```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"rajesh@college.edu\",\"password\":\"faculty123\"}"

# Get tickets (faculty sees only created)
curl -X GET http://localhost:8080/api/tickets \
  -H "Authorization: Bearer FACULTY_TOKEN"
```

---

## Test Role-Based Access

### Should Work ✅
```bash
# Admin assigns ticket
curl -X POST http://localhost:8080/api/tickets/3/assign/2 \
  -H "Authorization: Bearer ADMIN_TOKEN"
```

### Should Fail ❌ (403 Forbidden)
```bash
# Faculty tries to assign ticket
curl -X POST http://localhost:8080/api/tickets/3/assign/2 \
  -H "Authorization: Bearer FACULTY_TOKEN"
```

---

## Common Test Scenarios

### Scenario 1: Complete Ticket Lifecycle

```bash
# 1. Faculty creates ticket
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"rajesh@college.edu\",\"password\":\"faculty123\"}" | jq -r '.token')

TICKET_ID=$(curl -s -X POST http://localhost:8080/api/tickets \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"title\":\"Network Issue\",\"priority\":\"HIGH\",\"category\":\"NETWORK\"}" | jq -r '.id')

# 2. Admin assigns to engineer
ADMIN_TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"admin@college.edu\",\"password\":\"admin123\"}" | jq -r '.token')

curl -X POST http://localhost:8080/api/tickets/$TICKET_ID/assign/3 \
  -H "Authorization: Bearer $ADMIN_TOKEN"

# 3. Engineer resolves
ENG_TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"priya@college.edu\",\"password\":\"eng123\"}" | jq -r '.token')

curl -X PUT "http://localhost:8080/api/tickets/$TICKET_ID/resolve?userRole=ENGINEER&agentName=Ms.%20Priya" \
  -H "Authorization: Bearer $ENG_TOKEN"

# 4. Faculty closes
curl -X PUT "http://localhost:8080/api/tickets/$TICKET_ID/close?userRole=FACULTY&agentName=Dr.%20Rajesh" \
  -H "Authorization: Bearer $TOKEN"
```

---

## Expected Results Checklist

After running all tests, verify:

- [ ] Login returns JWT token
- [ ] Token is valid for 24 hours
- [ ] Admin sees all 5 demo tickets
- [ ] Engineer sees only tickets assigned to them (2 tickets for Kumar)
- [ ] Faculty sees only tickets they created
- [ ] Creating ticket returns new ticket with ID
- [ ] Assigning ticket updates assignedTo field
- [ ] Resolving ticket sets status to RESOLVED
- [ ] Closing ticket only works if status is RESOLVED
- [ ] Each action creates an audit_log entry
- [ ] 401 returned when token is missing
- [ ] 403 returned when role doesn't match endpoint requirement

---

## Troubleshooting Quick Tests

### Issue: Can't login
**Test:**
```bash
curl -v http://localhost:8080/api/auth/login
```
Should return 405 (Method Not Allowed) not 404

### Issue: 401 on authenticated endpoint
**Test:**
```bash
# Check if token is valid
curl -X GET http://localhost:8080/api/auth/user \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Issue: CORS error in browser
**Test:**
```bash
# Check CORS headers
curl -v -H "Origin: http://localhost:8080" \
  -H "Access-Control-Request-Method: GET" \
  -X OPTIONS http://localhost:8080/api/tickets
```

---

## Success Indicators

✅ **You're good if:**
- All curl commands return JSON (not HTML error pages)
- Status codes are 200 (success), 401 (unauthorized), or 403 (forbidden)
- Database shows new entries in audit_logs table
- Frontend login redirects to correct dashboard
- Browser console shows JWT token in localStorage

❌ **There's an issue if:**
- Getting 404 on /api/auth/login
- Getting HTML error pages instead of JSON
- Database is empty
- Frontend shows CORS errors
- Token not stored in localStorage

---

## Next Steps After Testing

1. ✅ If all tests pass → Update frontend to use real API
2. ✅ Update app.js with fetch() calls from FRONTEND_INTEGRATION_GUIDE.md
3. ✅ Remove mock data from frontend
4. ✅ Test complete workflows in browser
5. ✅ Deploy to production

---

**Testing Complete! System is Ready! 🎉**
