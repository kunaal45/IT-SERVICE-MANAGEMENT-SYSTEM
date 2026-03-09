# 🎯 FINAL PROJECT STRUCTURE - QUICK REFERENCE

## ✅ USE THESE PACKAGES (Active Code)

```
com.itsm.itsmsystem
│
├── 📦 enums/
│   ├── Role.java (ADMIN, ENGINEER, FACULTY)
│   ├── TicketStatus.java (OPEN, ASSIGNED, IN_PROGRESS, RESOLVED, CLOSED)
│   ├── TicketPriority.java (LOW, MEDIUM, HIGH, CRITICAL)
│   └── IssueCategory.java (HARDWARE, SOFTWARE, NETWORK, etc.)
│
├── 📦 dto/
│   ├── CreateTicketRequest.java
│   ├── LoginRequest.java
│   ├── LoginResponse.java
│   └── DashboardStats.java
│
├── 📦 exception/
│   ├── ResourceNotFoundException.java
│   ├── UnauthorizedException.java
│   └── InvalidStatusTransitionException.java
│
├── 📦 model.entity/
│   ├── User.java
│   ├── Ticket.java
│   └── AuditLog.java
│
├── 📦 repository/
│   ├── UserRepository.java
│   ├── TicketRepository.java
│   └── AuditLogRepository.java
│
├── 📦 security/
│   ├── JwtUtil.java ⭐ USE THIS
│   ├── JwtAuthenticationFilter.java ⭐ USE THIS
│   └── UserDetailsServiceImpl.java
│
├── 📦 service/
│   ├── TicketService.java
│   └── UserService.java
│
├── 📦 controller/ (lowercase c) ⭐ USE THIS
│   ├── AuthController.java
│   └── TicketController.java
│
├── 📦 config/
│   └── SecurityConfig.java ⭐ ONLY ACTIVE FILE
│
└── DataInitializer.java
```

---

## ❌ IGNORE THESE (Deprecated)

```
com.itsm.itsmsystem
│
├── Controller/ (capital C) ❌ ALL DEPRECATED
│   ├── TicketApiController.java
│   ├── AuthController.java
│   ├── TicketController.java
│   └── ... (all empty placeholders)
│
└── config/ (except SecurityConfig)
    ├── JwtUtil.java ❌ DEPRECATED
    ├── JwtAuthenticationFilter.java ❌ DEPRECATED
    └── CorsConfig.java ❌ DEPRECATED
```

---

## 🔑 Key Differences

| Feature | Old (Deprecated) | New (Active) |
|---------|------------------|--------------|
| **Package** | `Controller/` (capital C) | `controller/` (lowercase) |
| **JWT Location** | `config.JwtUtil` | `security.JwtUtil` |
| **Role Type** | `String "ADMIN"` | `Role.ADMIN` enum |
| **Status Type** | `String "OPEN"` | `TicketStatus.OPEN` enum |
| **Priority Type** | `String "HIGH"` | `TicketPriority.HIGH` enum |
| **Method Names** | `extractUsername()` | `extractEmail()` |
| **Service Methods** | `getTicketsAssignedTo()` | `getTicketsByRole()` |

---

## 📝 Import Guide

### ✅ Correct Imports:

```java
// Enums
import com.itsm.itsmsystem.enums.Role;
import com.itsm.itsmsystem.enums.TicketStatus;
import com.itsm.itsmsystem.enums.TicketPriority;
import com.itsm.itsmsystem.enums.IssueCategory;

// Security
import com.itsm.itsmsystem.security.JwtUtil;
import com.itsm.itsmsystem.security.JwtAuthenticationFilter;
import com.itsm.itsmsystem.security.UserDetailsServiceImpl;

// Config
import com.itsm.itsmsystem.config.SecurityConfig;

// Controllers
import com.itsm.itsmsystem.controller.AuthController;
import com.itsm.itsmsystem.controller.TicketController;

// Services
import com.itsm.itsmsystem.service.TicketService;
import com.itsm.itsmsystem.service.UserService;
```

### ❌ Wrong Imports (Don't Use):

```java
// DON'T USE
import com.itsm.itsmsystem.config.JwtUtil; ❌
import com.itsm.itsmsystem.config.JwtAuthenticationFilter; ❌
import com.itsm.itsmsystem.Controller.*; ❌
```

---

## 🚀 Quick Start

```bash
# 1. Clean build
mvn clean compile

# 2. Run
mvn spring-boot:run

# 3. Test login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@college.edu","password":"admin123"}'

# 4. Test tickets
curl -X GET http://localhost:8080/api/tickets \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

## 📊 API Endpoints

```
Authentication:
POST   /api/auth/login
GET    /api/auth/user

Tickets:
GET    /api/tickets
POST   /api/tickets
GET    /api/tickets/{id}
GET    /api/tickets/dashboard
POST   /api/tickets/{id}/assign/{engineerId}
PUT    /api/tickets/{id}/start
PUT    /api/tickets/{id}/resolve
PUT    /api/tickets/{id}/close
```

---

## 🔐 Test Credentials

```
Admin:
  Email: admin@college.edu
  Password: admin123

Engineer:
  Email: priya@college.edu
  Password: eng123

Faculty:
  Email: rajesh@college.edu
  Password: faculty123
```

---

## ✅ Verification Checklist

After `mvn clean compile`:

- [ ] Build succeeds
- [ ] No compilation errors
- [ ] Controllers in `controller/` package (lowercase)
- [ ] JWT files in `security/` package
- [ ] All enums used (no strings)
- [ ] Application starts successfully
- [ ] Login works
- [ ] Role-based access works

---

**All legacy files neutralized! Use only the active packages listed above.** ✅
