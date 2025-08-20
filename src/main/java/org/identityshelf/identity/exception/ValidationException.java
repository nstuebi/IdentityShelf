package org.identityshelf.identity.exception;

import org.identityshelf.identity.service.IdentityValidationService.ValidationError;
import java.util.List;

public class ValidationException extends RuntimeException {
    private final List<ValidationError> errors;
    
    public ValidationException(String message, List<ValidationError> errors) {
        super(message);
        this.errors = errors;
    }
    
    public List<ValidationError> getErrors() {
        return errors;
    }
}
