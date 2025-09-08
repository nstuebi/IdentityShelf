package org.identityshelf.core.domain.exception;

import java.util.List;

/**
 * Exception thrown when identity validation fails
 */
public class IdentityValidationException extends DomainException {
    
    private final List<String> validationErrors;
    
    public IdentityValidationException(String message, List<String> validationErrors) {
        super(message);
        this.validationErrors = validationErrors;
    }
    
    public IdentityValidationException(List<String> validationErrors) {
        super("Identity validation failed: " + String.join(", ", validationErrors));
        this.validationErrors = validationErrors;
    }
    
    public List<String> getValidationErrors() {
        return validationErrors;
    }
}
