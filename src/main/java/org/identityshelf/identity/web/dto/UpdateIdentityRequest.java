package org.identityshelf.identity.web.dto;

import jakarta.validation.constraints.Size;

public class UpdateIdentityRequest {
    @Size(max = 255)
    private String displayName;

    private String status;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}


