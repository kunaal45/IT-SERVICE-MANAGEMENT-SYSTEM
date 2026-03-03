-- ============================================================
-- ITSM System - Database Fix Script
-- ============================================================
-- This script fixes the "Data truncated for column 'role'" error
-- Run this in MySQL Workbench before starting the application
-- ============================================================

-- Switch to the correct database
USE itsm_db;

-- Fix the role column size (expand from current size to VARCHAR(20))
ALTER TABLE users MODIFY COLUMN role VARCHAR(20) NOT NULL;

-- Verify the fix
DESCRIBE users;

-- Check existing data (optional)
SELECT id, name, email, role, created_at FROM users ORDER BY id;

-- Success message
SELECT '✓ Database schema fixed successfully!' AS Status, 
       'role column is now VARCHAR(20)' AS Details,
       'You can now start the application' AS NextStep;

-- ============================================================
-- NEXT STEPS:
-- 1. Close this query tab
-- 2. Open PowerShell/CMD in project folder
-- 3. Run: .\mvnw.cmd clean
-- 4. Run: .\mvnw.cmd spring-boot:run
-- ============================================================
