package org.identityshelf.publicapi.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Map;

@Schema(description = "Create identity request")
public class CreateIdentityRequest {
    
    @Schema(description = "Identity type", example = "PERSON", required = true)
    @NotBlank(message = "Identity type is required")
    @JsonProperty("type")
    private String type;
    
    @Schema(description = "Display name", example = "John Doe", required = true, maxLength = 255)
    @NotBlank(message = "Display name is required")
    @Size(max = 255, message = "Display name must not exceed 255 characters")
    @JsonProperty("displayName")
    private String displayName;
    
    @Schema(description = "Identity status", example = "ACTIVE", allowableValues = {"ACTIVE", "INACTIVE", "SUSPENDED", "PENDING"})
    @JsonProperty("status")
    private String status = "ACTIVE";
    
    @Schema(description = "Identity attributes")
    @JsonProperty("attributes")
    private Map<String, Object> attributes;
    
    @Schema(description = "Initial identifiers")
    @JsonProperty("identifiers")
    private List<AddIdentifierRequest> identifiers;
    
    // Constructors
    public CreateIdentityRequest() {}
    
    public CreateIdentityRequest(String type, String displayName, String status, 
                                Map<String, Object> attributes, List<AddIdentifierRequest> identifiers) {
        this.type = type;
        this.displayName = displayName;
        this.status = status;
        this.attributes = attributes;
        this.identifiers = identifiers;
    }
    
    // Getters and setters
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Map<String, Object> getAttributes() { return attributes; }
    public void setAttributes(Map<String, Object> attributes) { this.attributes = attributes; }
    
    public List<AddIdentifierRequest> getIdentifiers() { return identifiers; }
    public void setIdentifiers(List<AddIdentifierRequest> identifiers) { this.identifiers = identifiers; }
}