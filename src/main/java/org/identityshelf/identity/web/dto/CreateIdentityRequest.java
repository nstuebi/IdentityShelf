package org.identityshelf.identity.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Map;

public class CreateIdentityRequest {
    
    @NotBlank(message = "Username is required")
    private final String username;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private final String email;
    
    private final String firstName;
    
    private final String lastName;
    
    @NotBlank(message = "Identity type is required")
    private final String identityType;
    
    private final Map<String, Object> attributes;
    
    public CreateIdentityRequest(
        String username,
        String email,
        String firstName,
        String lastName,
        String identityType,
        Map<String, Object> attributes
    ) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.identityType = identityType;
        this.attributes = attributes;
    }
    
    // Getters
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getIdentityType() { return identityType; }
    public Map<String, Object> getAttributes() { return attributes; }
}


