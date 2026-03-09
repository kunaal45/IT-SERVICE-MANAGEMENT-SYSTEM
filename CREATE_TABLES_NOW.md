# 🗄️ CREATE DATABASE TABLES - EXACT STEPS

## YOU ARE HERE
✅ MySQL is running
✅ Database `itsm_db` exists but is EMPTY (no tables)
❌ Need to create 5 tables and add sample data

---

## ⚡ FASTEST METHOD (Copy-Paste in MySQL)

### Step 1: Open MySQL Command Line
```bash
mysql -u root -p
```
Enter password: `2005`

You'll see:
```
mysql>
```

### Step 2: Select Your Database
```sql
USE itsm_db;
```

Should show:
```
Database changed
```

### Step 3: Create All Tables (Copy-Paste Below)

**Copy everything between the lines and paste into MySQL:**

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
-- INSERT SAMPLE USERS
-- =====================================================
INSERT INTO users (name, email, password, role) VALUES
('Admin User', 'admin@example.com', 'adminpass', 'ADMIN'),
('Support Engineer', 'engineer@example.com', 'engineerpass', 'SUPPORT_ENGINEER'),
('Another Engineer', 'eng2@example.com', 'engineerpass', 'SUPPORT_ENGINEER'),
('Student User', 'student@example.com', 'studentpass', 'STUDENT'),
('Another Student', 'student2@example.com', 'studentpass', 'STUDENT');

-- =====================================================
-- INSERT SLA RULES
-- =====================================================
INSERT INTO sla_rules (priority, max_hours) VALUES
('HIGH', 24),
('MEDIUM', 48),
('LOW', 72);

-- =====================================================
-- INSERT SAMPLE TICKETS
-- =====================================================
INSERT INTO tickets (title, description, priority, status, created_by_id, assigned_to_id, sla_deadline, sla_breached)
VALUES
(
    'Cannot access portal',
    'Login fails for many students. Error appears when trying to access the ITSM portal from campus network.',
    'HIGH',
    'OPEN',
    4,
    NULL,
    DATE_ADD(NOW(), INTERVAL 24 HOUR),
    FALSE
),
(
    'Email not syncing',
    'Emails are delayed by several hours. Sync issue with Microsoft Exchange server.',
    'MEDIUM',
    'ASSIGNED',
    4,
    2,
    DATE_ADD(NOW(), INTERVAL 48 HOUR),
    FALSE
),
(
    'Database connection timeout',
    'API calls timeout when querying large datasets.',
    'MEDIUM',
    'IN_PROGRESS',
    5,
    2,
    DATE_ADD(NOW(), INTERVAL 48 HOUR),
    FALSE
),
(
    'Report generation failing',
    'Custom reports not generating. Timeout after 5 minutes.',
    'LOW',
    'OPEN',
    4,
    NULL,
    DATE_ADD(NOW(), INTERVAL 72 HOUR),
    FALSE
),
(
    'Mobile app crashes on login',
    'iOS app crashes immediately after launch on iOS 14+',
    'HIGH',
    'RESOLVED',
    5,
    3,
    DATE_ADD(NOW(), INTERVAL 24 HOUR),
    FALSE
);

-- =====================================================
-- INSERT SAMPLE COMMENTS
-- =====================================================
INSERT INTO comments (ticket_id, user_id, content) VALUES
(1, 2, 'I am investigating the portal access issue. Will check the load balancer configuration.'),
(1, 4, 'Thanks for looking into this. It affects about 50 students.'),
(2, 2, 'Found the issue - sync service was stopped. Restarting now.'),
(3, 2, 'Working on optimizing the query. Will update in 2 hours.'),
(5, 3, 'Fixed the issue by updating the SSL certificate.');

-- =====================================================
-- INSERT SAMPLE AUDIT LOGS
-- =====================================================
INSERT INTO audit_logs (action, user_id, ticket_id, details) VALUES
('TICKET_CREATED', 4, 1, 'New ticket created by student for portal access issue'),
('TICKET_CREATED', 4, 2, 'New ticket created by student for email sync issue'),
('TICKET_ASSIGNED', 1, 2, 'Ticket assigned to Support Engineer (engineer@example.com)'),
('TICKET_ASSIGNED', 1, 3, 'Ticket assigned to Support Engineer (engineer@example.com)'),
('STATUS_CHANGED', 2, 2, 'Status changed from OPEN to ASSIGNED'),
('STATUS_CHANGED', 2, 3, 'Status changed from ASSIGNED to IN_PROGRESS'),
('COMMENT_ADDED', 2, 1, 'Engineer added comment on ticket'),
('COMMENT_ADDED', 4, 1, 'Student added comment on ticket'),
('STATUS_CHANGED', 3, 5, 'Status changed to RESOLVED'),
('TICKET_CLOSED', 5, 5, 'Ticket closed by student');
```

**Then press ENTER**

---

## ✅ Verify Tables Were Created

Run these commands one by one:

```sql
SHOW TABLES;
```

You should see:
```
+-----------------+
| Tables_in_itsm_db |
+-----------------+
| audit_logs      |
| comments        |
| sla_rules       |
| tickets         |
| users           |
+-----------------+
```

Check the data:
```sql
SELECT COUNT(*) FROM users;
```

Should show: `5`

```sql
SELECT * FROM users;
```

Should show all 5 users with emails

---

## 🎉 SUCCESS!

If you see all 5 tables and 5 users, your database is ready!

Now you can exit MySQL:
```sql
EXIT;
```

---

## 🚀 Next: Run Your Application

```bash
cd c:\Users\kunaa\IdeaProjects\itsm-system
mvnw.cmd spring-boot:run
```

Wait for:
```
Started ItsmSystemApplication in X.XXX seconds
Tomcat started on port(s): 8080
```

Then open:
```
http://localhost:8080/index.html
```

Login with:
```
admin@example.com
adminpass
```

---

**Done! Your database is now connected and ready!** ✅
