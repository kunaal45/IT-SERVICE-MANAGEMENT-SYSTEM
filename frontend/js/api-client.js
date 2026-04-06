// ============================================
// ITSM API Client for Frontend
// ============================================
// Simple JavaScript client to call backend APIs
// No workflow logic - just REST calls
// ============================================

class ITSMClient {
    constructor() {
        // Automatically detect if running locally or on Vercel
        const isLocal = window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1';
        
        // IMPORTANT: Replace 'YOUR-KOYEB-URL' below with the actual URL Koyeb gives you!
        this.baseURL = isLocal ? 'http://localhost:8080/api' : 'https://it-service-management-system-3.onrender.com/api';
        
        this.token = localStorage.getItem('jwt_token');
    }

    // ===== AUTHENTICATION =====

    async login(email, password) {
        const response = await fetch(`${this.baseURL}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });

        if (!response.ok) throw new Error('Login failed');

        const data = await response.json();
        this.token = data.data.token;
        localStorage.setItem('jwt_token', this.token);
        localStorage.setItem('user_email', data.data.email);
        localStorage.setItem('user_role', data.data.role);

        return data.data;
    }

    logout() {
        this.token = null;
        localStorage.removeItem('jwt_token');
        localStorage.removeItem('user_email');
        localStorage.removeItem('user_role');
    }

    // ===== TICKET OPERATIONS =====

    async createTicket(title, description, priority, category, location) {
        const response = await fetch(`${this.baseURL}/tickets`, {
            method: 'POST',
            headers: this.getHeaders(),
            body: JSON.stringify({
                title,
                description,
                priority,
                category,
                location
            })
        });

        if (!response.ok) throw new Error('Failed to create ticket');
        return await response.json();
    }

    async getTickets() {
        const response = await fetch(`${this.baseURL}/tickets`, {
            method: 'GET',
            headers: this.getHeaders()
        });

        if (!response.ok) throw new Error('Failed to get tickets');
        return await response.json();
    }

    async getTicket(id) {
        const response = await fetch(`${this.baseURL}/tickets/${id}`, {
            method: 'GET',
            headers: this.getHeaders()
        });

        if (!response.ok) throw new Error('Failed to get ticket');
        return await response.json();
    }

    async getAllTickets() {
        const response = await fetch(`${this.baseURL}/tickets/all`, {
            method: 'GET',
            headers: this.getHeaders()
        });

        if (!response.ok) throw new Error('Admin access required');
        return await response.json();
    }

    async assignTicket(ticketId, engineerId) {
        const response = await fetch(`${this.baseURL}/tickets/${ticketId}/assign`, {
            method: 'PUT',
            headers: this.getHeaders(),
            body: JSON.stringify({ engineerId })
        });

        if (!response.ok) throw new Error('Failed to assign ticket');
        return await response.json();
    }

    async startProgress(ticketId) {
        const response = await fetch(`${this.baseURL}/tickets/${ticketId}/start`, {
            method: 'PUT',
            headers: this.getHeaders()
        });

        if (!response.ok) throw new Error('Failed to start progress');
        return await response.json();
    }

    async resolveTicket(ticketId) {
        const response = await fetch(`${this.baseURL}/tickets/${ticketId}/resolve`, {
            method: 'PUT',
            headers: this.getHeaders()
        });

        if (!response.ok) throw new Error('Failed to resolve ticket');
        return await response.json();
    }

    async closeTicket(ticketId) {
        const response = await fetch(`${this.baseURL}/tickets/${ticketId}/close`, {
            method: 'PUT',
            headers: this.getHeaders()
        });

        if (!response.ok) throw new Error('Failed to close ticket');
        return await response.json();
    }

    async getDashboardStats() {
        const response = await fetch(`${this.baseURL}/tickets/dashboard/stats`, {
            method: 'GET',
            headers: this.getHeaders()
        });

        if (!response.ok) throw new Error('Failed to get dashboard stats');
        return await response.json();
    }

    async getAuditLog(ticketId) {
        const response = await fetch(`${this.baseURL}/tickets/${ticketId}/audit`, {
            method: 'GET',
            headers: this.getHeaders()
        });

        if (!response.ok) throw new Error('Failed to get audit log');
        return await response.json();
    }

    // ===== HELPERS =====

    getHeaders() {
        return {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${this.token}`
        };
    }

    isAuthenticated() {
        return !!this.token;
    }

    getUserRole() {
        return localStorage.getItem('user_role');
    }
}

// Create global instance
const api = new ITSMClient();

// ===== USAGE EXAMPLES =====

/*
// Login
api.login('admin@college.edu', 'admin123')
    .then(user => console.log('Logged in as:', user.name))
    .catch(err => console.error('Login failed:', err));

// Create ticket
api.createTicket(
    'New Issue',
    'Description here',
    'HIGH',
    'HARDWARE',
    'Lab Room'
)
    .then(result => console.log('Ticket created:', result.data))
    .catch(err => console.error('Error:', err));

// Assign ticket
api.assignTicket(1, 2)
    .then(result => console.log('Assigned:', result.data))
    .catch(err => console.error('Error:', err));

// Get all tickets
api.getTickets()
    .then(result => {
        console.log('Tickets:', result.data);
        result.data.forEach(t => {
            console.log(`[${t.id}] ${t.title} - ${t.status}`);
        });
    })
    .catch(err => console.error('Error:', err));

// Dashboard stats
api.getDashboardStats()
    .then(result => console.log('Stats:', result.data))
    .catch(err => console.error('Error:', err));

// Audit log
api.getAuditLog(1)
    .then(result => {
        console.log('Audit logs for ticket 1:');
        result.data.forEach(log => {
            console.log(`${log.createdAt}: ${log.action} - ${log.details}`);
        });
    })
    .catch(err => console.error('Error:', err));
*/
