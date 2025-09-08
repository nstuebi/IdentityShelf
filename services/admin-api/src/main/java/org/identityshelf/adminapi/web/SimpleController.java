package org.identityshelf.adminapi.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SimpleController {
    
    @GetMapping("/health")
    public String health() {
        return "Admin API is running!";
    }
    
    @GetMapping("/status")
    public String status() {
        return "All systems operational";
    }
}
