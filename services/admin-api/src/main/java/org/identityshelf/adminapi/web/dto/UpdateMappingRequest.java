package org.identityshelf.adminapi.web.dto;

import jakarta.validation.constraints.Min;

public class UpdateMappingRequest {
    @Min(value = 0, message = "Sort order must be non-negative")
    private int sortOrder = 0;
    
    private boolean required = false;
    
    private String overrideValidationRegex;
    
    private String overrideDefaultValue;
    
    // Getters and setters
    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
    public boolean isRequired() { return required; }
    public void setRequired(boolean required) { this.required = required; }
    public String getOverrideValidationRegex() { return overrideValidationRegex; }
    public void setOverrideValidationRegex(String overrideValidationRegex) { this.overrideValidationRegex = overrideValidationRegex; }
    public String getOverrideDefaultValue() { return overrideDefaultValue; }
    public void setOverrideDefaultValue(String overrideDefaultValue) { this.overrideDefaultValue = overrideDefaultValue; }
}
