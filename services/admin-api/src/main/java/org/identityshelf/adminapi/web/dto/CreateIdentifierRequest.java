package org.identityshelf.adminapi.web.dto;

public class CreateIdentifierRequest {
    private String identityId;
    private String identifierTypeId;
    private String identifierValue;
    private boolean primary = false;
    
    // Getters and setters
    public String getIdentityId() { return identityId; }
    public void setIdentityId(String identityId) { this.identityId = identityId; }
    public String getIdentifierTypeId() { return identifierTypeId; }
    public void setIdentifierTypeId(String identifierTypeId) { this.identifierTypeId = identifierTypeId; }
    public String getIdentifierValue() { return identifierValue; }
    public void setIdentifierValue(String identifierValue) { this.identifierValue = identifierValue; }
    public boolean isPrimary() { return primary; }
    public void setPrimary(boolean primary) { this.primary = primary; }
}
