package org.identityshelf.publicapi.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.UUID;

@Schema(description = "Identifier response")
public class IdentifierResponse {
    
    @Schema(description = "Identifier ID", example = "123e4567-e89b-12d3-a456-426614174000")
    @JsonProperty("id")
    private UUID id;
    
    @Schema(description = "Identifier type", example = "email")
    @JsonProperty("type")
    private String type;
    
    @Schema(description = "Identifier value", example = "john.doe@example.com")
    @JsonProperty("value")
    private String value;
    
    @Schema(description = "Whether this is the primary identifier", example = "true")
    @JsonProperty("primary")
    private Boolean primary;
    
    @Schema(description = "Whether this identifier is verified", example = "true")
    @JsonProperty("verified")
    private Boolean verified;
    
    @Schema(description = "Verification timestamp")
    @JsonProperty("verifiedAt")
    private OffsetDateTime verifiedAt;
    
    @Schema(description = "Creation timestamp")
    @JsonProperty("createdAt")
    private OffsetDateTime createdAt;
    
    @Schema(description = "Last update timestamp")
    @JsonProperty("updatedAt")
    private OffsetDateTime updatedAt;
    
    // Constructors
    public IdentifierResponse() {}
    
    public IdentifierResponse(UUID id, String type, String value, Boolean primary, 
                             Boolean verified, OffsetDateTime verifiedAt, 
                             OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.type = type;
        this.value = value;
        this.primary = primary;
        this.verified = verified;
        this.verifiedAt = verifiedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    
    public Boolean getPrimary() { return primary; }
    public void setPrimary(Boolean primary) { this.primary = primary; }
    
    public Boolean getVerified() { return verified; }
    public void setVerified(Boolean verified) { this.verified = verified; }
    
    public OffsetDateTime getVerifiedAt() { return verifiedAt; }
    public void setVerifiedAt(OffsetDateTime verifiedAt) { this.verifiedAt = verifiedAt; }
    
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}
