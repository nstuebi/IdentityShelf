package org.identityshelf.data.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Configuration for automated DDL generation and migration creation.
 * 
 * This configuration is activated during build to generate DDL scripts
 * and create Flyway migrations when schema changes are detected.
 * 
 * DDL generation is a persistence layer concern and belongs in the identity-data module.
 */
@Configuration
@Profile("ddl-generation")
@ConditionalOnProperty(name = "yagen.auto-migration.enabled", havingValue = "true", matchIfMissing = true)
public class DDLGenerationConfig {

    @PostConstruct
    public void ensureDirectories() {
        try {
            // Ensure target directories exist
            Path ddlDir = Paths.get("target/generated-ddl");
            Files.createDirectories(ddlDir);
            
            Path migrationDir = Paths.get("src/main/resources/db/migration");
            Files.createDirectories(migrationDir);
            
            System.out.println("DDL Generation Config: Directories prepared");
            System.out.println("DDL Output: " + ddlDir.toAbsolutePath());
            System.out.println("Migration Dir: " + migrationDir.toAbsolutePath());
            
        } catch (IOException e) {
            System.err.println("Failed to create DDL directories: " + e.getMessage());
        }
    }
}
