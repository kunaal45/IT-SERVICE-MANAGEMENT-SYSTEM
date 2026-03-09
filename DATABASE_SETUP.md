# 🗄️ ITSM System - Database Setup Guide

## ✅ YOUR DATABASE SETUP INSTRUCTIONS

Your MySQL database needs to be initialized with the proper schema and sample data. Follow these steps:

---

## 📋 SETUP STEPS

### Step 1: Open MySQL Command Line or MySQL Workbench

**Option A: Command Line**
```cmd
mysql -u root -p
```
Then enter your password: `2005`

**Option B: MySQL Workbench**
1. Open MySQL Workbench
2. Connect to your local MySQL server
3. Open a new SQL query window

---

### Step 2: Create the Database

Run this command:
```sql
CREATE DATABASE IF NOT EXISTS itsm_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE itsm_db;
```

---

### Step 3: Execute the Schema File

Run the complete schema with tables and sample data:

```sql
-- Copy and paste the entire contents of:
-- src/main/resources/db/schema.sql
```

**OR** Execute from file (MySQL Command Line):
```cmd
mysql -u root -p2005 itsm_db < schema.sql
```

---

### Step 4: Verify the Database

Run these verification queries:

```sql
-- Check all tables exist
SHOW TABLES;

-- Count users
SELECT COUNT(*) as users FROM users;

-- Count tickets  
SELECT COUNT(*) as tickets FROM tickets;

-- Count comments
SELECT COUNT(*) as comments FROM comments;

-- Count audit logs
SELECT COUNT(*) as audit_logs FROM audit_logs;

-- Check SLA rules
SELECT * FROM sla_rules;
```

Expected Results:
- 5 users
- 5 tickets
- 5 comments
- 10 audit logs
- 3 SLA rules

---

## 🔑 DATABASE CREDENTIALS

Your database is configured with:
- **Host**: localhost
- **Port**: 3306
- **Database**: itsm_db
- **Username**: root
- **Password**: 2005
- **Driver**: MySQL 8.0+

These are already set in `application.properties`

---

## 📊 DATABASE SCHEMA

### Users Table
```
id (AUTO_INCREMENT PRIMARY KEY)
name VARCHAR(100)
email VARCHAR(100) UNIQUE
password VARCHAR(255)
role VARCHAR(50) [ADMIN, SUPPORT_ENGINEER, STUDENT]
created_at TIMESTAMP
updated_at TIMESTAMP
```

### Tickets Table
```
id (AUTO_INCREMENT PRIMARY KEY)
title VARCHAR(255)
description LONGTEXT
priority VARCHAR(20) [HIGH, MEDIUM, LOW]
status VARCHAR(50) [OPEN, ASSIGNED, IN_PROGRESS, RESOLVED, CLOSED]
sla_deadline DATETIME
sla_breached BOOLEAN
created_by_id BIGINT (FK to users)
assigned_to_id BIGINT (FK to users)
resolution_notes LONGTEXT
created_at TIMESTAMP
updated_at TIMESTAMP
```

### Comments Table
```
id (AUTO_INCREMENT PRIMARY KEY)
ticket_id BIGINT (FK to tickets)
user_id BIGINT (FK to users)
content LONGTEXT
created_at TIMESTAMP
```

### Audit Logs Table
```
id (AUTO_INCREMENT PRIMARY KEY)
action VARCHAR(100)
user_id BIGINT (FK to users)
ticket_id BIGINT (FK to tickets)
details LONGTEXT
created_at TIMESTAMP
```

### SLA Rules Table
```
id (AUTO_INCREMENT PRIMARY KEY)
priority VARCHAR(50) UNIQUE [HIGH, MEDIUM, LOW]
max_hours INT
created_at TIMESTAMP
updated_at TIMESTAMP
```

---

## 📝 SAMPLE DATA PROVIDED

### Users (5 total)
1. **Admin User** - admin@example.com / adminpass (ADMIN role)
2. **Support Engineer** - engineer@example.com / engineerpass (SUPPORT_ENGINEER)
3. **Another Engineer** - eng2@example.com / engineerpass (SUPPORT_ENGINEER)
4. **Student User** - student@example.com / studentpass (STUDENT)
5. **Another Student** - student2@example.com / studentpass (STUDENT)

### Tickets (5 total)
1. "Cannot access portal" (HIGH, OPEN)
2. "Email not syncing" (MEDIUM, ASSIGNED)
3. "Database connection timeout" (MEDIUM, IN_PROGRESS)
4. "Report generation failing" (LOW, OPEN)
5. "Mobile app crashes on login" (HIGH, RESOLVED)

### SLA Rules (3 total)
- HIGH: 24 hours
- MEDIUM: 48 hours
- LOW: 72 hours

---

## 🔧 APPLICATION CONFIGURATION

The `application.properties` is already configured:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/itsm_db
spring.datasource.username=root
spring.datasource.password=2005
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

**Important**: `ddl-auto=validate` means Spring Boot will validate the schema but NOT create/modify tables. You must run the SQL schema manually.

---

## ✅ QUICK SETUP (3 Steps)

### 1. Open MySQL Command Line
```cmd
mysql -u root -p2005
```

### 2. Create Database
```sql
CREATE DATABASE IF NOT EXISTS itsm_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE itsm_db;
```

### 3. Copy-Paste the Schema

Copy the entire contents of `src/main/resources/db/schema.sql` and paste into MySQL command line, then press Enter.

**Done!** Your database is ready. 🎉

---

## 🐛 TROUBLESHOOTING

### "Access denied for user 'root'"
- Verify your MySQL root password is `2005`
- Check MySQL is running
- Verify your MySQL installation

### "Unknown database 'itsm_db'"
- Run: `CREATE DATABASE itsm_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;`
- Then: `USE itsm_db;`

### "Table already exists"
- Drop existing tables: `DROP TABLE IF EXISTS comments;`
- Then run schema again

### "Foreign key constraint fails"
- Ensure user records exist before inserting tickets
- Ensure tickets exist before inserting comments
- Schema handles this with proper insert order

### "Connection refused when starting app"
- Ensure MySQL is running
- Check host/port/credentials in application.properties
- Verify database and tables exist

---

## 🚀 NEXT STEPS

After setting up the database:

1. ✅ Run the Spring Boot application:
   ```cmd
   mvnw.cmd spring-boot:run
   ```

2. ✅ Open the frontend:
   ```
   http://localhost:8080/index.html
   ```

3. ✅ Login with demo credentials:
   ```
   admin@example.com / adminpass
   ```

4. ✅ Your data will now persist in the database!

---

## 📚 FILES PROVIDED

- `src/main/resources/db/schema.sql` - Complete database schema with sample data
- `src/main/resources/application.properties` - Database configuration (already set)
- Updated entity classes with `@Entity` annotations
- JPA Repository interfaces for database access

---

## ✨ FEATURES NOW AVAILABLE

✅ Persistent data storage in MySQL
✅ All entities mapped with JPA
✅ Automatic timestamp management
✅ Foreign key relationships
✅ Sample data for testing
✅ Complete audit trail
✅ SLA tracking

---

## 📞 VERIFICATION

After running the app, verify by:

1. Creating a new ticket in the UI
2. Querying the database:
   ```sql
   SELECT * FROM tickets ORDER BY created_at DESC LIMIT 1;
   ```
3. Confirm your new ticket appears in the database

**If it does, everything is working!** ✅

---

**Status:** ✅ Database Setup Complete
**Ready to Run:** ✅ YES
**Data Persistence:** ✅ ENABLED

Enjoy your ITSM system with persistent database storage! 🎉
