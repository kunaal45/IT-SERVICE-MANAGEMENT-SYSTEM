# Compilation Error Fix Guide

## Problem
The Maven test compilation phase is failing with errors saying it cannot find compiled entity classes:
- User
- Ticket  
- Comment
- AuditLog
- SLARule

However, the main source code compiles successfully, and the compiled .class files DO exist in `target/classes/`.

## Root Cause
This is a Maven classpath caching issue where:
1. Main compilation (compile phase) succeeds and generates .class files
2. Test compilation (test-compile phase) fails because it cannot find the compiled classes
3. This happens due to stale IntelliJ IDE caches or Maven module configuration issues

## Solution (3 Steps)

### Step 1: Close IntelliJ IDEA
Close IntelliJ completely before proceeding. This ensures the IDE cache is released.

### Step 2: Run the Clean Rebuild Script
Run the batch file `REBUILD_CLEAN.bat` from the project root:
```batch
REBUILD_CLEAN.bat
```

This script:
1. Runs `mvn clean` - removes target directory
2. Runs `mvn compile` - recompiles main source code
3. Runs `mvn test-compile` - recompiles test code

### Step 3: Reopen IntelliJ and Verify
1. Reopen IntelliJ IDEA
2. Let it re-index the project (wait for indexing to complete)
3. Try building again: `Build > Build Project` from the menu
4. Or run: `mvn spring-boot:run` to start the application

## Optional: Invalidate IDE Cache
If the issue persists after the clean rebuild, run:
```batch
INVALIDATE_IDEA_CACHE.bat
```

Then repeat Steps 1-3 above.

## Why This Works
- `mvn clean` removes all old artifacts and metadata
- Rebuilding from scratch ensures all dependencies are correctly resolved
- IntelliJ's re-indexing (when you reopen it) picks up all the newly compiled classes
- The compilation phases now execute in the correct order with proper classpath visibility

## Verification
After running the rebuild script, you should see:
```
[OK] Target directory cleaned.
[OK] Main source code compiled successfully.
[OK] Test source code compiled successfully.
```

If any of these fail, check:
1. Maven is installed: `mvn --version`
2. Java is installed: `java --version`
3. JAVA_HOME environment variable is set correctly
4. No files are locked by other processes

## Additional Commands
If you need to run tests after fixing:
```batch
mvn clean test
```

To build the entire project:
```batch
mvn clean install
```

To start the Spring Boot application:
```batch
mvn spring-boot:run
```
