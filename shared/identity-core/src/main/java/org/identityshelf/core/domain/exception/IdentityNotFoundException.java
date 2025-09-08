package org.identityshelf.core.domain.exception;

import org.identityshelf.core.domain.valueobject.IdentityId;

/**
 * Exception thrown when an identity is not found
 */
public class IdentityNotFoundException extends DomainException {
    
    private final IdentityId identityId;
    
    public IdentityNotFoundException(IdentityId identityId) {
        super("Identity not found: " + identityId);
        this.identityId = identityId;
    }
    
    public IdentityId getIdentityId() {
        return identityId;
    }
}
