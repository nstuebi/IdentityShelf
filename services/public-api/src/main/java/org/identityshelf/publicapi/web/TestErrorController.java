package org.identityshelf.publicapi.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestErrorController {
    
    @GetMapping("/error/basic")
    public String triggerBasicError() {
        throw new RuntimeException("This is a basic error message");
    }
    
    @GetMapping("/error/enhanced")
    public String triggerEnhancedError() {
        throw new IllegalArgumentException("Invalid parameter provided");
    }
    
    @GetMapping("/error/full")
    public String triggerFullError() {
        throw new RuntimeException("This error should show full details including stack trace");
    }
    
    @GetMapping("/error/nested")
    public String triggerNestedError() {
        try {
            throw new RuntimeException("Root cause error");
        } catch (Exception e) {
            throw new RuntimeException("Wrapper error", e);
        }
    }
}

