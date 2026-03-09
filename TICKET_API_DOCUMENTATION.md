╔════════════════════════════════════════════════════════════════════════════╗
║                                                                            ║
║            ✅ TICKET API ENDPOINTS - COMPLETE DOCUMENTATION ✅             ║
║                                                                            ║
╚════════════════════════════════════════════════════════════════════════════╝


🎯 TICKET CONTROLLER IMPLEMENTED!
═══════════════════════════════════════════════════════════════════════════

I've created a complete TicketController with all CRUD operations and workflow
management for your ITSM system.


📋 AVAILABLE ENDPOINTS:
═══════════════════════════════════════════════════════════════════════════


1️⃣ GET ALL TICKETS (Role-based)
────────────────────────────────

Endpoint: GET /api/tickets
Headers: Authorization: Bearer <JWT_TOKEN>

Returns:
  - ADMIN: All tickets in the system
  - ENGINEER: Tickets assigned to you
  - FACULTY: Tickets created by you

Example Response:
[
  {
    "id": 1,
    "title": "Lab PC-05 monitor not working",
    "description": "Monitor shows no display...",
    "status": "ASSIGNED",
    "priority": "HIGH",
    "category": "HARDWARE",
    "location": "CSE Lab 1",
    "createdBy": { "id": 5, "name": "Dr. Rajesh Kumar", "email": "rajesh@college.edu" },
    "assignedTo": { "id": 2, "name": "Mr. Kumar", "email": "kumar@college.edu" },
    "createdAt": "2026-03-03T10:00:00",
    "resolutionNotes": null
  }
]


2️⃣ GET SINGLE TICKET
──────────────────────

Endpoint: GET /api/tickets/{id}
Headers: Authorization: Bearer <JWT_TOKEN>

Example: GET /api/tickets/5

Returns: Single ticket details


3️⃣ CREATE NEW TICKET (Faculty/Admin only)
───────────────────────────────────────────

Endpoint: POST /api/tickets
Headers: 
  - Authorization: Bearer <JWT_TOKEN>
  - Content-Type: application/json

Request Body:
{
  "title": "Projector not working in Room 205",
  "description": "Projector shows no signal from laptop. Tried different cables.",
  "priority": "HIGH",
  "category": "HARDWARE",
  "location": "CSE Room 205"
}

Response: Created ticket with status OPEN


4️⃣ ASSIGN TICKET TO ENGINEER (Admin only)
───────────────────────────────────────────

Endpoint: PUT /api/tickets/{ticketId}/assign/{engineerId}
Headers: Authorization: Bearer <JWT_TOKEN>

Example: PUT /api/tickets/5/assign/2

Action:
  - Assigns ticket to specified engineer
  - Changes status to ASSIGNED
  - Creates audit log entry

Response: Updated ticket


5️⃣ START WORKING ON TICKET (Engineer only)
────────────────────────────────────────────

Endpoint: PUT /api/tickets/{ticketId}/start
Headers: Authorization: Bearer <JWT_TOKEN>

Example: PUT /api/tickets/5/start

Action:
  - Changes status to IN_PROGRESS
  - Can only start tickets assigned to you
  - Creates audit log entry

Response: Updated ticket


6️⃣ RESOLVE TICKET (Engineer/Admin only)
─────────────────────────────────────────

Endpoint: PUT /api/tickets/{ticketId}/resolve
Headers: Authorization: Bearer <JWT_TOKEN>

Example: PUT /api/tickets/5/resolve

Action:
  - Changes status to RESOLVED
  - Sets resolvedAt timestamp
  - Creates audit log entry

Response: Updated ticket


7️⃣ CLOSE TICKET (Faculty only - ticket creator)
─────────────────────────────────────────────────

Endpoint: PUT /api/tickets/{ticketId}/close
Headers: Authorization: Bearer <JWT_TOKEN>

Example: PUT /api/tickets/5/close

Action:
  - Changes status to CLOSED
  - Sets closedAt timestamp
  - Only ticket creator can close
  - Creates audit log entry

Response: Updated ticket


8️⃣ UPDATE RESOLUTION NOTES (Engineer only)
────────────────────────────────────────────

Endpoint: PUT /api/tickets/{ticketId}/notes
Headers: 
  - Authorization: Bearer <JWT_TOKEN>
  - Content-Type: application/json

Request Body:
{
  "notes": "Replaced HDMI cable and remote batteries. Projector working fine now. Tested with multiple inputs."
}

Response: Updated ticket with resolution notes


9️⃣ GET DASHBOARD STATISTICS
─────────────────────────────

Endpoint: GET /api/tickets/stats
Headers: Authorization: Bearer <JWT_TOKEN>

Response:
{
  "totalTickets": 20,
  "openTickets": 7,
  "assignedTickets": 3,
  "inProgressTickets": 6,
  "resolvedTickets": 4,
  "closedTickets": 0
}


🔟 GET TICKETS BY STATUS
────────────────────────

Endpoint: GET /api/tickets/status/{status}
Headers: Authorization: Bearer <JWT_TOKEN>

Example: GET /api/tickets/status/OPEN

Response: List of tickets with specified status


1️⃣1️⃣ HEALTH CHECK
──────────────────

Endpoint: GET /api/tickets/health
Headers: None required

Response:
{
  "status": "OK",
  "service": "Ticket Service",
  "message": "Ticket management system is running"
}


═══════════════════════════════════════════════════════════════════════════


🔐 SECURITY & PERMISSIONS:
═══════════════════════════════════════════════════════════════════════════

ADMIN can:
  ✓ View all tickets
  ✓ Create tickets
  ✓ Assign tickets to engineers
  ✓ Resolve tickets
  ✓ View dashboard stats

ENGINEER can:
  ✓ View assigned tickets
  ✓ Start working (IN_PROGRESS)
  ✓ Update resolution notes
  ✓ Resolve tickets
  ✓ View dashboard stats

FACULTY can:
  ✓ View their created tickets
  ✓ Create new tickets
  ✓ Close their tickets (after resolved)
  ✓ View dashboard stats


═══════════════════════════════════════════════════════════════════════════


📊 TICKET WORKFLOW:
═══════════════════════════════════════════════════════════════════════════

1. Faculty creates ticket
   ↓
   Status: OPEN

2. Admin assigns to engineer
   ↓
   Status: ASSIGNED

3. Engineer starts work
   ↓
   Status: IN_PROGRESS

4. Engineer adds resolution notes (optional)
   ↓
   (Status remains IN_PROGRESS)

5. Engineer resolves ticket
   ↓
   Status: RESOLVED

6. Faculty closes ticket
   ↓
   Status: CLOSED


═══════════════════════════════════════════════════════════════════════════


🧪 TESTING THE ENDPOINTS:
═══════════════════════════════════════════════════════════════════════════

Using cURL:

1. Login first to get JWT token:
───────────────────────────────

curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@college.edu",
    "password": "admin123"
  }'

Save the "token" from response.


2. Get all tickets:
──────────────────

curl -X GET http://localhost:8080/api/tickets \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"


3. Create new ticket (as faculty):
──────────────────────────────────

curl -X POST http://localhost:8080/api/tickets \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Printer not working in staff room",
    "description": "Printer showing paper jam error",
    "priority": "HIGH",
    "category": "HARDWARE",
    "location": "Staff Room"
  }'


4. Assign ticket (as admin):
────────────────────────────

curl -X PUT http://localhost:8080/api/tickets/1/assign/2 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"


5. Start working (as engineer):
───────────────────────────────

curl -X PUT http://localhost:8080/api/tickets/1/start \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"


6. Add resolution notes:
───────────────────────

curl -X PUT http://localhost:8080/api/tickets/1/notes \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "notes": "Cleared paper jam. Cleaned rollers. Printer working fine now."
  }'


7. Resolve ticket:
─────────────────

curl -X PUT http://localhost:8080/api/tickets/1/resolve \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"


═══════════════════════════════════════════════════════════════════════════


🎯 DEMO DATA AVAILABLE:
═══════════════════════════════════════════════════════════════════════════

When you run the application, 20 demo tickets are automatically created:

• 7 OPEN tickets - Ready to be assigned
• 3 ASSIGNED tickets - Waiting for engineer to start
• 6 IN_PROGRESS tickets - Being worked on
• 4 RESOLVED tickets - Completed

You can immediately test all endpoints with this demo data!


═══════════════════════════════════════════════════════════════════════════


✅ WHAT'S INCLUDED:
═══════════════════════════════════════════════════════════════════════════

✓ Complete TicketController with 11 endpoints
✓ Role-based access control
✓ JWT authentication on all endpoints
✓ Input validation
✓ Error handling
✓ Audit logging
✓ Workflow validation
✓ Resolution notes tracking
✓ Dashboard statistics
✓ Health check endpoint


═══════════════════════════════════════════════════════════════════════════


🚀 TO USE:
═══════════════════════════════════════════════════════════════════════════

1. Run the application:
   .\mvnw.cmd spring-boot:run

2. Login to get JWT token:
   POST /api/auth/login

3. Use the token in Authorization header:
   Authorization: Bearer <your_token>

4. Call any ticket endpoint!


═══════════════════════════════════════════════════════════════════════════

              🎉 TICKET API IS COMPLETE AND READY TO USE! 🎉

═══════════════════════════════════════════════════════════════════════════
