package org.identityshelf.identity.web.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;

@Data
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
}
