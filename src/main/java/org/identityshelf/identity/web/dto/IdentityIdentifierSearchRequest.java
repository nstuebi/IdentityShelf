package org.identityshelf.identity.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdentityIdentifierSearchRequest {
    private String identifierTypeId;
    private String identifierValue;
    private Boolean verified;
    private Boolean primary;
}
