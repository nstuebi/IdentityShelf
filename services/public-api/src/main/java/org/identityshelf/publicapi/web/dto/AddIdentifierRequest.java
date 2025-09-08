package org.identityshelf.publicapi.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Add identifier request")
public class AddIdentifierRequest {
    
    @Schema(description = "Identifier type", example = "email", required = true)
    @NotBlank(message = "Identifier type is required")
    @JsonProperty("type")
    private String type;
    
    @Schema(description = "Identifier value", example = "john.doe@example.com", required = true)
    @NotBlank(message = "Identifier value is required")
    @JsonProperty("value")
    private String value;
    
    @Schema(description = "Whether this is the primary identifier", example = "false")
    @JsonProperty("primary")
    private Boolean primary = false;
    
    // Constructors
    public AddIdentifierRequest() {}
    
    public AddIdentifierRequest(String type, String value, Boolean primary) {
        this.type = type;
        this.value = value;
        this.primary = primary;
    }
    
    // Getters and setters
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    
    public Boolean getPrimary() { return primary; }
    public void setPrimary(Boolean primary) { this.primary = primary; }
}
