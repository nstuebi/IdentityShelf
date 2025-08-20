package org.identityshelf.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/build-info")
public class BuildInfoController {
    
    @Value("${spring.application.name:IdentityShelf}")
    private String applicationName;
    
    @GetMapping
    public BuildInfo getBuildInfo() {
        return new BuildInfo(
            applicationName,
            LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            System.getProperty("java.version", "Unknown"),
            System.getProperty("os.name", "Unknown")
        );
    }
    
    public static class BuildInfo {
        private final String applicationName;
        private final String buildTime;
        private final String javaVersion;
        private final String osName;
        
        public BuildInfo(String applicationName, String buildTime, String javaVersion, String osName) {
            this.applicationName = applicationName;
            this.buildTime = buildTime;
            this.javaVersion = javaVersion;
            this.osName = osName;
        }
        
        public String getApplicationName() {
            return applicationName;
        }
        
        public String getBuildTime() {
            return buildTime;
        }
        
        public String getJavaVersion() {
            return javaVersion;
        }
        
        public String getOsName() {
            return osName;
        }
    }
}

