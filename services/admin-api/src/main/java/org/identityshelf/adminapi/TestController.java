package org.identityshelf.adminapi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    
    @GetMapping("/health")
    public String health() {
        return "Admin API is running!";
    }
}
