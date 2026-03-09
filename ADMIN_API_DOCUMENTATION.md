# ITSM Admin API Documentation

## Overview
Minimal Spring Boot ITSM system with JWT authentication. ADMIN-only features for ticket queue, user directory, and asset management.

## Database Setup

1. Create database:
```sql
CREATE DATABASE itsm_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. Load sample data:
```sql
mysql -u root -p itsm_db < src/main/resources/db/seed-data.sql
```

## Build & Run

```bash
# Build
mvn clean install

# Run
mvn spring-boot:run
```

Server runs on: `http://localhost:8080`

---

## Authentication

### Login
**POST** `/api/auth/login`

```json
{
  "email": "admin@college.edu",
  "password": "admin123"
}
```

**Response:**
```json
{
  "token": "eyJhbGc...",
  "email": "admin@college.edu",
  "name": "Admin User",
  "role": "ADMIN"
}
```

**Usage:** Include token in all admin requests:
```
Authorization: Bearer <token>
```

---

## API Endpoints

### 1. User Directory (Admin Only)

#### Get All Users
**GET** `/api/admin/users`
- Headers: `Authorization: Bearer <token>`
- Response: List of all users

#### Get User by ID
**GET** `/api/admin/users/{id}`
- Path: User ID
- Response: User details

#### Create New User
**POST** `/api/admin/users`
- Headers: `Authorization: Bearer <token>`
- Body:
```json
{
  "email": "newuser@college.edu",
  "name": "New User",
  "password": "password123",
  "role": "ENGINEER"
}
```

**Password:** Automatically BCrypt encoded
**Roles:** ADMIN, ENGINEER, USER

---

### 2. Ticket Queue (Admin Only)

#### Get All Tickets
**GET** `/api/admin/tickets`
- Response: All tickets with full details

#### Get Ticket by ID
**GET** `/api/admin/tickets/{id}`
- Path: Ticket ID
- Response: Single ticket

#### Get Tickets by Status
**GET** `/api/admin/tickets/status/{status}`
- Path: Status value (OPEN, ASSIGNED, IN_PROGRESS, RESOLVED, CLOSED)
- Response: Filtered tickets

#### Assign Engineer to Ticket
**PUT** `/api/admin/tickets/{id}/assign/{engineerId}`
- Path: Ticket ID and Engineer User ID
- Response: Updated ticket with status ASSIGNED

#### Update Ticket Status
**PUT** `/api/admin/tickets/{id}/status`
- Headers: `Authorization: Bearer <token>`
- Body:
```json
{
  "status": "IN_PROGRESS"
}
```

**Valid Statuses:**
- OPEN
- ASSIGNED
- IN_PROGRESS
- RESOLVED
- CLOSED

---

### 3. Asset Management (Admin Only)

#### Get All Assets
**GET** `/api/admin/assets`
- Response: All assets in system

#### Get Asset by ID
**GET** `/api/admin/assets/{id}`
- Path: Asset ID
- Response: Single asset details

#### Create New Asset
**POST** `/api/admin/assets`
- Headers: `Authorization: Bearer <token>`
- Body:
```json
{
  "assetCode": "LAP-002",
  "name": "Lenovo ThinkPad",
  "category": "LAPTOP",
  "status": "AVAILABLE",
  "location": "Office D"
}
```

**Asset Categories:**
- LAPTOP
- DESKTOP
- PRINTER
- MONITOR
- SERVER
- etc.

**Asset Status:**
- AVAILABLE
- ASSIGNED
- MAINTENANCE
- RETIRED

---

## Sample cURL Requests

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@college.edu","password":"admin123"}'
```

### Get All Users
```bash
curl -X GET http://localhost:8080/api/admin/users \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Create User
```bash
curl -X POST http://localhost:8080/api/admin/users \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "email":"newengineer@college.edu",
    "name":"New Engineer",
    "password":"password123",
    "role":"ENGINEER"
  }'
```

### Get All Tickets
```bash
curl -X GET http://localhost:8080/api/admin/tickets \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Assign Engineer to Ticket
```bash
curl -X PUT http://localhost:8080/api/admin/tickets/1/assign/2 \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Update Ticket Status
```bash
curl -X PUT http://localhost:8080/api/admin/tickets/1/status \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"status":"IN_PROGRESS"}'
```

### Get All Assets
```bash
curl -X GET http://localhost:8080/api/admin/assets \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Create Asset
```bash
curl -X POST http://localhost:8080/api/admin/assets \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "assetCode":"DES-002",
    "name":"HP Desktop Pro",
    "category":"DESKTOP",
    "status":"AVAILABLE",
    "location":"Office E"
  }'
```

---

## Sample Login Credentials

| Email | Password | Role |
|-------|----------|------|
| admin@college.edu | admin123 | ADMIN |
| engineer1@college.edu | admin123 | ENGINEER |
| engineer2@college.edu | admin123 | ENGINEER |

---

## Architecture

```
Controller
    ↓
Service
    ↓
Repository
    ↓
Entity/Database
```

### Files Structure:
- **Entity**: AdminUser, Ticket, Asset
- **Repository**: UserRepository, TicketRepository, AssetRepository
- **Service**: UserService, TicketService, AssetService
- **Controller**: AuthController, UserController, TicketController, AssetController
- **Config**: SecurityConfig, JwtUtil, JwtAuthenticationFilter

---

## Security

- ✅ JWT Token-based authentication
- ✅ BCrypt password encoding
- ✅ @PreAuthorize("hasRole('ADMIN')") for protected endpoints
- ✅ Stateless session management
- ✅ No refresh tokens (simple design)

---

## Error Handling

All errors return:
```json
{
  "error": "Error message"
}
```

Common errors:
- 401 Unauthorized (missing/invalid token)
- 403 Forbidden (not ADMIN role)
- 404 Not Found (resource doesn't exist)
- 400 Bad Request (invalid input)

---

## Notes

- All timestamps are in UTC
- Password minimum: 8 characters (best practice)
- Token expiration: 24 hours
- No pagination (returns all records)
- Simple error handling (no custom exceptions)
- No soft deletes
- No audit logging

---

**Version**: 1.0  
**Last Updated**: February 18, 2026
