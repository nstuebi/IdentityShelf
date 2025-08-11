package org.identityshelf.identity.web.dto;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public class IdentityResponse {
    private final UUID id;
    private final String username;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime updatedAt;
    private final String identityType;
    private final Map<String, Object> attributes;

    public IdentityResponse(
        UUID id,
        String username,
        String email,
        String firstName,
        String lastName,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        String identityType,
        Map<String, Object> attributes
    ) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.identityType = identityType;
        this.attributes = attributes;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public String getIdentityType() {
        return identityType;
    }
    
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}


