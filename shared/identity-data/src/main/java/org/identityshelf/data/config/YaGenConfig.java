package org.identityshelf.data.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for yaGen DDL generation and audit support.
 * 
 * This configuration is only active in development profile
 * and when yagen.enabled property is true.
 * 
 * YaGen is a persistence layer concern and belongs in the identity-data module.
 */
@Configuration
@ConditionalOnProperty(name = "yagen.enabled", havingValue = "true", matchIfMissing = false)
public class YaGenConfig {
    
    static {
        // Configure yaGen global settings
        System.setProperty("yagen.generator.output.dir", "target/generated-ddl");
        System.setProperty("yagen.generator.output.create.file", "create.sql");
        System.setProperty("yagen.generator.output.drop.file", "drop.sql");
        
        // Enable history table generation
        System.setProperty("yagen.generator.history.enabled", "true");
        System.setProperty("yagen.generator.history.suffix", "_HISTORY");
    }
}
