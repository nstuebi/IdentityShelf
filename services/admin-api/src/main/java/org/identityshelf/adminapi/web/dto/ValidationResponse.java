package org.identityshelf.adminapi.web.dto;

public class ValidationResponse {
    private boolean valid;
    private String regex;
    
    // Constructor
    public ValidationResponse(boolean valid, String regex) {
        this.valid = valid;
        this.regex = regex;
    }
    
    // Getters
    public boolean isValid() { return valid; }
    public String getRegex() { return regex; }
}
