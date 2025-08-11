package org.identityshelf.identity.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Map;

public class CreateIdentityRequest {
    
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 100, message = "Username must be between 3 and 100 characters")
    private final String username;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 320, message = "Email must not exceed 320 characters")
    private final String email;
    
    @Size(max = 100, message = "First name must not exceed 100 characters")
    private final String firstName;
    
    @Size(max = 100, message = "Last name must not exceed 100 characters")
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


