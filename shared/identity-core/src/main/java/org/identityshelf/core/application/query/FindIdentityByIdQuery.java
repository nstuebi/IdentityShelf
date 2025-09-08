package org.identityshelf.core.application.query;

import org.identityshelf.core.domain.valueobject.IdentityId;

/**
 * Query to find identity by ID
 */
public record FindIdentityByIdQuery(IdentityId identityId) {
    
    public FindIdentityByIdQuery {
        if (identityId == null) {
            throw new IllegalArgumentException("Identity ID cannot be null");
        }
    }
}
