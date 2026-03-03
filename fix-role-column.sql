-- ===========================================================
-- ITSM System - Database Fix for Role Column
-- ===========================================================
-- This script fixes the "Data truncated for column 'role'" error
-- by ensuring the role column is large enough to hold all enum values

USE itsm_db;

-- Fix the role column size
ALTER TABLE users MODIFY COLUMN role VARCHAR(20) NOT NULL;

-- Verify the change
DESCRIBE users;

-- Optional: Check existing data
SELECT id, name, email, role FROM users;

-- Status
SELECT 'Database schema fixed successfully!' AS status;
