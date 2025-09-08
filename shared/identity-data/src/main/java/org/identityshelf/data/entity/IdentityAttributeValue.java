package org.identityshelf.data.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "identity_attribute_values")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"uuid", "createdAt", "updatedAt"})
public class IdentityAttributeValue {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", columnDefinition = "uuid")
    private UUID uuid;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "identity_uuid", nullable = false)
    private Identity identity;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attribute_type_uuid", nullable = false)
    private AttributeType attributeType;
    
    @Column(name = "string_value")
    private String stringValue;
    
    @Column(name = "integer_value")
    private Long integerValue;
    
    @Column(name = "decimal_value")
    private Double decimalValue;
    
    @Column(name = "boolean_value")
    private Boolean booleanValue;
    
    @Column(name = "date_value")
    private OffsetDateTime dateValue;
    
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
    
    // JPA lifecycle methods
    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
        updatedAt = OffsetDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }
    
    // Constructors
    public IdentityAttributeValue(Identity identity, AttributeType attributeType) {
        this.identity = identity;
        this.attributeType = attributeType;
    }
    
    // Helper method to get the appropriate value based on data type
    public Object getValue() {
        if (attributeType == null) {
            return null;
        }
        
        switch (attributeType.getDataType()) {
            case STRING:
            case EMAIL:
            case PHONE:
            case URL:
            case SELECT:
            case MULTI_SELECT:
                return stringValue;
            case INTEGER:
                return integerValue;
            case DECIMAL:
                return decimalValue;
            case BOOLEAN:
                return booleanValue;
            case DATE:
            case DATETIME:
                return dateValue;
            default:
                return stringValue;
        }
    }
    
    // Helper method to set the appropriate value based on data type
    public void setValue(Object value) {
        if (attributeType == null) {
            return;
        }
        
        switch (attributeType.getDataType()) {
            case STRING:
            case EMAIL:
            case PHONE:
            case URL:
            case SELECT:
            case MULTI_SELECT:
                this.stringValue = value != null ? value.toString() : null;
                break;
            case INTEGER:
                if (value instanceof Number) {
                    this.integerValue = ((Number) value).longValue();
                } else if (value instanceof String) {
                    try {
                        this.integerValue = Long.parseLong((String) value);
                    } catch (NumberFormatException e) {
                        this.integerValue = null;
                    }
                } else {
                    this.integerValue = null;
                }
                break;
            case DECIMAL:
                if (value instanceof Number) {
                    this.decimalValue = ((Number) value).doubleValue();
                } else if (value instanceof String) {
                    try {
                        this.decimalValue = Double.parseDouble((String) value);
                    } catch (NumberFormatException e) {
                        this.decimalValue = null;
                    }
                } else {
                    this.decimalValue = null;
                }
                break;
            case BOOLEAN:
                if (value instanceof Boolean) {
                    this.booleanValue = (Boolean) value;
                } else if (value instanceof String) {
                    this.booleanValue = Boolean.parseBoolean((String) value);
                } else {
                    this.booleanValue = null;
                }
                break;
            case DATE:
            case DATETIME:
                if (value instanceof OffsetDateTime) {
                    this.dateValue = (OffsetDateTime) value;
                } else if (value instanceof String) {
                    // Simple parsing - in production you'd want more robust date parsing
                    try {
                        this.dateValue = OffsetDateTime.parse((String) value);
                    } catch (Exception e) {
                        this.dateValue = null;
                    }
                } else {
                    this.dateValue = null;
                }
                break;
        }
    }
}
