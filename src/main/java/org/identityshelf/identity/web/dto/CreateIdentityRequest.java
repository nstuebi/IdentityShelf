package org.identityshelf.identity.web.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.Map;

public class CreateIdentityRequest {
    
    @NotBlank(message = "Identity type is required")
    private final String identityType;
    
    private final Map<String, Object> attributes;
    
    public CreateIdentityRequest(
        String identityType,
        Map<String, Object> attributes
    ) {
        this.identityType = identityType;
        this.attributes = attributes;
    }
    
    // Getters
    public String getIdentityType() { return identityType; }
    public Map<String, Object> getAttributes() { return attributes; }
    
    // Helper methods to extract common attributes
    public String getUsername() { 
        return attributes != null ? (String) attributes.get("username") : null; 
    }
    
    public String getEmail() { 
        return attributes != null ? (String) attributes.get("email") : null; 
    }
    
    public String getFirstName() { 
        return attributes != null ? (String) attributes.get("first_name") : null; 
    }
    
    public String getLastName() { 
        return attributes != null ? (String) attributes.get("last_name") : null; 
    }
}


