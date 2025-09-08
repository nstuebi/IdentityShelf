#!/bin/bash

# Script to manually trigger DDL generation and migration creation
# This is useful for development when you want to generate migrations manually

echo "=== yaGen DDL Generation and Migration Creation ==="
echo ""

# Check if we're in the right directory
if [ ! -f "build.gradle" ]; then
    echo "ERROR: Please run this script from the project root directory"
    exit 1
fi

# Create output directories
echo "Creating output directories..."
mkdir -p target/generated-ddl
mkdir -p src/main/resources/db/migration

# Run the DDL generation test
echo "Running DDL generation..."
./gradlew generateDDLAndMigrations --console=plain

# Check results
if [ $? -eq 0 ]; then
    echo ""
    echo "✅ DDL generation completed successfully!"
    echo ""
    
    # Show generated files
    if [ -f "target/generated-ddl/create-auto.sql" ]; then
        echo "📄 Generated DDL file:"
        echo "   target/generated-ddl/create-auto.sql"
        echo "   Size: $(wc -l < target/generated-ddl/create-auto.sql) lines"
    fi
    
    # Check for new migrations
    NEW_MIGRATIONS=$(find src/main/resources/db/migration -name "V*__auto_generated_*.sql" -newer build.gradle 2>/dev/null | wc -l)
    if [ "$NEW_MIGRATIONS" -gt 0 ]; then
        echo ""
        echo "🆕 New migrations created:"
        find src/main/resources/db/migration -name "V*__auto_generated_*.sql" -newer build.gradle 2>/dev/null
    else
        echo ""
        echo "ℹ️  No new migrations needed (schema unchanged)"
    fi
    
    echo ""
    echo "📂 Check the following directories:"
    echo "   - target/generated-ddl/     (Generated DDL files)"
    echo "   - src/main/resources/db/migration/  (Flyway migrations)"
    
else
    echo ""
    echo "❌ DDL generation failed!"
    echo "Check the output above for error details."
    exit 1
fi

echo ""
echo "=== DDL Generation Complete ==="

