@echo off
setlocal

REM Script to manually trigger DDL generation and migration creation
REM This is useful for development when you want to generate migrations manually

echo === yaGen DDL Generation and Migration Creation ===
echo.

REM Check if we're in the right directory
if not exist "build.gradle" (
    echo ERROR: Please run this script from the project root directory
    exit /b 1
)

REM Create output directories
echo Creating output directories...
if not exist "target\generated-ddl" mkdir "target\generated-ddl"
if not exist "src\main\resources\db\migration" mkdir "src\main\resources\db\migration"

REM Run the DDL generation test
echo Running DDL generation...
call gradlew.bat generateDDLAndMigrations --console=plain

REM Check results
if %ERRORLEVEL% EQU 0 (
    echo.
    echo ✅ DDL generation completed successfully!
    echo.
    
    REM Show generated files
    if exist "target\generated-ddl\create-auto.sql" (
        echo 📄 Generated DDL file:
        echo    target\generated-ddl\create-auto.sql
        for /f %%i in ('type "target\generated-ddl\create-auto.sql" ^| find /c /v ""') do echo    Size: %%i lines
    )
    
    echo.
    echo 📂 Check the following directories:
    echo    - target\generated-ddl\     ^(Generated DDL files^)
    echo    - src\main\resources\db\migration\  ^(Flyway migrations^)
    
) else (
    echo.
    echo ❌ DDL generation failed!
    echo Check the output above for error details.
    exit /b 1
)

echo.
echo === DDL Generation Complete ===

