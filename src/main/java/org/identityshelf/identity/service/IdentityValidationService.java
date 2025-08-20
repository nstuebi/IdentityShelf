package org.identityshelf.identity.service;

import org.identityshelf.identity.domain.AttributeType;
import org.identityshelf.identity.web.dto.CreateIdentityRequest;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class IdentityValidationService {
    
    private static final Logger logger = LoggerFactory.getLogger(IdentityValidationService.class);
    
    public ValidationResult validateIdentity(CreateIdentityRequest request, List<AttributeType> attributeTypes) {
        List<ValidationError> errors = new ArrayList<>();
        
        // Validate all required attributes first
        for (AttributeType attributeType : attributeTypes) {
            if (attributeType.isRequired()) {
                Object value = request.getAttributes() != null ? 
                    request.getAttributes().get(attributeType.getName()) : null;
                if (value == null || value.toString().trim().isEmpty()) {
                    errors.add(new ValidationError(attributeType.getName(), 
                        "Field '" + attributeType.getDisplayName() + "' is required"));
                }
            }
        }
        
        // Validate all provided attributes
        if (request.getAttributes() != null) {
            for (Map.Entry<String, Object> entry : request.getAttributes().entrySet()) {
                String attributeName = entry.getKey();
                Object value = entry.getValue();
                validateField(attributeName, value != null ? value.toString() : null, attributeTypes, errors);
            }
        }
        
        return new ValidationResult(errors.isEmpty(), errors);
    }
    
    private void validateField(String fieldName, String value, List<AttributeType> attributeTypes, List<ValidationError> errors) {
        AttributeType attributeType = attributeTypes.stream()
            .filter(at -> at.getName().equals(fieldName))
            .findFirst()
            .orElse(null);
            
        if (attributeType == null) {
            // Field not defined in attribute types, skip validation
            return;
        }
        
        // Required field validation
        if (attributeType.isRequired() && (value == null || value.trim().isEmpty())) {
            errors.add(new ValidationError(fieldName, "Field '" + attributeType.getDisplayName() + "' is required"));
            return;
        }
        
        // Skip further validation if value is null/empty and not required
        if (value == null || value.trim().isEmpty()) {
            return;
        }
        
        // Regex validation
        if (attributeType.getValidationRegex() != null && !attributeType.getValidationRegex().trim().isEmpty()) {
            try {
                Pattern pattern = Pattern.compile(attributeType.getValidationRegex());
                if (!pattern.matcher(value).matches()) {
                    errors.add(new ValidationError(fieldName, 
                        attributeType.getDisplayName() + " format is invalid"));
                }
            } catch (Exception e) {
                logger.warn("Invalid regex pattern for attribute {}: {}", fieldName, attributeType.getValidationRegex());
            }
        }
    }
    
    public static class ValidationResult {
        private final boolean valid;
        private final List<ValidationError> errors;
        
        public ValidationResult(boolean valid, List<ValidationError> errors) {
            this.valid = valid;
            this.errors = errors;
        }
        
        public boolean isValid() { return valid; }
        public List<ValidationError> getErrors() { return errors; }
    }
    
    public static class ValidationError {
        private final String field;
        private final String message;
        
        public ValidationError(String field, String message) {
            this.field = field;
            this.message = message;
        }
        
        public String getField() { return field; }
        public String getMessage() { return message; }
    }
}
