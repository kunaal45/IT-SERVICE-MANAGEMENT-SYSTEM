# ✅ DATABASE CONNECTION VERIFICATION & TESTS

## YOUR STATUS CHECK

✅ **Database created in MySQL**: `itsm_db`
✅ **All tables created**: users, tickets, comments, audit_logs, sla_rules
✅ **Sample data inserted**: 25+ records
✅ **Test file updated**: With 11 comprehensive tests

---

## 🧪 HOW TO RUN TESTS

### Option 1: Run All Tests (Recommended)
```bash
cd c:\Users\kunaa\IdeaProjects\itsm-system
mvnw.cmd test
```

### Option 2: Run Specific Test
```bash
mvnw.cmd test -Dtest=ItsmSystemApplicationTests
```

### Option 3: Run from IDE
- Open project in IntelliJ IDEA or Eclipse
- Right-click on `ItsmSystemApplicationTests.java`
- Click "Run 'ItsmSystemApplicationTests'"

---

## 🧪 WHAT TESTS WILL CHECK

### Test 1: **contextLoads()**
- Verifies Spring context loads
- Checks all repositories are autowired
- **Expected**: ✅ PASS

### Test 2: **testDatabaseConnection()**
- Verifies database connection works
- Checks user repository can connect
- **Expected**: ✅ PASS

### Test 3: **testUserTableExists()**
- Verifies users table exists with 5 records
- **Expected**: ✅ 5 users found

### Test 4: **testTicketTableExists()**
- Verifies tickets table exists with 5 records
- **Expected**: ✅ 5 tickets found

### Test 5: **testCommentTableExists()**
- Verifies comments table exists with 5 records
- **Expected**: ✅ 5 comments found

### Test 6: **testAuditLogTableExists()**
- Verifies audit_logs table exists with 10 records
- **Expected**: ✅ 10 audit logs found

### Test 7: **testSLARuleTableExists()**
- Verifies sla_rules table exists with 3 records
- **Expected**: ✅ 3 SLA rules found

### Test 8: **testAdminUserExists()**
- Verifies admin@example.com exists with ADMIN role
- **Expected**: ✅ PASS

### Test 9: **testEngineerUserExists()**
- Verifies engineer@example.com exists with SUPPORT_ENGINEER role
- **Expected**: ✅ PASS

### Test 10: **testStudentUserExists()**
- Verifies student@example.com exists with STUDENT role
- **Expected**: ✅ PASS

### Test 11: **testSLARulesConfiguration()**
- Verifies all 3 SLA rules are configured correctly
- HIGH: 24 hours
- MEDIUM: 48 hours
- LOW: 72 hours
- **Expected**: ✅ All 3 rules verified

---

## ✅ EXPECTED TEST RESULTS

If all tests pass, you'll see:
```
[INFO] Tests run: 11, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---

## 🔍 WHAT IF TESTS FAIL?

### If you see: "Connection refused"
```
ERROR: java.sql.SQLException: Cannot get a connection
SOLUTION: 
  1. Check MySQL is running
  2. Verify database itsm_db exists
  3. Check username/password in application.properties
```

### If you see: "Table 'itsm_db.users' doesn't exist"
```
SOLUTION:
  1. You didn't create the tables
  2. Execute the SQL from CREATE_TABLES_NOW.md
  3. Verify with: SHOW TABLES; in MySQL
```

### If you see: "Incorrect number of users/tickets"
```
SOLUTION:
  1. Check you ran the full INSERT statements
  2. Verify with: SELECT COUNT(*) FROM users;
  3. Should be 5 users, 5 tickets, 5 comments, etc.
```

---

## 📋 TEST FILE LOCATION & CONTENT

**File**: `src/test/java/com/itsm/itsmsystem/ItsmSystemApplicationTests.java`

**What I Added**:
- ✅ 11 comprehensive test methods
- ✅ Dependency injection of all 5 repositories
- ✅ Assertions for each table/record count
- ✅ Specific user and SLA rule verification
- ✅ Detailed test descriptions

**Size**: ~140 lines of test code

---

## 🚀 COMPLETE WORKFLOW

### Step 1: Verify MySQL ✅
```bash
mysql -u root -p
USE itsm_db;
SHOW TABLES;
SELECT COUNT(*) FROM users;
```

### Step 2: Run Tests ✅
```bash
cd c:\Users\kunaa\IdeaProjects\itsm-system
mvnw.cmd test
```

### Step 3: Check Results ✅
```
BUILD SUCCESS = Database is correctly connected
```

### Step 4: Run Application ✅
```bash
mvnw.cmd spring-boot:run
```

### Step 5: Test in Browser ✅
```
http://localhost:8080/index.html
Login: admin@example.com / adminpass
```

---

## 📊 DATABASE VERIFICATION CHECKLIST

| Item | Check | Expected |
|------|-------|----------|
| MySQL Running | `mysql -u root -p` | ✅ Connects |
| Database Exists | `SHOW DATABASES;` | ✅ itsm_db exists |
| Tables Created | `SHOW TABLES;` | ✅ 5 tables |
| Users Table | `SELECT COUNT(*) FROM users;` | ✅ 5 rows |
| Tickets Table | `SELECT COUNT(*) FROM tickets;` | ✅ 5 rows |
| Comments Table | `SELECT COUNT(*) FROM comments;` | ✅ 5 rows |
| Audit Logs Table | `SELECT COUNT(*) FROM audit_logs;` | ✅ 10 rows |
| SLA Rules Table | `SELECT COUNT(*) FROM sla_rules;` | ✅ 3 rows |
| Admin User | `SELECT * FROM users WHERE email='admin@example.com';` | ✅ Found |
| Tests Pass | `mvnw.cmd test` | ✅ 11/11 PASS |

---

## 📝 MANUAL VERIFICATION IN MYSQL

If you want to manually verify before running tests:

```sql
-- Check all tables exist
SHOW TABLES;

-- Count records in each table
SELECT COUNT(*) as users FROM users;
SELECT COUNT(*) as tickets FROM tickets;
SELECT COUNT(*) as comments FROM comments;
SELECT COUNT(*) as audit_logs FROM audit_logs;
SELECT COUNT(*) as sla_rules FROM sla_rules;

-- Check admin user
SELECT * FROM users WHERE email = 'admin@example.com';

-- Check SLA rules
SELECT * FROM sla_rules;

-- Check sample ticket
SELECT * FROM tickets LIMIT 1;

-- Check sample comment
SELECT * FROM comments LIMIT 1;

-- Check audit logs
SELECT * FROM audit_logs LIMIT 1;
```

---

## 🎯 DATABASE CONNECTION CONFIGURATION

Your `application.properties` is correctly configured:

```properties
# Database URL
spring.datasource.url=jdbc:mysql://localhost:3306/itsm_db

# Credentials
spring.datasource.username=root
spring.datasource.password=2005

# Driver
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=validate
```

This means:
- ✅ Connects to MySQL on localhost:3306
- ✅ Uses database: itsm_db
- ✅ Authenticates with root/2005
- ✅ Validates (doesn't auto-create) tables
- ✅ Uses MySQL dialect

---

## ✨ WHAT HAPPENS WHEN YOU RUN TESTS

```
Test Execution:
    ↓
Spring Boot loads context
    ↓
Repositories are autowired
    ↓
Connection to itsm_db is made
    ↓
Each test queries the database
    ↓
Results are verified
    ↓
If all pass: BUILD SUCCESS ✅
If any fail: BUILD FAILURE ❌ (shows which test failed)
```

---

## 🎊 NEXT STEPS

### 1. Run the Tests
```bash
mvnw.cmd test
```

### 2. If Tests Pass (11/11)
```bash
mvnw.cmd spring-boot:run
```

### 3. If Tests Fail
Check the error message and refer to the "WHAT IF TESTS FAIL?" section above

### 4. Once App is Running
```
http://localhost:8080/index.html
```

---

## ✅ SUMMARY

**What's Done**:
- ✅ Database: itsm_db created in MySQL
- ✅ Tables: All 5 tables created (users, tickets, comments, audit_logs, sla_rules)
- ✅ Data: 25+ sample records inserted
- ✅ Tests: 11 comprehensive tests added to verify everything
- ✅ Config: application.properties correctly configured

**What You Need to Do**:
1. Run: `mvnw.cmd test`
2. Verify: All 11 tests pass
3. Run app: `mvnw.cmd spring-boot:run`
4. Test in browser: Login and use the system

**Status**: ✅ READY FOR TESTING

---

## 🚀 RUN THIS COMMAND NOW

```bash
cd c:\Users\kunaa\IdeaProjects\itsm-system
mvnw.cmd test
```

If you see:
```
[INFO] BUILD SUCCESS
```

Your database is **correctly connected** and **all tests pass**! ✅

---

**Created**: February 11, 2026
**Status**: ✅ READY
**Tests**: 11 comprehensive
**Expected**: 11/11 PASS
