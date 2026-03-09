# Frontend Integration Guide - Complete Backend Connection

## 1. Login and Store JWT Token

```javascript
// Login Function
async function login(email, password) {
    try {
        const response = await fetch('http://localhost:8080/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email, password })
        });

        if (!response.ok) {
            throw new Error('Login failed');
        }

        const data = await response.json();
        
        // Store token and user info in localStorage
        localStorage.setItem('jwt_token', data.token);
        localStorage.setItem('user_email', data.email);
        localStorage.setItem('user_name', data.name);
        localStorage.setItem('user_role', data.role);
        localStorage.setItem('user_id', data.id);

        // Redirect based on role
        if (data.role === 'ADMIN') {
            window.location.href = '/admin-dashboard.html';
        } else if (data.role === 'ENGINEER' || data.role === 'SUPPORT_ENGINEER') {
            window.location.href = '/engineer-dashboard.html';
        } else if (data.role === 'FACULTY') {
            window.location.href = '/faculty-dashboard.html';
        }

    } catch (error) {
        console.error('Login error:', error);
        alert('Invalid credentials');
    }
}
```

## 2. Get Authorization Headers

```javascript
function getAuthHeaders() {
    const token = localStorage.getItem('jwt_token');
    return {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
    };
}
```

## 3. Fetch All Tickets (Role-Based)

```javascript
// Admin gets all tickets
// Engineer gets assigned tickets
// Faculty gets created tickets
async function fetchTickets() {
    try {
        const response = await fetch('http://localhost:8080/api/tickets', {
            method: 'GET',
            headers: getAuthHeaders()
        });

        if (response.status === 401 || response.status === 403) {
            // Token expired or unauthorized
            logout();
            return;
        }

        const tickets = await response.json();
        return tickets;
    } catch (error) {
        console.error('Error fetching tickets:', error);
        return [];
    }
}
```

## 4. Create Ticket (Faculty/Admin Only)

```javascript
async function createTicket(title, description, priority, category, location) {
    try {
        const response = await fetch('http://localhost:8080/api/tickets', {
            method: 'POST',
            headers: getAuthHeaders(),
            body: JSON.stringify({
                title: title,
                description: description,
                priority: priority,
                category: category,
                location: location
            })
        });

        if (!response.ok) {
            throw new Error('Failed to create ticket');
        }

        const ticket = await response.json();
        alert('Ticket created successfully!');
        return ticket;
    } catch (error) {
        console.error('Error creating ticket:', error);
        alert('Failed to create ticket');
    }
}
```

## 5. Assign Ticket to Engineer (Admin Only)

```javascript
async function assignTicket(ticketId, engineerId) {
    try {
        const response = await fetch(
            `http://localhost:8080/api/tickets/${ticketId}/assign/${engineerId}`,
            {
                method: 'POST',
                headers: getAuthHeaders()
            }
        );

        if (!response.ok) {
            throw new Error('Failed to assign ticket');
        }

        const ticket = await response.json();
        alert('Ticket assigned successfully!');
        return ticket;
    } catch (error) {
        console.error('Error assigning ticket:', error);
        alert('Failed to assign ticket');
    }
}
```

## 6. Resolve Ticket (Engineer/Admin Only)

```javascript
async function resolveTicket(ticketId) {
    const userName = localStorage.getItem('user_name');
    const userEmail = localStorage.getItem('user_email');
    const userRole = localStorage.getItem('user_role');

    try {
        const response = await fetch(
            `http://localhost:8080/api/tickets/${ticketId}/resolve?userRole=${userRole}&agentName=${encodeURIComponent(userName)}&agentEmail=${encodeURIComponent(userEmail)}`,
            {
                method: 'PUT',
                headers: getAuthHeaders()
            }
        );

        if (!response.ok) {
            const error = await response.text();
            throw new Error(error);
        }

        const ticket = await response.json();
        alert('Ticket resolved successfully!');
        return ticket;
    } catch (error) {
        console.error('Error resolving ticket:', error);
        alert(error.message || 'Failed to resolve ticket');
    }
}
```

## 7. Close Ticket (Faculty Only)

```javascript
async function closeTicket(ticketId) {
    const userName = localStorage.getItem('user_name');
    const userEmail = localStorage.getItem('user_email');
    const userRole = localStorage.getItem('user_role');

    try {
        const response = await fetch(
            `http://localhost:8080/api/tickets/${ticketId}/close?userRole=${userRole}&agentName=${encodeURIComponent(userName)}&agentEmail=${encodeURIComponent(userEmail)}`,
            {
                method: 'PUT',
                headers: getAuthHeaders()
            }
        );

        if (!response.ok) {
            const error = await response.text();
            throw new Error(error);
        }

        const ticket = await response.json();
        alert('Ticket closed successfully!');
        return ticket;
    } catch (error) {
        console.error('Error closing ticket:', error);
        alert(error.message || 'Failed to close ticket');
    }
}
```

## 8. Get Current User Info

```javascript
async function getCurrentUser() {
    try {
        const response = await fetch('http://localhost:8080/api/auth/user', {
            method: 'GET',
            headers: getAuthHeaders()
        });

        if (!response.ok) {
            logout();
            return null;
        }

        return await response.json();
    } catch (error) {
        console.error('Error getting user info:', error);
        return null;
    }
}
```

## 9. Logout Function

```javascript
function logout() {
    localStorage.removeItem('jwt_token');
    localStorage.removeItem('user_email');
    localStorage.removeItem('user_name');
    localStorage.removeItem('user_role');
    localStorage.removeItem('user_id');
    window.location.href = '/index.html';
}
```

## 10. Check Authentication on Page Load

```javascript
function checkAuth() {
    const token = localStorage.getItem('jwt_token');
    const currentPage = window.location.pathname;

    // Allow access to login page
    if (currentPage.includes('index.html') || currentPage === '/') {
        return true;
    }

    // Redirect to login if no token
    if (!token) {
        window.location.href = '/index.html';
        return false;
    }

    return true;
}

// Call on every protected page
document.addEventListener('DOMContentLoaded', function() {
    checkAuth();
});
```

## 11. Role-Based Button Visibility

```javascript
function showButtonsByRole() {
    const userRole = localStorage.getItem('user_role');

    // Show/hide buttons based on role
    if (userRole === 'ENGINEER' || userRole === 'SUPPORT_ENGINEER') {
        document.getElementById('resolveButton').style.display = 'block';
        document.getElementById('closeButton').style.display = 'none';
    } else if (userRole === 'FACULTY') {
        document.getElementById('resolveButton').style.display = 'none';
        document.getElementById('closeButton').style.display = 'block';
    } else if (userRole === 'ADMIN') {
        document.getElementById('resolveButton').style.display = 'block';
        document.getElementById('assignButton').style.display = 'block';
    }
}
```

## 12. Error Handling Template

```javascript
async function handleAPICall(apiFunction) {
    try {
        return await apiFunction();
    } catch (error) {
        if (error.message.includes('401') || error.message.includes('403')) {
            alert('Session expired. Please login again.');
            logout();
        } else {
            console.error('API Error:', error);
            alert('An error occurred. Please try again.');
        }
    }
}
```

## 13. Complete Login Page Example

```html
<!DOCTYPE html>
<html>
<head>
    <title>ITSM Login</title>
</head>
<body>
    <form id="loginForm">
        <input type="email" id="email" placeholder="Email" required>
        <input type="password" id="password" placeholder="Password" required>
        <button type="submit">Login</button>
    </form>

    <script>
        document.getElementById('loginForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;
            
            try {
                const response = await fetch('http://localhost:8080/api/auth/login', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ email, password })
                });

                if (!response.ok) {
                    alert('Invalid credentials');
                    return;
                }

                const data = await response.json();
                
                localStorage.setItem('jwt_token', data.token);
                localStorage.setItem('user_email', data.email);
                localStorage.setItem('user_name', data.name);
                localStorage.setItem('user_role', data.role);
                localStorage.setItem('user_id', data.id);

                if (data.role === 'ADMIN') {
                    window.location.href = '/admin-dashboard.html';
                } else if (data.role === 'ENGINEER') {
                    window.location.href = '/engineer-dashboard.html';
                } else {
                    window.location.href = '/faculty-dashboard.html';
                }

            } catch (error) {
                console.error('Login error:', error);
                alert('Login failed');
            }
        });
    </script>
</body>
</html>
```

## Test Credentials

Use these credentials to test the system:

### Admin
- Email: `admin@college.edu`
- Password: `admin123`

### Engineers
- Email: `priya@college.edu` / Password: `eng123`
- Email: `kumar@college.edu` / Password: `eng123`
- Email: `arun@college.edu` / Password: `eng123`

### Faculty
- Email: `rajesh@college.edu` / Password: `faculty123`
- Email: `anitha@college.edu` / Password: `faculty123`
- Email: `suresh@college.edu` / Password: `faculty123`

## Important Notes

1. **Token Storage**: Store JWT in `localStorage` (or `sessionStorage` for better security)
2. **Token Expiry**: Handle 401 responses by redirecting to login
3. **CORS**: Already configured in SecurityConfig for localhost:8080
4. **Role Names**: Use exact role names from database (ADMIN, ENGINEER, FACULTY)
5. **URL Encoding**: Use `encodeURIComponent()` for query parameters with special characters
6. **Error Messages**: Backend sends detailed error messages - display them to users
7. **Activity Logs**: Every action automatically creates an activity log entry in database

## Common Issues & Solutions

### Issue: 401 Unauthorized
**Solution**: Check if JWT token is valid and not expired

### Issue: 403 Forbidden
**Solution**: User doesn't have required role for the endpoint

### Issue: CORS Error
**Solution**: Make sure SecurityConfig CORS settings match your frontend URL

### Issue: Token not sent
**Solution**: Ensure Authorization header is included: `Bearer ${token}`
