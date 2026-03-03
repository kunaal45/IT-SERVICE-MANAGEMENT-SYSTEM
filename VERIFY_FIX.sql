-- ================================================================
-- VERIFICATION SCRIPT - Run this AFTER fixing to confirm success
-- ================================================================

USE itsm_db;

-- Check users table exists and has correct structure
SELECT 'Checking users table structure...' AS Step1;
DESCRIBE users;

SELECT '' AS BlankLine;

-- Verify role column is VARCHAR(50)
SELECT 
    COLUMN_NAME,
    COLUMN_TYPE,
    IS_NULLABLE,
    CHARACTER_MAXIMUM_LENGTH
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'users' AND COLUMN_NAME = 'role';

SELECT '' AS BlankLine;

-- Check if any data exists
SELECT 
    CASE 
        WHEN COUNT(*) = 0 THEN 'No users yet (will be created by application)'
        WHEN COUNT(*) >= 7 THEN 'All demo users created successfully!'
        ELSE CONCAT('Found ', COUNT(*), ' users')
    END AS UserStatus
FROM users;

SELECT '' AS BlankLine;

-- Show all users if they exist
SELECT 'Existing users:' AS Info;
SELECT id, email, name, role, created_at FROM users ORDER BY id;

SELECT '' AS BlankLine;
SELECT '✓ Verification complete!' AS Result;
