// ========================================
// ITSM SYSTEM - FRONTEND ONLY
// Spring Boot Backend Integration
// ========================================

// API Configuration
const API_URL = '/api';
const TOKEN_KEY = 'itsm_token';
const USER_KEY = 'itsm_user';

// ========================================
// UTILITY FUNCTIONS
// ========================================

/**
 * Get authorization headers with JWT token
 */
function getAuthHeaders() {
    const token = localStorage.getItem(TOKEN_KEY);
    return {
        'Content-Type': 'application/json',
        'Authorization': token ? `Bearer ${token}` : ''
    };
}

/**
 * Get current logged-in user from localStorage
 */
function getCurrentUser() {
    const userJson = localStorage.getItem(USER_KEY);
    return userJson ? JSON.parse(userJson) : null;
}

/**
 * Check if user is authenticated
 */
function checkAuth() {
    const token = localStorage.getItem(TOKEN_KEY);
    const currentPage = window.location.pathname;

    console.log('Checking auth - Current page:', currentPage);
    console.log('Token exists:', !!token);

    // Allow access to login page
    if (currentPage.includes('index.html') || currentPage === '/') {
        console.log('Login page - access allowed');
        return true;
    }

    // Redirect to login if no token
    if (!token) {
        console.log('No token found - redirecting to login');
        window.location.href = '/index.html';
        return false;
    }

    console.log('Token found - access allowed');
    return true;
}

/**
 * Redirect to appropriate dashboard based on role
 */
function redirectToDashboard(role) {
    if (role === 'ADMIN') {
        window.location.href = '/admin-dashboard.html';
    } else if (role === 'ENGINEER' || role === 'SUPPORT_ENGINEER') {
        window.location.href = '/engineer-dashboard.html';
    } else if (role === 'FACULTY') {
        window.location.href = '/faculty-dashboard.html';
    } else {
        window.location.href = '/faculty-dashboard.html';
    }
}

/**
 * Logout user
 */
function logout() {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
    window.location.href = '/index.html';
}

/**
 * Go back to dashboard
 */
function goToDashboard() {
    const user = getCurrentUser();
    if (user) {
        redirectToDashboard(user.role);
    } else {
        window.location.href = '/index.html';
    }
}

// ========================================
// AUTHENTICATION API CALLS
// ========================================

/**
 * Handle user login
 */
async function handleLogin(email, password) {
    try {
        const response = await fetch(`${API_URL}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });

        if (response.ok) {
            const apiResponse = await response.json();
            console.log('Login response:', apiResponse);
            
            const data = apiResponse.data; // Extract the data object from API response
            
            if (!data || !data.token) {
                console.error('Invalid response data:', data);
                showError('Invalid server response. Please try again.');
                return false;
            }

            // Store token and user info
            localStorage.setItem(TOKEN_KEY, data.token);
            localStorage.setItem(USER_KEY, JSON.stringify({
                id: data.userId,
                email: data.email,
                name: data.name,
                role: data.role
            }));

            console.log('Token stored:', data.token.substring(0, 20) + '...');
            console.log('User stored:', data.email, '- Role:', data.role);

            // Redirect based on role
            redirectToDashboard(data.role);
            return true;
        } else {
            const errorData = await response.json().catch(() => ({}));
            console.error('Login failed:', response.status, errorData);
            showError('Invalid email or password');
            return false;
        }
    } catch (error) {
        console.error('Login error:', error);
        showError('Connection error. Please try again.');
        return false;
    }
}

/**
 * Load user info into page
 */
function loadUserInfo() {
    const user = getCurrentUser();
    if (user) {
        // Update user name displays
        const userNameElements = document.querySelectorAll('#userName, .user-name');
        userNameElements.forEach(el => {
            if (el) el.textContent = user.name;
        });

        // Update user email displays
        const userEmailElements = document.querySelectorAll('#userEmail, .user-email');
        userEmailElements.forEach(el => {
            if (el) el.textContent = user.email;
        });
    }
}

// ========================================
// TICKET API CALLS
// ========================================

/**
 * Fetch all tickets from backend (role-based filtering)
 */
async function loadTickets() {
    try {
        const response = await fetch(`${API_URL}/tickets`, {
            headers: getAuthHeaders()
        });

        if (response.status === 401 || response.status === 403) {
            logout();
            return [];
        }

        if (response.ok) {
            const tickets = await response.json();
            return tickets;
        } else {
            console.error('Failed to load tickets');
            return [];
        }
    } catch (error) {
        console.error('Error loading tickets:', error);
        return [];
    }
}

/**
 * Fetch single ticket by ID
 */
async function loadTicketDetails(ticketId) {
    try {
        const response = await fetch(`${API_URL}/tickets/${ticketId}`, {
            headers: getAuthHeaders()
        });

        if (response.ok) {
            const ticket = await response.json();
            return ticket;
        }
    } catch (error) {
        console.error('Error loading ticket:', error);
        showError('Failed to load ticket details');
    }
}

/**
 * Create new ticket
 */
async function createTicket(formData) {
    try {
        const response = await fetch(`${API_URL}/tickets`, {
            method: 'POST',
            headers: getAuthHeaders(),
            body: JSON.stringify(formData)
        });

        if (response.ok) {
            const ticket = await response.json();
            showSuccess('Ticket created successfully!');
            return ticket;
        } else {
            showError('Failed to create ticket');
            return null;
        }
    } catch (error) {
        console.error('Error creating ticket:', error);
        showError('Connection error');
        return null;
    }
}

/**
 * Assign ticket to engineer (Admin only)
 */
async function assignTicket(ticketId, engineerId) {
    try {
        const response = await fetch(
            `${API_URL}/tickets/${ticketId}/assign/${engineerId}`,
            {
                method: 'POST',
                headers: getAuthHeaders()
            }
        );

        if (response.ok) {
            showSuccess('Ticket assigned successfully!');
            return await response.json();
        } else {
            showError('Failed to assign ticket');
            return null;
        }
    } catch (error) {
        console.error('Error assigning ticket:', error);
        showError('Connection error');
        return null;
    }
}

/**
 * Resolve ticket (Engineer only)
 */
async function resolveTicket(ticketId, notes) {
    try {
        const user = getCurrentUser();
        const url = new URL(`${API_URL}/tickets/${ticketId}/resolve`, window.location.origin);
        url.searchParams.append('userRole', user.role);
        url.searchParams.append('agentName', user.name);
        url.searchParams.append('agentEmail', user.email);

        const response = await fetch(url.toString(), {
            method: 'PUT',
            headers: getAuthHeaders()
        });

        if (response.ok) {
            showSuccess('Ticket resolved successfully!');
            return await response.json();
        } else {
            const error = await response.text();
            showError(error || 'Failed to resolve ticket');
            return null;
        }
    } catch (error) {
        console.error('Error resolving ticket:', error);
        showError('Connection error');
        return null;
    }
}

/**
 * Close ticket (Faculty only)
 */
async function closeTicket(ticketId) {
    if (!confirm('Are you sure you want to close this ticket?')) return null;

    try {
        const user = getCurrentUser();
        const url = new URL(`${API_URL}/tickets/${ticketId}/close`, window.location.origin);
        url.searchParams.append('userRole', user.role);
        url.searchParams.append('agentName', user.name);
        url.searchParams.append('agentEmail', user.email);

        const response = await fetch(url.toString(), {
            method: 'PUT',
            headers: getAuthHeaders()
        });

        if (response.ok) {
            showSuccess('Ticket closed successfully!');
            return await response.json();
        } else {
            const error = await response.text();
            showError(error || 'Failed to close ticket');
            return null;
        }
    } catch (error) {
        console.error('Error closing ticket:', error);
        showError('Connection error');
        return null;
    }
}

/**
 * Update ticket status
 */
async function updateTicketStatus(ticketId, status) {
    try {
        const user = getCurrentUser();
        const url = new URL(`${API_URL}/tickets/${ticketId}/status`, window.location.origin);
        url.searchParams.append('status', status);
        url.searchParams.append('userRole', user.role);

        const response = await fetch(url.toString(), {
            method: 'PUT',
            headers: getAuthHeaders()
        });

        if (response.ok) {
            showSuccess(`Status updated to ${status}`);
            return await response.json();
        } else {
            const error = await response.text();
            showError(error || 'Failed to update status');
            return null;
        }
    } catch (error) {
        console.error('Error updating status:', error);
        showError('Connection error');
        return null;
    }
}

// ========================================
// STATS API CALLS
// ========================================

/**
 * Load dashboard statistics
 */
async function loadStats() {
    try {
        const tickets = await loadTickets();
        
        const stats = {
            total: tickets.length,
            open: tickets.filter(t => t.status === 'OPEN').length,
            assigned: tickets.filter(t => t.status === 'ASSIGNED').length,
            inProgress: tickets.filter(t => t.status === 'IN_PROGRESS').length,
            resolved: tickets.filter(t => t.status === 'RESOLVED').length,
            closed: tickets.filter(t => t.status === 'CLOSED').length
        };

        updateStatsDisplay(stats);
        return stats;
    } catch (error) {
        console.error('Error loading stats:', error);
    }
}

/**
 * Update stats display on page
 */
function updateStatsDisplay(stats) {
    const elements = {
        'totalTickets': stats.total,
        'openTickets': stats.open,
        'assignedTickets': stats.assigned,
        'inProgressTickets': stats.inProgress,
        'resolvedTickets': stats.resolved,
        'closedTickets': stats.closed,
        'breachedTickets': stats.breached || 0,
        'unassignedTickets': stats.open,
        'activeTickets': stats.total - stats.closed,
        'resolvedTodayCount': stats.resolved,
        'assignedCount': stats.assigned + stats.inProgress,
        'inProgressCount': stats.inProgress
    };

    for (const [id, value] of Object.entries(elements)) {
        const element = document.getElementById(id);
        if (element) {
            element.textContent = value;
        }
    }
}

// ========================================
// UI HELPER FUNCTIONS
// ========================================

/**
 * Show success message
 */
function showSuccess(message) {
    // Try to use existing notification system
    if (typeof showNotification === 'function') {
        showNotification(message, 'success');
    } else {
        alert(message);
    }
}

/**
 * Show error message
 */
function showError(message) {
    // Try to use existing notification system
    if (typeof showNotification === 'function') {
        showNotification(message, 'error');
    } else {
        alert(message);
    }
}

/**
 * Format date for display
 */
function formatDate(dateString) {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return date.toLocaleString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
}

/**
 * View ticket details
 */
function viewTicket(ticketId) {
    window.location.href = `ticket-details.html?id=${ticketId}`;
}

/**
 * Show resolve modal
 */
function showResolveModal(ticketId) {
    const notes = prompt('Enter resolution notes:');
    if (notes) {
        resolveTicket(ticketId, notes).then(ticket => {
            if (ticket) {
                // Reload current page data
                if (typeof loadTicketDetails === 'function') {
                    loadTicketDetails(ticketId);
                } else {
                    location.reload();
                }
            }
        });
    }
}

// ========================================
// PAGE INITIALIZATION
// ========================================

// Check authentication on page load
document.addEventListener('DOMContentLoaded', function() {
    checkAuth();
});
