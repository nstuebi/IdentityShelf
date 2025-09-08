package org.identityshelf.adminapi.web.dto;

import jakarta.validation.constraints.Size;
import java.util.Map;

public class UpdateIdentityRequest {
    @Size(max = 255)
    private String displayName;

    private String status;
    
    private Map<String, Object> attributes;

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

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}


