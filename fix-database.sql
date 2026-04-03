USE itsm_db;

-- Fix User role column
ALTER TABLE users MODIFY COLUMN role VARCHAR(50) NOT NULL;

-- Fix AuditLog user_role column
ALTER TABLE audit_logs MODIFY COLUMN user_role VARCHAR(50);

-- Ensure indexes exist
ALTER TABLE users ADD INDEX IF NOT EXISTS idx_user_role (role);
