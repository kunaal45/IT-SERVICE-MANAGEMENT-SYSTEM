# 🗄️ DATABASE SETUP - STEP-BY-STEP VISUAL GUIDE

## YOUR EXACT STEPS (Follow This Exactly)

---

## STEP 1️⃣: OPEN MYSQL

### Option A: Command Line (Recommended)

**Click Start Menu → Search for "Command Prompt"**

```
C:\Users\kunaa>
```

**Type this command:**
```cmd
mysql -u root -p
```

**Press Enter**

**You'll see:**
```
Enter password:
```

**Type your password (2005) and press Enter**

You should now see:
```
mysql>
```

---

## STEP 2️⃣: SELECT YOUR DATABASE

**In the mysql> prompt, type:**

```sql
USE itsm_db;
```

**Press Enter**

You should see:
```
Database changed
mysql>
```

---

## STEP 3️⃣: CREATE ALL TABLES

**Copy all the SQL code below (Ctrl+A then Ctrl+C):**

```sql
-- CREATE USERS TABLE
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255),
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- CREATE SLA RULES TABLE
CREATE TABLE IF NOT EXISTS sla_rules (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    priority VARCHAR(50) NOT NULL UNIQUE,
    max_hours INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_priority (priority)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- CREATE TICKETS TABLE
CREATE TABLE IF NOT EXISTS tickets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description LONGTEXT,
    priority VARCHAR(20) NOT NULL,
    status VARCHAR(50) NOT NULL,
    sla_deadline DATETIME,
    sla_breached BOOLEAN DEFAULT FALSE,
    created_by_id BIGINT NOT NULL,
    assigned_to_id BIGINT,
    resolution_notes LONGTEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by_id) REFERENCES users(id) ON DELETE RESTRICT,
    FOREIGN KEY (assigned_to_id) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_status (status),
    INDEX idx_priority (priority),
    INDEX idx_created_by (created_by_id),
    INDEX idx_assigned_to (assigned_to_id),
    INDEX idx_sla_breached (sla_breached)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- CREATE COMMENTS TABLE
CREATE TABLE IF NOT EXISTS comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ticket_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    content LONGTEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ticket_id) REFERENCES tickets(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT,
    INDEX idx_ticket (ticket_id),
    INDEX idx_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- CREATE AUDIT LOGS TABLE
CREATE TABLE IF NOT EXISTS audit_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    action VARCHAR(100) NOT NULL,
    user_id BIGINT,
    ticket_id BIGINT,
    details LONGTEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (ticket_id) REFERENCES tickets(id) ON DELETE SET NULL,
    INDEX idx_action (action),
    INDEX idx_user (user_id),
    INDEX idx_ticket (ticket_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- INSERT SAMPLE USERS
INSERT INTO users (name, email, password, role) VALUES
('Admin User', 'admin@example.com', 'adminpass', 'ADMIN'),
('Support Engineer', 'engineer@example.com', 'engineerpass', 'SUPPORT_ENGINEER'),
('Another Engineer', 'eng2@example.com', 'engineerpass', 'SUPPORT_ENGINEER'),
('Student User', 'student@example.com', 'studentpass', 'STUDENT'),
('Another Student', 'student2@example.com', 'studentpass', 'STUDENT');

-- INSERT SLA RULES
INSERT INTO sla_rules (priority, max_hours) VALUES
('HIGH', 24),
('MEDIUM', 48),
('LOW', 72);

-- INSERT SAMPLE TICKETS
INSERT INTO tickets (title, description, priority, status, created_by_id, assigned_to_id, sla_deadline, sla_breached)
VALUES
('Cannot access portal', 'Login fails for many students', 'HIGH', 'OPEN', 4, NULL, DATE_ADD(NOW(), INTERVAL 24 HOUR), FALSE),
('Email not syncing', 'Emails delayed by several hours', 'MEDIUM', 'ASSIGNED', 4, 2, DATE_ADD(NOW(), INTERVAL 48 HOUR), FALSE),
('Database connection timeout', 'API calls timeout', 'MEDIUM', 'IN_PROGRESS', 5, 2, DATE_ADD(NOW(), INTERVAL 48 HOUR), FALSE),
('Report generation failing', 'Custom reports not generating', 'LOW', 'OPEN', 4, NULL, DATE_ADD(NOW(), INTERVAL 72 HOUR), FALSE),
('Mobile app crashes', 'iOS app crashes on iOS 14+', 'HIGH', 'RESOLVED', 5, 3, DATE_ADD(NOW(), INTERVAL 24 HOUR), FALSE);

-- INSERT SAMPLE COMMENTS
INSERT INTO comments (ticket_id, user_id, content) VALUES
(1, 2, 'Investigating portal access issue. Checking load balancer.'),
(1, 4, 'Thanks for looking into this. Affects 50 students.'),
(2, 2, 'Found issue - sync service stopped. Restarting.'),
(3, 2, 'Optimizing query. Will update in 2 hours.'),
(5, 3, 'Fixed by updating SSL certificate.');

-- INSERT SAMPLE AUDIT LOGS
INSERT INTO audit_logs (action, user_id, ticket_id, details) VALUES
('TICKET_CREATED', 4, 1, 'Ticket created'),
('TICKET_CREATED', 4, 2, 'Ticket created'),
('TICKET_ASSIGNED', 1, 2, 'Assigned to engineer'),
('TICKET_ASSIGNED', 1, 3, 'Assigned to engineer'),
('STATUS_CHANGED', 2, 2, 'Changed to ASSIGNED'),
('STATUS_CHANGED', 2, 3, 'Changed to IN_PROGRESS'),
('COMMENT_ADDED', 2, 1, 'Engineer commented'),
('COMMENT_ADDED', 4, 1, 'Student commented'),
('STATUS_CHANGED', 3, 5, 'Changed to RESOLVED'),
('TICKET_CLOSED', 5, 5, 'Ticket closed');
```

**In MySQL command line, right-click and paste (Ctrl+V)**

**You'll see output like:**
```
Query OK, 0 rows affected
Query OK, 0 rows affected
Query OK, 5 rows affected
Query OK, 5 rows affected
Query OK, 5 rows affected
Query OK, 5 rows affected
Query OK, 10 rows affected
```

---

## STEP 4️⃣: VERIFY SETUP

**Type these commands one by one to check everything:**

```sql
SHOW TABLES;
```

**Should show:**
```
+--------------------+
| Tables_in_itsm_db  |
+--------------------+
| audit_logs         |
| comments           |
| sla_rules          |
| tickets            |
| users              |
+--------------------+
```

**Then type:**
```sql
SELECT COUNT(*) FROM users;
```

**Should show:**
```
+----------+
| COUNT(*) |
+----------+
|        5 |
+----------+
```

**Check all tables:**
```sql
SELECT COUNT(*) FROM tickets;
SELECT COUNT(*) FROM comments;
SELECT COUNT(*) FROM audit_logs;
SELECT COUNT(*) FROM sla_rules;
```

---

## STEP 5️⃣: VIEW YOUR DATA

**See all users:**
```sql
SELECT * FROM users;
```

**See all tickets:**
```sql
SELECT * FROM tickets;
```

**See SLA rules:**
```sql
SELECT * FROM sla_rules;
```

---

## STEP 6️⃣: EXIT MYSQL

**Type:**
```sql
EXIT;
```

Or press **Ctrl+Z** then **Enter**

---

## ✅ DATABASE SETUP COMPLETE!

Your database is now ready with:
- ✅ 5 tables created
- ✅ 5 users inserted (including admin, engineer, student)
- ✅ 5 sample tickets
- ✅ 5 comments
- ✅ 10 audit logs
- ✅ 3 SLA rules

---

## 🚀 NOW RUN YOUR APPLICATION

**Open a NEW command prompt:**

```cmd
cd c:\Users\kunaa\IdeaProjects\itsm-system
mvnw.cmd spring-boot:run
```

**Wait for:**
```
Started ItsmSystemApplication in X.XXX seconds
Tomcat started on port(s): 8080
```

**Then open browser:**
```
http://localhost:8080/index.html
```

**Login with:**
```
Email: admin@example.com
Password: adminpass
```

---

## 🎉 SUCCESS!

Your ITSM system is now:
✅ Connected to MySQL database
✅ Using persistent storage
✅ Ready for production
✅ Full data preservation

**Enjoy!** 🚀

---

## 📞 IF SOMETHING GOES WRONG

| Error | Solution |
|-------|----------|
| "Access denied" | Check password (2005) |
| "Unknown database" | Run: CREATE DATABASE itsm_db; |
| "Duplicate entry" | Tables already exist (OK!) |
| "Connection refused" | MySQL not running |

---

**Time: 5 minutes | Status: ✅ COMPLETE**
