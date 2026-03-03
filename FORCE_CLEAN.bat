@echo off
echo.
echo ============================================
echo   FORCE CLEAN - Delete Target Directory
echo ============================================
echo.

echo Deleting target directory...
if exist target (
    rmdir /s /q target
    echo Target directory deleted successfully!
) else (
    echo Target directory doesn't exist (already clean)
)

echo.
echo Target directory is now clean.
echo.
pause
