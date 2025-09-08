package org.identityshelf.adminapi.web.dto;

public class IdentityIdentifierSearchRequest {
    private String identifierTypeId;
    private String identifierValue;
    private Boolean verified;
    private Boolean primary;
    
    // Getters and setters
    public String getIdentifierTypeId() { return identifierTypeId; }
    public void setIdentifierTypeId(String identifierTypeId) { this.identifierTypeId = identifierTypeId; }
    public String getIdentifierValue() { return identifierValue; }
    public void setIdentifierValue(String identifierValue) { this.identifierValue = identifierValue; }
    public Boolean getVerified() { return verified; }
    public void setVerified(Boolean verified) { this.verified = verified; }
    public Boolean getPrimary() { return primary; }
    public void setPrimary(Boolean primary) { this.primary = primary; }
}
