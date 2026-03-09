# ITSM Admin System - Quick Start Guide

## 1️⃣ Setup Database

```bash
# Create database
mysql -u root -p2005 -e "CREATE DATABASE IF NOT EXISTS itsm_db;"

# Or manually create in MySQL:
# CREATE DATABASE itsm_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

## 2️⃣ Build Project

```bash
cd itsm-system
mvn clean install
```

## 3️⃣ Run Application

```bash
mvn spring-boot:run
```

You'll see:
```
Started ItsmSystemApplication in X seconds
Tomcat started on port(s): 8080
```

## 4️⃣ Test Authentication

### Login as Admin:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@college.edu","password":"admin123"}'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "email": "admin@college.edu",
  "name": "Admin User",
  "role": "ADMIN"
}
```

**Save this token for next requests!**

## 5️⃣ Test Admin Endpoints

### View All Users:
```bash
curl -X GET http://localhost:8080/api/admin/users \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### Create New User:
```bash
curl -X POST http://localhost:8080/api/admin/users \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "email":"newengineer@college.edu",
    "name":"New Engineer",
    "password":"password123",
    "role":"ENGINEER"
  }'
```

### View All Tickets:
```bash
curl -X GET http://localhost:8080/api/admin/tickets \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### Assign Engineer to Ticket:
```bash
curl -X PUT http://localhost:8080/api/admin/tickets/1/assign/2 \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### Update Ticket Status:
```bash
curl -X PUT http://localhost:8080/api/admin/tickets/1/status \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{"status":"IN_PROGRESS"}'
```

### View All Assets:
```bash
curl -X GET http://localhost:8080/api/admin/assets \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### Create New Asset:
```bash
curl -X POST http://localhost:8080/api/admin/assets \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "assetCode":"LAP-003",
    "name":"MacBook Pro",
    "category":"LAPTOP",
    "status":"AVAILABLE",
    "location":"Office A"
  }'
```

## 6️⃣ Available Features

### ✅ Authentication
- POST `/api/auth/login` - Get JWT token

### ✅ User Directory
- GET `/api/admin/users` - View all users
- GET `/api/admin/users/{id}` - View single user
- POST `/api/admin/users` - Create new user (BCrypt password)

### ✅ Ticket Queue
- GET `/api/admin/tickets` - View all tickets
- GET `/api/admin/tickets/{id}` - View single ticket
- GET `/api/admin/tickets/status/{status}` - Filter by status
- PUT `/api/admin/tickets/{id}/assign/{engineerId}` - Assign engineer
- PUT `/api/admin/tickets/{id}/status` - Change status

### ✅ Asset Management
- GET `/api/admin/assets` - View all assets
- GET `/api/admin/assets/{id}` - View single asset
- POST `/api/admin/assets` - Add new asset

## 7️⃣ Sample Data

**Default Admin User:**
- Email: `admin@college.edu`
- Password: `admin123`
- Role: ADMIN

**Sample Engineers:**
- Email: `engineer1@college.edu` | Password: `admin123` | Role: ENGINEER
- Email: `engineer2@college.edu` | Password: `admin123` | Role: ENGINEER

**Sample Tickets:**
- ID 1: Server Down (OPEN, CRITICAL)
- ID 2: Email Issue (ASSIGNED, HIGH)
- ID 3: Password Reset (RESOLVED, MEDIUM)

**Sample Assets:**
- LAP-001: Dell Laptop (AVAILABLE)
- DES-001: HP Desktop (ASSIGNED)
- PRN-001: Canon Printer (AVAILABLE)

## 📁 Project Structure

```
src/main/java/com/itsm/itsmsystem/
├── config/
│   ├── SecurityConfig.java (Spring Security setup)
│   ├── JwtUtil.java (Token generation/validation)
│   └── JwtAuthenticationFilter.java (JWT filter)
├── controller/
│   ├── AuthController.java
│   ├── UserController.java
│   ├── TicketController.java
│   └── AssetController.java
├── service/
│   ├── UserService.java
│   ├── TicketService.java
│   └── AssetService.java
├── repository/
│   ├── UserRepository.java
│   ├── TicketRepository.java
│   └── AssetRepository.java
└── model/entity/
    ├── AdminUser.java
    ├── Ticket.java
    └── Asset.java
```

## 🔒 Security Features

- ✅ JWT Token authentication (Bearer token)
- ✅ BCrypt password encryption
- ✅ @PreAuthorize("hasRole('ADMIN')") on all admin endpoints
- ✅ Stateless session management
- ✅ No refresh tokens (simple 24-hour expiration)

## 🚀 Next Steps

1. Test all endpoints with the token
2. Create additional users with different roles
3. Add more tickets and assets
4. Modify statuses and assignments
5. Deploy to production (update JWT secret in properties)

## ⚠️ Important Notes

- **JWT Secret**: Change in `application.properties` before production
- **Database**: Use MySQL 8.0+
- **Java**: Requires Java 17+
- **Port**: Default 8080 (configurable in properties)

---

**Ready to use!** All endpoints are protected with ADMIN role. 🎯
