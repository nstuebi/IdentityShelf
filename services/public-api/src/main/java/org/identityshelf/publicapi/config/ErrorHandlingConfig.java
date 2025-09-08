package org.identityshelf.publicapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.error-handling")
public class ErrorHandlingConfig {
    
    private ErrorDetailLevel detailLevel = ErrorDetailLevel.BASIC;
    
    public ErrorDetailLevel getDetailLevel() {
        return detailLevel;
    }
    
    public void setDetailLevel(ErrorDetailLevel detailLevel) {
        this.detailLevel = detailLevel;
    }
    
    public enum ErrorDetailLevel {
        BASIC,      // Only basic error message
        ENHANCED,   // Error message + some context
        FULL        // Error message + stack trace + full context
    }
    
    public boolean isFullDetailEnabled() {
        return detailLevel == ErrorDetailLevel.FULL;
    }
    
    public boolean isEnhancedDetailEnabled() {
        return detailLevel == ErrorDetailLevel.ENHANCED || detailLevel == ErrorDetailLevel.FULL;
    }
}

