package org.identityshelf.identity.web.dto;

import lombok.Data;
import jakarta.validation.constraints.Min;

@Data
public class UpdateMappingRequest {
    @Min(value = 0, message = "Sort order must be non-negative")
    private int sortOrder = 0;
    
    private boolean required = false;
    
    private String overrideValidationRegex;
    
    private String overrideDefaultValue;
}
