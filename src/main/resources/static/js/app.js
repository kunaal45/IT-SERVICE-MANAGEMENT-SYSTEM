// ========================================
// ITSM SYSTEM - Shared App Logic
// Spring Boot Backend Integration
// ========================================

const API_URL = '/api';
const TOKEN_KEY = 'itsm_token';
const USER_KEY = 'itsm_user';
const ENGINEER_LEVEL_KEY = 'itsm_engineer_level';

// ========================================
// AUTH UTILITIES
// ========================================

function getAuthHeaders() {
    const token = localStorage.getItem(TOKEN_KEY);
    return {
        'Content-Type': 'application/json',
        'Authorization': token ? `Bearer ${token}` : ''
    };
}

/**
 * Shared API fetch utility that handles auth headers and JSON response.
 * @param {string} endpoint - API endpoint starting with /
 * @param {object} options - fetch options
 */
async function apiFetch(endpoint, options = {}) {
    const url = endpoint.startsWith('http') ? endpoint : endpoint;
    const defaultOptions = {
        headers: getAuthHeaders()
    };
    const mergedOptions = { ...defaultOptions, ...options };
    if (options.body && typeof options.body === 'object') {
        mergedOptions.body = JSON.stringify(options.body);
    }

    try {
        const response = await fetch(mergedOptions.url || url, mergedOptions);
        if (response.status === 401 || response.status === 403) {
            logout();
            return { success: false, message: 'Session expired' };
        }
        const data = await response.json().catch(() => ({}));
        return {
            success: response.ok,
            status: response.status,
            data: data.data || data,
            message: data.message || (response.ok ? 'Success' : 'API Error')
        };
    } catch (error) {
        console.error('API Error:', error);
        return { success: false, message: 'Connection error' };
    }
}

/**
 * Legacy wrapper for fetch with auth
 */
async function fetchWithAuth(url, options = {}) {
    return apiFetch(url, options);
}

function getCurrentUser() {
    const userJson = localStorage.getItem(USER_KEY);
    return userJson ? JSON.parse(userJson) : null;
}

function checkAuth() {
    const token = localStorage.getItem(TOKEN_KEY);
    const currentPage = window.location.pathname;
    if (currentPage.includes('index.html') || currentPage === '/') {
        return true;
    }
    if (!token) {
        window.location.href = '/index.html';
        return false;
    }
    return true;
}

function redirectToDashboard(role, engineerLevel) {
    if (role === 'ADMIN') {
        window.location.href = '/admin-dashboard.html';
    } else if (role === 'SERVICE_DESK') {
        window.location.href = '/service-desk-dashboard.html';
    } else if (role === 'ENGINEER' || role === 'SUPPORT_ENGINEER') {
        const level = engineerLevel || localStorage.getItem(ENGINEER_LEVEL_KEY);
        if (level === 'JUNIOR') {
            window.location.href = '/junior-engineer-dashboard.html';
        } else {
            window.location.href = '/engineer-dashboard.html';
        }
    } else {
        window.location.href = '/faculty-dashboard.html';
    }
}

/**
 * Show a styled logout confirmation modal.
 * The actual logout only happens when the user confirms.
 */
function logout() {
    // If a confirmation modal is already showing, don't create another one
    if (document.getElementById('logoutConfirmModal')) return;

    const overlay = document.createElement('div');
    overlay.id = 'logoutConfirmModal';
    overlay.style.cssText = `
        position:fixed;inset:0;z-index:99999;
        background:rgba(0,0,0,0.6);
        display:flex;align-items:center;justify-content:center;
        animation:fadeIn 0.2s ease;
    `;
    overlay.innerHTML = `
        <div style="
            background:#161c26;border:1px solid #253348;
            border-radius:16px;padding:32px;max-width:380px;width:90%;
            box-shadow:0 30px 60px rgba(0,0,0,0.5);
            animation:slideUp 0.25s ease;
            text-align:center;
        ">
            <div style="width:56px;height:56px;background:rgba(255,77,106,0.15);border-radius:50%;display:flex;align-items:center;justify-content:center;margin:0 auto 16px;">
                <span class='material-icons' style='color:#ff4d6a;font-size:28px;'>logout</span>
            </div>
            <h3 style="color:#e2e8f0;font-size:18px;font-weight:700;margin:0 0 8px;">Confirm Logout</h3>
            <p style="color:#7a90a8;font-size:14px;margin:0 0 24px;">Are you sure you want to sign out of the ITSM portal?</p>
            <div style="display:flex;gap:12px;">
                <button id='logoutCancelBtn' style="
                    flex:1;padding:10px 16px;border-radius:8px;
                    background:#1c2535;border:1px solid #253348;
                    color:#7a90a8;font-size:14px;font-weight:600;cursor:pointer;
                    font-family:Inter,sans-serif;
                " onmouseover="this.style.background='#212d40'" onmouseout="this.style.background='#1c2535'">
                    Cancel
                </button>
                <button id='logoutConfirmBtn' style="
                    flex:1;padding:10px 16px;border-radius:8px;
                    background:#ff4d6a;border:none;
                    color:#fff;font-size:14px;font-weight:700;cursor:pointer;
                    font-family:Inter,sans-serif;
                " onmouseover="this.style.background='#e0314a'" onmouseout="this.style.background='#ff4d6a'">
                    Sign Out
                </button>
            </div>
        </div>
        <style>
            @keyframes fadeIn { from{opacity:0} to{opacity:1} }
            @keyframes slideUp { from{transform:translateY(20px);opacity:0} to{transform:translateY(0);opacity:1} }
        </style>
    `;

    document.body.appendChild(overlay);

    document.getElementById('logoutCancelBtn').addEventListener('click', () => overlay.remove());
    overlay.addEventListener('click', (e) => { if (e.target === overlay) overlay.remove(); });
    document.getElementById('logoutConfirmBtn').addEventListener('click', () => {
        localStorage.removeItem(TOKEN_KEY);
        localStorage.removeItem(USER_KEY);
        localStorage.removeItem(ENGINEER_LEVEL_KEY);
        window.location.href = '/index.html';
    });
}

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

async function handleLogin(email, password) {
    try {
        const response = await fetch(`${API_URL}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });

        if (response.ok) {
            const apiResponse = await response.json();
            const data = apiResponse.data;
            if (!data || !data.token) {
                showError('Invalid server response. Please try again.');
                return false;
            }
            localStorage.setItem(TOKEN_KEY, data.token);
            localStorage.setItem(USER_KEY, JSON.stringify({
                id: data.userId,
                email: data.email,
                name: data.name,
                role: data.role,
                engineerLevel: data.engineerLevel || null
            }));
            if (data.engineerLevel) {
                localStorage.setItem(ENGINEER_LEVEL_KEY, data.engineerLevel);
            }
            redirectToDashboard(data.role, data.engineerLevel);
            return true;
        } else {
            showError('Invalid email or password');
            return false;
        }
    } catch (error) {
        console.error('Login error:', error);
        showError('Connection error. Please try again.');
        return false;
    }
}

function loadUserInfo() {
    const user = getCurrentUser();
    if (user) {
        document.querySelectorAll('#userName, .user-name').forEach(el => {
            if (el) el.textContent = user.name;
        });
        document.querySelectorAll('#userEmail, .user-email').forEach(el => {
            if (el) el.textContent = user.email;
        });
        // Set avatar initials
        document.querySelectorAll('.user-avatar-initial').forEach(el => {
            if (el) el.textContent = user.name ? user.name.charAt(0).toUpperCase() : '?';
        });
    }
}

// ========================================
// TICKET API CALLS
// ========================================

/**
 * Fetch tickets for the current user role:
 * ADMIN  → all tickets (paged, first page)
 * ENGINEER → assigned to me
 * FACULTY / others → created by me
 */
async function loadTickets() {
    try {
        const user = getCurrentUser();
        let url;
        if (user && user.role === 'ADMIN') {
            // Admin sees all tickets
            url = `${API_URL}/tickets/all?size=200`;
        } else {
            // Engineer / Faculty sees their own
            url = `${API_URL}/tickets/my`;
        }

        const response = await fetch(url, { headers: getAuthHeaders() });

        if (response.status === 401 || response.status === 403) {
            logout();
            return [];
        }
        if (response.ok) {
            const result = await response.json();
            // /all returns a paged object; /my returns a list
            const data = result.data;
            if (Array.isArray(data)) return data;
            if (data && Array.isArray(data.content)) return data.content;
            return [];
        }
        console.error('Failed to load tickets:', response.status);
        return [];
    } catch (error) {
        console.error('Error loading tickets:', error);
        return [];
    }
}

/**
 * Fetch ALL tickets (admin only), returns a flat array.
 */
async function fetchAllTickets() {
    try {
        const response = await fetch(`${API_URL}/tickets/all?size=200`, {
            headers: getAuthHeaders()
        });
        if (response.status === 401 || response.status === 403) {
            logout();
            return [];
        }
        if (response.ok) {
            const result = await response.json();
            const data = result.data;
            if (Array.isArray(data)) return data;
            if (data && Array.isArray(data.content)) return data.content;
            return [];
        }
        return [];
    } catch (error) {
        console.error('Error fetching all tickets:', error);
        return [];
    }
}

/**
 * Fetch single ticket by ID.
 */
async function loadTicketDetails(ticketId) {
    try {
        const response = await fetch(`${API_URL}/tickets/${ticketId}`, {
            headers: getAuthHeaders()
        });
        if (response.ok) {
            const result = await response.json();
            return result.data || result;
        }
    } catch (error) {
        console.error('Error loading ticket:', error);
        showError('Failed to load ticket details');
    }
    return null;
}

/**
 * Create new ticket (Faculty / Admin).
 * @param {object} formData - { title, description, category, priority, location }
 */
async function createTicket(formData) {
    try {
        const response = await fetch(`${API_URL}/tickets`, {
            method: 'POST',
            headers: getAuthHeaders(),
            body: JSON.stringify(formData)
        });
        if (response.ok) {
            const result = await response.json();
            showSuccess('Ticket created successfully!');
            return result.data || result;
        } else {
            const err = await response.json().catch(() => ({}));
            showError(err.message || 'Failed to create ticket');
            return null;
        }
    } catch (error) {
        console.error('Error creating ticket:', error);
        showError('Connection error');
        return null;
    }
}

/**
 * Assign ticket to engineer (Admin only).
 * Sends PUT /api/tickets/{id}/assign with body { engineerId }
 */
async function assignTicket(ticketId, engineerId) {
    try {
        const response = await fetch(`${API_URL}/tickets/${ticketId}/assign`, {
            method: 'PUT',
            headers: getAuthHeaders(),
            body: JSON.stringify({ engineerId: parseInt(engineerId) })
        });
        if (response.ok) {
            const result = await response.json();
            showSuccess('Ticket assigned successfully!');
            return result.data || result;
        } else {
            const err = await response.json().catch(() => ({}));
            showError(err.message || 'Failed to assign ticket');
            return null;
        }
    } catch (error) {
        console.error('Error assigning ticket:', error);
        showError('Connection error');
        return null;
    }
}

/**
 * Start progress on a ticket (Engineer only).
 */
async function startProgress(ticketId) {
    try {
        const response = await fetch(`${API_URL}/tickets/${ticketId}/start`, {
            method: 'PUT',
            headers: getAuthHeaders()
        });
        if (response.ok) {
            const result = await response.json();
            showSuccess('Ticket moved to In Progress!');
            return result.data || result;
        } else {
            const err = await response.json().catch(() => ({}));
            showError(err.message || 'Failed to start progress');
            return null;
        }
    } catch (error) {
        console.error('Error starting progress:', error);
        showError('Connection error');
        return null;
    }
}

/**
 * Resolve ticket (Engineer / Admin).
 * @param {string|number} ticketId
 * @param {string} [notes] - optional resolution notes
 */
async function resolveTicket(ticketId, notes) {
    try {
        const body = notes ? JSON.stringify({ resolutionNotes: notes }) : null;
        const response = await fetch(`${API_URL}/tickets/${ticketId}/resolve`, {
            method: 'PUT',
            headers: getAuthHeaders(),
            body: body
        });
        if (response.ok) {
            const result = await response.json();
            showSuccess('Ticket resolved successfully!');
            return result.data || result;
        } else {
            const err = await response.json().catch(() => ({}));
            showError(err.message || 'Failed to resolve ticket');
            return null;
        }
    } catch (error) {
        console.error('Error resolving ticket:', error);
        showError('Connection error');
        return null;
    }
}

/**
 * Close ticket (Faculty / Admin).
 */
async function closeTicket(ticketId) {
    try {
        const response = await fetch(`${API_URL}/tickets/${ticketId}/close`, {
            method: 'PUT',
            headers: getAuthHeaders()
        });
        if (response.ok) {
            const result = await response.json();
            showSuccess('Ticket closed successfully!');
            return result.data || result;
        } else {
            const err = await response.json().catch(() => ({}));
            showError(err.message || 'Failed to close ticket');
            return null;
        }
    } catch (error) {
        console.error('Error closing ticket:', error);
        showError('Connection error');
        return null;
    }
}

/**
 * Fetch audit log entries for a specific ticket.
 */
async function fetchTicketAuditLogs(ticketId) {
    try {
        const response = await fetch(`${API_URL}/tickets/${ticketId}/audit`, {
            headers: getAuthHeaders()
        });
        if (response.ok) {
            const result = await response.json();
            return result.data || [];
        }
    } catch (error) {
        console.error('Error fetching audit logs:', error);
    }
    return [];
}

/**
 * Fetch comments for a specific ticket.
 */
async function fetchTicketComments(ticketId) {
    try {
        const response = await fetch(`${API_URL}/tickets/${ticketId}/comments`, {
            headers: getAuthHeaders()
        });
        if (response.ok) {
            const result = await response.json();
            return result.data || [];
        }
    } catch (error) {
        console.error('Error fetching comments:', error);
    }
    return [];
}

/**
 * Post comment (Faculty only).
 */
async function postTicketComment(ticketId, content) {
    try {
        const response = await fetch(`${API_URL}/tickets/${ticketId}/comments`, {
            method: 'POST',
            headers: getAuthHeaders(),
            body: JSON.stringify({ content })
        });
        if (response.ok) {
            const result = await response.json();
            return result.data || result;
        } else {
            const err = await response.json().catch(() => ({}));
            showError(err.message || 'Failed to post comment');
            return null;
        }
    } catch (error) {
        console.error('Error posting comment:', error);
        showError('Connection error');
        return null;
    }
}

/**
 * Fetch all audit logs (Admin only).
 */
async function fetchAllAuditLogs() {
    try {
        const response = await fetch(`${API_URL}/audit-logs`, {
            headers: getAuthHeaders()
        });
        if (response.ok) {
            const result = await response.json();
            return result.data || [];
        }
    } catch (error) {
        console.error('Error fetching all audit logs:', error);
    }
    return [];
}

/**
 * Fetch recent audit logs (Admin dashboard activity feed).
 */
async function fetchRecentAuditLogs() {
    try {
        const response = await fetch(`${API_URL}/audit-logs/recent`, {
            headers: getAuthHeaders()
        });
        if (response.ok) {
            const result = await response.json();
            return result.data || [];
        }
    } catch (error) {
        console.error('Error fetching recent audit logs:', error);
    }
    return [];
}

/**
 * Update SLA rule (Admin only).
 */
async function updateSLARule(priority, maxHours) {
    try {
        const response = await apiFetch(`${API_URL}/sla-rules/${priority.toUpperCase()}`, {
            method: 'PUT',
            body: { maxHours: parseInt(maxHours) }
        });
        if (response.success) {
            return response.data;
        } else {
            showError(response.message || 'Failed to update SLA rule');
            return null;
        }
    } catch (error) {
        console.error('Error updating SLA rule:', error);
        return null;
    }
}

/**
 * Fetch list of engineers (Admin only).
 */
async function fetchEngineers() {
    try {
        const response = await fetch(`${API_URL}/tickets/engineers`, {
            headers: getAuthHeaders()
        });
        if (response.ok) {
            const result = await response.json();
            return result.data || [];
        }
    } catch (error) {
        console.error('Error fetching engineers:', error);
    }
    return [];
}

/**
 * Load dashboard statistics from the backend.
 */
async function loadStats() {
    try {
        const response = await fetch(`${API_URL}/tickets/dashboard/stats`, {
            headers: getAuthHeaders()
        });
        if (response.ok) {
            const result = await response.json();
            const stats = result.data || {};
            updateStatsDisplay(stats);
            return stats;
        }
    } catch (error) {
        console.error('Error loading stats:', error);
    }
}

/**
 * Update stats display elements by ID.
 */
function updateStatsDisplay(stats) {
    const elements = {
        'totalTickets': stats.totalTickets,
        'openTickets': stats.openTickets,
        'assignedTickets': stats.assignedTickets,
        'inProgressTickets': stats.inProgressTickets,
        'resolvedTickets': stats.resolvedTickets,
        'closedTickets': stats.closedTickets,
        'breachedTickets': stats.breachedTickets || 0,
        'unassignedTickets': stats.openTickets || 0,
        'totalTicketsCount': stats.totalTickets,
        'openTicketsCount': stats.openTickets,
        'resolvedTicketsCount': (stats.resolvedTickets || 0) + (stats.closedTickets || 0),
        'assignedCount': (stats.assignedTickets || 0) + (stats.inProgressTickets || 0),
        'inProgressCount': stats.inProgressTickets,
        'resolvedTodayCount': stats.resolvedTickets
    };
    for (const [id, value] of Object.entries(elements)) {
        const el = document.getElementById(id);
        if (el && value !== undefined && value !== null) {
            el.textContent = value;
        }
    }
}

// ========================================
// UI HELPERS
// ========================================

function showSuccess(message) {
    showNotificationBanner(message, 'success');
}

function showError(message) {
    showNotificationBanner(message, 'error');
}

function showNotificationBanner(message, type) {
    // Remove existing banners
    const existing = document.getElementById('itsm-notification');
    if (existing) existing.remove();

    const banner = document.createElement('div');
    banner.id = 'itsm-notification';
    banner.style.cssText = `
        position: fixed; top: 20px; right: 20px; z-index: 9999;
        padding: 14px 20px; border-radius: 10px; 
        font-family: Inter, sans-serif; font-size: 14px; font-weight: 500;
        box-shadow: 0 8px 24px rgba(0,0,0,0.15);
        display: flex; align-items: center; gap: 10px;
        animation: slideInRight 0.3s ease; max-width: 360px;
    `;
    if (type === 'success') {
        banner.style.background = '#ecfdf5';
        banner.style.color = '#065f46';
        banner.style.border = '1px solid #6ee7b7';
        banner.innerHTML = '<span style="font-size:18px">✅</span>' + message;
    } else {
        banner.style.background = '#fef2f2';
        banner.style.color = '#991b1b';
        banner.style.border = '1px solid #fca5a5';
        banner.innerHTML = '<span style="font-size:18px">❌</span>' + message;
    }
    document.body.appendChild(banner);
    setTimeout(() => { if (banner.parentNode) banner.remove(); }, 4000);
}

function formatDate(dateString) {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return date.toLocaleString('en-US', {
        year: 'numeric', month: 'short', day: 'numeric',
        hour: '2-digit', minute: '2-digit'
    });
}

function formatDateShort(dateString) {
    if (!dateString) return '';
    const date = new Date(dateString);
    const month = date.toLocaleString('en-US', { month: 'short' });
    const day = date.getDate();
    const time = date.toLocaleString('en-US', { hour: '2-digit', minute: '2-digit', hour12: true });
    return `${month} ${day}, ${time}`;
}

function formatRelativeTime(dateString) {
    if (!dateString) return '';
    const date = new Date(dateString);
    const now = new Date();
    const diff = now - date;
    const minutes = Math.floor(diff / 60000);
    const hours = Math.floor(diff / 3600000);
    const days = Math.floor(diff / 86400000);
    if (minutes < 1) return 'just now';
    if (minutes < 60) return `${minutes} min${minutes > 1 ? 's' : ''} ago`;
    if (hours < 24) return `${hours} hour${hours > 1 ? 's' : ''} ago`;
    return `${days} day${days > 1 ? 's' : ''} ago`;
}

function getPriorityColor(priority) {
    switch (priority) {
        case 'HIGH': case 'CRITICAL': return 'bg-red-100 text-red-800';
        case 'MEDIUM': return 'bg-yellow-100 text-yellow-800';
        case 'LOW': return 'bg-green-100 text-green-800';
        default: return 'bg-slate-100 text-slate-800';
    }
}

function getStatusColor(status) {
    switch (status) {
        case 'OPEN': return 'status-open';
        case 'ASSIGNED': return 'status-assigned';
        case 'IN_PROGRESS': return 'status-in-progress';
        case 'RESOLVED': return 'status-resolved';
        case 'CLOSED': return 'status-closed';
        case 'SLA_BREACHED': return 'status-breached';
        default: return 'status-closed';
    }
}

function getActionIcon(action) {
    switch (action) {
        case 'TICKET_CREATED': return { icon: 'add_circle', color: 'bg-blue-100 text-blue-600' };
        case 'TICKET_ASSIGNED': return { icon: 'person_add', color: 'bg-orange-100 text-orange-600' };
        case 'TICKET_REASSIGNED': return { icon: 'manage_accounts', color: 'bg-yellow-100 text-yellow-600' };
        case 'TICKET_STARTED': return { icon: 'sync', color: 'bg-purple-100 text-purple-600' };
        case 'TICKET_RESOLVED': return { icon: 'check_circle', color: 'bg-green-100 text-green-600' };
        case 'TICKET_CLOSED': return { icon: 'lock', color: 'bg-slate-100 text-slate-600' };
        case 'SLA_BREACHED': return { icon: 'error', color: 'bg-red-100 text-red-600' };
        default: return { icon: 'info', color: 'bg-slate-100 text-slate-600' };
    }
}

function viewTicket(ticketId) {
    window.location.href = `ticket-details.html?id=${ticketId}`;
}

/**
 * Show resolve modal from anywhere by injecting or triggering a modal.
 * Note: If the page already implements a resolveModal (like engineer-dashboard), 
 * it will use that. Otherwise, injects a shared one.
 */
function showResolveModal(ticketId) {
    if (document.getElementById('resolveModal')) {
        // use local
        if (typeof handleResolve === 'function') {
            handleResolve(ticketId);
        } else if (typeof handleResolveFromHeader === 'function') {
            // we'd need to mock the URL params for header but ticketId is passed
            // For now just route it properly if handleResolve is missing
            const oldUrl = window.history.replaceState(null, null, `?id=${ticketId}`);
            handleResolveFromHeader();
        }
        return;
    }

    // Inject fallback logic if needed
    const notes = prompt('Enter resolution notes (optional):');
    if (notes === null) return;
    resolveTicket(ticketId, notes).then(ticket => {
        if (ticket) {
            location.reload();
        }
    });
}

// ========================================
// PAGE INIT
// ========================================
document.addEventListener('DOMContentLoaded', function () {
    checkAuth();
});
