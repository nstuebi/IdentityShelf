package org.identityshelf.identity.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.identityshelf.identity.domain.IdentityTypeAttributeMapping;
import org.identityshelf.identity.web.dto.CreateIdentityRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class IdentityValidationService {
    
    public ValidationResult validateIdentity(CreateIdentityRequest request, List<IdentityTypeAttributeMapping> mappings) {
        List<ValidationError> errors = new ArrayList<>();
        
        // Validate all required attributes first
        for (IdentityTypeAttributeMapping mapping : mappings) {
            if (mapping.isRequired()) {
                Object value = request.getAttributes() != null ? 
                    request.getAttributes().get(mapping.getAttributeType().getName()) : null;
                if (value == null || value.toString().trim().isEmpty()) {
                    errors.add(new ValidationError(mapping.getAttributeType().getName(), 
                        "Field '" + mapping.getAttributeType().getDisplayName() + "' is required"));
                }
            }
        }
        
        // Validate all provided attributes
        if (request.getAttributes() != null) {
            for (Map.Entry<String, Object> entry : request.getAttributes().entrySet()) {
                String attributeName = entry.getKey();
                Object value = entry.getValue();
                validateField(attributeName, value != null ? value.toString() : null, mappings, errors);
            }
        }
        
        return new ValidationResult(errors.isEmpty(), errors);
    }
    
    private void validateField(String fieldName, String value, List<IdentityTypeAttributeMapping> mappings, List<ValidationError> errors) {
        IdentityTypeAttributeMapping mapping = mappings.stream()
            .filter(m -> m.getAttributeType().getName().equals(fieldName))
            .findFirst()
            .orElse(null);
            
        if (mapping == null) {
            // Field not defined in mappings, skip validation
            return;
        }
        
        // Required field validation
        if (mapping.isRequired() && (value == null || value.trim().isEmpty())) {
            errors.add(new ValidationError(fieldName, "Field '" + mapping.getAttributeType().getDisplayName() + "' is required"));
            return;
        }
        
        // Skip further validation if value is null/empty and not required
        if (value == null || value.trim().isEmpty()) {
            return;
        }
        
        // Cumulative regex validation - both base and override must match
        validateCumulativeRegex(fieldName, value, mapping, errors);
    }
    
    /**
     * Validates value against cumulative regex rules (both base attribute and mapping override)
     */
    private void validateCumulativeRegex(String fieldName, String value, IdentityTypeAttributeMapping mapping, List<ValidationError> errors) {
        // Validate against base attribute regex if present
        String baseRegex = mapping.getAttributeType().getValidationRegex();
        if (baseRegex != null && !baseRegex.trim().isEmpty()) {
            try {
                Pattern pattern = Pattern.compile(baseRegex);
                if (!pattern.matcher(value).matches()) {
                    errors.add(new ValidationError(fieldName, 
                        mapping.getAttributeType().getDisplayName() + " format is invalid (base rule)"));
                    return; // Don't continue if base validation fails
                }
            } catch (Exception e) {
                log.warn("Invalid base regex pattern for attribute {}: {}", fieldName, baseRegex);
            }
        }
        
        // Additionally validate against override regex if present
        String overrideRegex = mapping.getOverrideValidationRegex();
        if (overrideRegex != null && !overrideRegex.trim().isEmpty()) {
            try {
                Pattern pattern = Pattern.compile(overrideRegex);
                if (!pattern.matcher(value).matches()) {
                    errors.add(new ValidationError(fieldName, 
                        mapping.getAttributeType().getDisplayName() + " format is invalid (additional rule)"));
                }
            } catch (Exception e) {
                log.warn("Invalid override regex pattern for attribute {}: {}", fieldName, overrideRegex);
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
