package org.identityshelf.adminapi.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;

public class CreateMappingRequest {
    @NotBlank(message = "Identity type ID is required")
    private String identityTypeId;
    
    @NotBlank(message = "Attribute type ID is required")
    private String attributeTypeId;
    
    @Min(value = 0, message = "Sort order must be non-negative")
    private int sortOrder = 0;
    
    private boolean required = false;
    
    private String overrideValidationRegex;
    
    private String overrideDefaultValue;
    
    // Getters and setters
    public String getIdentityTypeId() { return identityTypeId; }
    public void setIdentityTypeId(String identityTypeId) { this.identityTypeId = identityTypeId; }
    public String getAttributeTypeId() { return attributeTypeId; }
    public void setAttributeTypeId(String attributeTypeId) { this.attributeTypeId = attributeTypeId; }
    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
    public boolean isRequired() { return required; }
    public void setRequired(boolean required) { this.required = required; }
    public String getOverrideValidationRegex() { return overrideValidationRegex; }
    public void setOverrideValidationRegex(String overrideValidationRegex) { this.overrideValidationRegex = overrideValidationRegex; }
    public String getOverrideDefaultValue() { return overrideDefaultValue; }
    public void setOverrideDefaultValue(String overrideDefaultValue) { this.overrideDefaultValue = overrideDefaultValue; }
}
