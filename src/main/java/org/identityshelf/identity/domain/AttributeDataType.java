package org.identityshelf.identity.domain;

public enum AttributeDataType {
    STRING("String"),
    INTEGER("Integer"),
    DECIMAL("Decimal"),
    BOOLEAN("Boolean"),
    DATE("Date"),
    DATETIME("DateTime"),
    EMAIL("Email"),
    PHONE("Phone"),
    URL("URL"),
    SELECT("Select"),
    MULTI_SELECT("Multi-Select");
    
    private final String displayName;
    
    AttributeDataType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}
