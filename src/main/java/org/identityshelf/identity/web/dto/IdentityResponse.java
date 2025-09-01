package org.identityshelf.identity.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
public class IdentityResponse {
    private final UUID id;
    private final String username;
    private final String email;
    private final String displayName;
    private final String status;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime updatedAt;
    private final String identityType;
    private final Map<String, Object> attributes;
}


