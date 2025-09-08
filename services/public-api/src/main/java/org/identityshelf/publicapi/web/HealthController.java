package org.identityshelf.publicapi.web;

import org.identityshelf.publicapi.web.dto.HealthResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/v1")
public class HealthController {
    
    @Value("${spring.application.version:1.0.0}")
    private String version;
    
    private final long startTime = System.currentTimeMillis();
    
    @GetMapping("/health")
    public ResponseEntity<HealthResponse> health() {
        long uptime = (System.currentTimeMillis() - startTime) / 1000;
        
        HealthResponse response = new HealthResponse(
            "UP",
            OffsetDateTime.now(),
            version,
            uptime
        );
        
        return ResponseEntity.ok(response);
    }
}
