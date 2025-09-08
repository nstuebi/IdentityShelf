package org.identityshelf.publicapi.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Identifier search result")
public class IdentifierSearchResult {
    
    @Schema(description = "Identifier information")
    @JsonProperty("identifier")
    private IdentifierResponse identifier;
    
    @Schema(description = "Identity information")
    @JsonProperty("identity")
    private IdentityResponse identity;
    
    // Constructors
    public IdentifierSearchResult() {}
    
    public IdentifierSearchResult(IdentifierResponse identifier, IdentityResponse identity) {
        this.identifier = identifier;
        this.identity = identity;
    }
    
    // Getters and setters
    public IdentifierResponse getIdentifier() { return identifier; }
    public void setIdentifier(IdentifierResponse identifier) { this.identifier = identifier; }
    
    public IdentityResponse getIdentity() { return identity; }
    public void setIdentity(IdentityResponse identity) { this.identity = identity; }
}
