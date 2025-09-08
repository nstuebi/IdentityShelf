package org.identityshelf.publicapi.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Identifier search criteria")
public class IdentifierSearchCriteria {
    
    @Schema(description = "Identifier type", example = "email")
    @JsonProperty("type")
    private String type;
    
    @Schema(description = "Identifier value", example = "john.doe@example.com")
    @JsonProperty("value")
    private String value;
    
    @Schema(description = "Filter by verification status", example = "true")
    @JsonProperty("verified")
    private Boolean verified;
    
    @Schema(description = "Filter by primary status", example = "true")
    @JsonProperty("primary")
    private Boolean primary;
    
    // Constructors
    public IdentifierSearchCriteria() {}
    
    public IdentifierSearchCriteria(String type, String value, Boolean verified, Boolean primary) {
        this.type = type;
        this.value = value;
        this.verified = verified;
        this.primary = primary;
    }
    
    // Getters and setters
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    
    public Boolean getVerified() { return verified; }
    public void setVerified(Boolean verified) { this.verified = verified; }
    
    public Boolean getPrimary() { return primary; }
    public void setPrimary(Boolean primary) { this.primary = primary; }
}
