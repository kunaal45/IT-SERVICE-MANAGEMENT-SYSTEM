-- ITSM System Database Schema
-- Database: itsm_db
-- Created for ITSM Faculty-Based Ticketing System

-- =====================================================
-- 1. USERS TABLE
-- =====================================================
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255),
    role VARCHAR(50) NOT NULL, -- ADMIN, SUPPORT_ENGINEER, FACULTY
    assigned_area VARCHAR(100), -- For FACULTY: CSE Lab 1, Library, etc.
    specialization VARCHAR(50), -- For SUPPORT_ENGINEER: HARDWARE, SOFTWARE, NETWORK, etc.
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_role (role),
    INDEX idx_specialization (specialization),
    INDEX idx_assigned_area (assigned_area)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 2. CATEGORIES TABLE (NEW)
-- =====================================================
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE, -- HARDWARE, SOFTWARE, NETWORK, etc.
    description TEXT,
    default_sla_hours_high INT NOT NULL DEFAULT 2,
    default_sla_hours_medium INT NOT NULL DEFAULT 8,
    default_sla_hours_low INT NOT NULL DEFAULT 24,
    assigned_engineer_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (assigned_engineer_id) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 3. SLA_RULES TABLE
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
-- 4. TICKETS TABLE (UPDATED)
-- =====================================================
CREATE TABLE IF NOT EXISTS tickets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description LONGTEXT,
    category VARCHAR(50) NOT NULL, -- HARDWARE, SOFTWARE, NETWORK, etc.
    priority VARCHAR(20) NOT NULL, -- HIGH, MEDIUM, LOW
    status VARCHAR(50) NOT NULL, -- OPEN, PENDING_APPROVAL, ASSIGNED, IN_PROGRESS, RESOLVED, CLOSED
    location VARCHAR(100) NOT NULL, -- Specific area: CSE Lab 1, Library, etc.
    resource_request BOOLEAN DEFAULT FALSE,
    quantity INT, -- For resource requests
    sla_deadline DATETIME,
    sla_breached BOOLEAN DEFAULT FALSE,
    created_by_id BIGINT NOT NULL,
    assigned_to_id BIGINT,
    resolution_notes LONGTEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    resolved_at DATETIME,
    FOREIGN KEY (created_by_id) REFERENCES users(id) ON DELETE RESTRICT,
    FOREIGN KEY (assigned_to_id) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_status (status),
    INDEX idx_category (category),
    INDEX idx_priority (priority),
    INDEX idx_location (location),
    INDEX idx_resource_request (resource_request),
    INDEX idx_created_by (created_by_id),
    INDEX idx_assigned_to (assigned_to_id),
    INDEX idx_sla_breached (sla_breached)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 5. COMMENTS TABLE
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
-- 6. AUDIT_LOGS TABLE
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
-- SAMPLE DATA INSERTION
-- =====================================================

-- Insert Sample Users (Faculty-Based System)
INSERT INTO users (name, email, password, role, assigned_area, specialization) VALUES
('Admin User', 'admin@college.edu', 'admin123', 'ADMIN', NULL, NULL),
('Mr. Kumar', 'kumar@college.edu', 'eng123', 'SUPPORT_ENGINEER', NULL, 'HARDWARE'),
('Ms. Priya', 'priya@college.edu', 'eng123', 'SUPPORT_ENGINEER', NULL, 'NETWORK'),
('Mr. Arun', 'arun@college.edu', 'eng123', 'SUPPORT_ENGINEER', NULL, 'SOFTWARE'),
('Dr. Rajesh Kumar', 'rajesh@college.edu', 'faculty123', 'FACULTY', 'CSE Lab 1', NULL),
('Prof. Anitha M', 'anitha@college.edu', 'faculty123', 'FACULTY', 'Library', NULL),
('Dr. Suresh T', 'suresh@college.edu', 'faculty123', 'FACULTY', 'EC Department', NULL);

-- Insert Categories
INSERT INTO categories (name, description, default_sla_hours_high, default_sla_hours_medium, default_sla_hours_low, assigned_engineer_id) VALUES
('HARDWARE', 'Computer, printer, projector, AC, fans', 2, 8, 24, 2),
('SOFTWARE', 'Application install, license issues, updates', 4, 12, 48, 4),
('NETWORK', 'WiFi, internet, LAN connectivity', 1, 6, 24, 3),
('PORTAL_WEBSITE', 'Login issues, server down, bugs', 1, 4, 12, 4),
('INFRASTRUCTURE', 'Chairs, tables, drinking water, electrical', 24, 48, 72, 2),
('ATTENDANCE_SYSTEM', 'Biometric, RFID, software issues', 2, 8, 24, 4),
('RESOURCE_REQUEST', 'Additional resources needed', 168, 168, 168, NULL);

-- Insert SLA Rules
INSERT INTO sla_rules (priority, max_hours) VALUES
('HIGH', 24),
('MEDIUM', 48),
('LOW', 72);

-- Insert Sample Tickets (Faculty-Based)
INSERT INTO tickets (title, description, category, priority, status, location, resource_request, quantity, created_by_id, assigned_to_id, sla_deadline, sla_breached)
VALUES
(
    'Lab PC-05 monitor not working',
    'Monitor shows no display, power LED blinking. Students cannot use this workstation.',
    'HARDWARE',
    'HIGH',
    'ASSIGNED',
    'CSE Lab 1',
    FALSE,
    NULL,
    5,
    2,
    DATE_ADD(NOW(), INTERVAL 2 HOUR),
    FALSE
),
(
    'WiFi intermittent in library',
    'Students unable to connect to WiFi. Connection drops frequently.',
    'NETWORK',
    'MEDIUM',
    'IN_PROGRESS',
    'Library',
    FALSE,
    NULL,
    6,
    3,
    DATE_ADD(NOW(), INTERVAL 6 HOUR),
    FALSE
),
(
    'Need additional chairs for reading room',
    'Current capacity insufficient during exam season. Need more seating.',
    'RESOURCE_REQUEST',
    'MEDIUM',
    'PENDING_APPROVAL',
    'Library Reading Room',
    TRUE,
    15,
    6,
    NULL,
    DATE_ADD(NOW(), INTERVAL 7 DAY),
    FALSE
),
(
    'Portal login error 500',
    'Faculty portal showing server error when trying to upload marks.',
    'PORTAL_WEBSITE',
    'HIGH',
    'OPEN',
    'Admin Server',
    FALSE,
    NULL,
    7,
    NULL,
    DATE_ADD(NOW(), INTERVAL 1 HOUR),
    FALSE
),
(
    'Projector not displaying',
    'HDMI connection issue. Tried multiple cables.',
    'HARDWARE',
    'HIGH',
    'RESOLVED',
    'EC Classroom 301',
    FALSE,
    NULL,
    7,
    2,
    DATE_ADD(NOW(), INTERVAL 2 HOUR),
    FALSE
);

-- Insert Sample Comments
INSERT INTO comments (ticket_id, user_id, content) VALUES
(1, 2, 'Checked the monitor. Display cable is faulty. Replacing with new cable.'),
(1, 5, 'Thanks! Students have a lab session at 2 PM.'),
(2, 3, 'Router in library needs firmware update. Scheduled for tonight.'),
(5, 2, 'Replaced HDMI cable and verified display working correctly.');

-- Insert Sample Audit Logs
INSERT INTO audit_logs (action, user_id, ticket_id, details) VALUES
('TICKET_CREATED', 5, 1, 'New hardware issue reported by Dr. Rajesh Kumar for CSE Lab 1'),
('TICKET_AUTO_ASSIGNED', NULL, 1, 'Ticket auto-assigned to Hardware Engineer based on HARDWARE category'),
('TICKET_CREATED', 6, 2, 'New network issue reported by Prof. Anitha M for Library'),
('TICKET_CREATED', 6, 3, 'Resource request for 15 chairs submitted by Prof. Anitha M'),
('TICKET_RESOLVED', 2, 5, 'Hardware issue resolved by Mr. Kumar');

-- =====================================================
-- VERIFICATION QUERIES
-- =====================================================
-- Check users count
SELECT COUNT(*) as user_count FROM users;

-- Check tickets count
SELECT COUNT(*) as ticket_count FROM tickets;

-- Check comments count
SELECT COUNT(*) as comment_count FROM comments;

-- Check audit logs count
SELECT COUNT(*) as audit_count FROM audit_logs;

-- Check categories
SELECT * FROM categories;

-- Check SLA rules
SELECT * FROM sla_rules;
