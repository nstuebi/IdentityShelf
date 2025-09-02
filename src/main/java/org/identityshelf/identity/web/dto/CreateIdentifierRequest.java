package org.identityshelf.identity.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateIdentifierRequest {
    private String identityId;
    private String identifierTypeId;
    private String identifierValue;
    private boolean primary = false;
}
