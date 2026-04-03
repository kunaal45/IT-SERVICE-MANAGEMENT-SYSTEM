-- Sample data for ITSM System
-- Admin User (password: admin123 - BCrypt encoded)
INSERT INTO users (email, name, password, role, created_at) VALUES
('admin@college.edu', 'Admin User', '$2a$10$slYQmyNdGzin7olVN3p5HO.P.5nQJVvlLqJMuVvx9yHC3n6IZ5Jvm', 'ADMIN', NOW());

-- Engineer Users
INSERT INTO users (email, name, password, role, created_at) VALUES
('engineer1@college.edu', 'Engineer One', '$2a$10$slYQmyNdGzin7olVN3p5HO.P.5nQJVvlLqJMuVvx9yHC3n6IZ5Jvm', 'ENGINEER', NOW()),
('engineer2@college.edu', 'Engineer Two', '$2a$10$slYQmyNdGzin7olVN3p5HO.P.5nQJVvlLqJMuVvx9yHC3n6IZ5Jvm', 'ENGINEER', NOW());

-- Sample Tickets
INSERT INTO tickets (title, description, status, priority, created_by_id, assigned_to_id, created_at, updated_at) VALUES
('Server Down', 'Production server is down', 'OPEN', 'CRITICAL', 1, NULL, NOW(), NOW()),
('Email Issue', 'User cannot access email', 'ASSIGNED', 'HIGH', 1, 2, NOW(), NOW()),
('Password Reset', 'Need to reset user password', 'RESOLVED', 'MEDIUM', 1, 3, NOW(), NOW());

-- Sample Assets
INSERT INTO assets (asset_code, name, category, status, location, created_at) VALUES
('LAP-001', 'Dell Latitude 5000', 'LAPTOP', 'AVAILABLE', 'Office A', NOW()),
('DES-001', 'HP Desktop Pro', 'DESKTOP', 'ASSIGNED', 'Office B', NOW()),
('PRN-001', 'Canon Printer', 'PRINTER', 'AVAILABLE', 'Office C', NOW());
