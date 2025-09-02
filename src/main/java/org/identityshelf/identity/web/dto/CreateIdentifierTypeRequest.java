package org.identityshelf.identity.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateIdentifierTypeRequest {
    private String name;
    private String displayName;
    private String description;
    private String dataType;
    private String validationRegex;
    private String defaultValue;
    private boolean unique = true;
    private boolean searchable = true;
}
