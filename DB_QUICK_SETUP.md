# 🗄️ DATABASE SETUP - QUICK START (5 MINUTES)

## ✅ YOUR DATABASE IS READY TO SETUP

You created the database `itsm_db`, but now you need to create the tables and add sample data.

---

## ⚡ FASTEST WAY (Copy-Paste)

### Step 1: Open MySQL Command Line
```cmd
mysql -u root -p
```
Enter password: `2005`

### Step 2: Select Database
```sql
USE itsm_db;
```

### Step 3: Copy Everything Below & Paste Into MySQL

```sql
-- =====================================================
-- 1. USERS TABLE
-- =====================================================
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

-- =====================================================
-- 2. SLA_RULES TABLE
-- =====================================================
CREATE TABLE IF NOT EXISTS sla_rules (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    priority VARCHAR(50) NOT NULL UNIQUE,
    max_hours INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_priority (priority)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 3. TICKETS TABLE
-- =====================================================
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

-- =====================================================
-- 4. COMMENTS TABLE
-- =====================================================
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

-- =====================================================
-- 5. AUDIT_LOGS TABLE
-- =====================================================
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

-- =====================================================
-- INSERT SAMPLE DATA
-- =====================================================

-- Insert Users
INSERT INTO users (name, email, password, role) VALUES
('Admin User', 'admin@example.com', 'adminpass', 'ADMIN'),
('Support Engineer', 'engineer@example.com', 'engineerpass', 'SUPPORT_ENGINEER'),
('Another Engineer', 'eng2@example.com', 'engineerpass', 'SUPPORT_ENGINEER'),
('Student User', 'student@example.com', 'studentpass', 'STUDENT'),
('Another Student', 'student2@example.com', 'studentpass', 'STUDENT');

-- Insert SLA Rules
INSERT INTO sla_rules (priority, max_hours) VALUES
('HIGH', 24),
('MEDIUM', 48),
('LOW', 72);

-- Insert Tickets
INSERT INTO tickets (title, description, priority, status, created_by_id, assigned_to_id, sla_deadline, sla_breached)
VALUES
('Cannot access portal', 'Login fails for many students', 'HIGH', 'OPEN', 4, NULL, DATE_ADD(NOW(), INTERVAL 24 HOUR), FALSE),
('Email not syncing', 'Emails delayed by several hours', 'MEDIUM', 'ASSIGNED', 4, 2, DATE_ADD(NOW(), INTERVAL 48 HOUR), FALSE),
('Database connection timeout', 'API calls timeout', 'MEDIUM', 'IN_PROGRESS', 5, 2, DATE_ADD(NOW(), INTERVAL 48 HOUR), FALSE),
('Report generation failing', 'Custom reports not generating', 'LOW', 'OPEN', 4, NULL, DATE_ADD(NOW(), INTERVAL 72 HOUR), FALSE),
('Mobile app crashes', 'iOS app crashes on iOS 14+', 'HIGH', 'RESOLVED', 5, 3, DATE_ADD(NOW(), INTERVAL 24 HOUR), FALSE);

-- Insert Comments
INSERT INTO comments (ticket_id, user_id, content) VALUES
(1, 2, 'Investigating the portal access issue. Will check load balancer.'),
(1, 4, 'Thanks for looking into this. It affects 50 students.'),
(2, 2, 'Found the issue - sync service was stopped. Restarting now.'),
(3, 2, 'Working on optimizing the query. Will update in 2 hours.'),
(5, 3, 'Fixed by updating the SSL certificate.');

-- Insert Audit Logs
INSERT INTO audit_logs (action, user_id, ticket_id, details) VALUES
('TICKET_CREATED', 4, 1, 'New ticket created'),
('TICKET_CREATED', 4, 2, 'New ticket created'),
('TICKET_ASSIGNED', 1, 2, 'Ticket assigned to engineer'),
('TICKET_ASSIGNED', 1, 3, 'Ticket assigned to engineer'),
('STATUS_CHANGED', 2, 2, 'Status changed to ASSIGNED'),
('STATUS_CHANGED', 2, 3, 'Status changed to IN_PROGRESS'),
('COMMENT_ADDED', 2, 1, 'Engineer added comment'),
('COMMENT_ADDED', 4, 1, 'Student added comment'),
('STATUS_CHANGED', 3, 5, 'Status changed to RESOLVED'),
('TICKET_CLOSED', 5, 5, 'Ticket closed');
```

### Step 4: Press Enter & Wait

MySQL will execute all commands. You should see "Query OK" messages.

### Step 5: Verify

```sql
-- Check all tables exist
SHOW TABLES;

-- Check data count
SELECT COUNT(*) FROM users;
SELECT COUNT(*) FROM tickets;
SELECT COUNT(*) FROM comments;
SELECT COUNT(*) FROM audit_logs;
SELECT * FROM sla_rules;
```

**Done!** ✅ Tables created with sample data!

---

## 📊 EXPECTED COUNTS

| Table | Count |
|-------|-------|
| users | 5 |
| tickets | 5 |
| comments | 5 |
| audit_logs | 10 |
| sla_rules | 3 |

---

## 🔑 LOGIN CREDENTIALS (Already In Database)

```
Admin:    admin@example.com / adminpass
Engineer: engineer@example.com / engineerpass
Student:  student@example.com / studentpass
```

---

## 🚀 NEXT STEP

After setup, run:

```cmd
cd c:\Users\kunaa\IdeaProjects\itsm-system
mvnw.cmd spring-boot:run
```

Then open:
```
http://localhost:8080/index.html
```

**Your data will now persist in MySQL!** 🎉

---

## ❓ FAQ

**Q: Can I use MySQL Workbench instead?**
A: Yes! Open a new SQL query, paste the SQL, and execute.

**Q: What if tables already exist?**
A: The SQL uses `IF NOT EXISTS`, so it's safe to run again.

**Q: Do I need to import the schema file?**
A: No, just copy-paste the SQL above directly into MySQL.

**Q: When do I modify application.properties?**
A: Already done! It's configured for localhost:3306, itsm_db, root, password 2005.

---

**Time to Complete:** 5 minutes
**Difficulty:** Very Easy
**Status:** ✅ Ready

Setup your database now! 🗄️
