-- Fix column types in identity_type_attribute_mappings table to match referenced tables

-- First, backup the existing data
CREATE TEMPORARY TABLE mapping_backup AS 
SELECT * FROM identity_type_attribute_mappings;

-- Drop the existing table with wrong column types
DROP TABLE identity_type_attribute_mappings;

-- Recreate with correct column types
CREATE TABLE identity_type_attribute_mappings (
    id VARCHAR(36) PRIMARY KEY,
    identity_type_id VARCHAR(36) NOT NULL,
    attribute_type_id VARCHAR(36) NOT NULL,
    sort_order INTEGER NOT NULL DEFAULT 0,
    is_required BOOLEAN NOT NULL DEFAULT FALSE,
    override_validation_regex TEXT,
    override_default_value TEXT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    
    -- Foreign key constraints
    CONSTRAINT fk_mapping_identity_type FOREIGN KEY (identity_type_id) REFERENCES identity_types(id) ON DELETE CASCADE,
    CONSTRAINT fk_mapping_attribute_type FOREIGN KEY (attribute_type_id) REFERENCES attribute_types(id) ON DELETE CASCADE,
    
    -- Unique constraint to prevent duplicate mappings
    CONSTRAINT uk_identity_type_attribute UNIQUE (identity_type_id, attribute_type_id)
);

-- Create indexes for performance
CREATE INDEX idx_mapping_identity_type ON identity_type_attribute_mappings(identity_type_id);
CREATE INDEX idx_mapping_attribute_type ON identity_type_attribute_mappings(attribute_type_id);
CREATE INDEX idx_mapping_sort_order ON identity_type_attribute_mappings(identity_type_id, sort_order);

-- Restore the data, converting UUID to VARCHAR(36)
INSERT INTO identity_type_attribute_mappings (
    id, identity_type_id, attribute_type_id, sort_order, is_required,
    override_validation_regex, override_default_value, is_active, created_at, updated_at
)
SELECT
    id::text,  -- Convert UUID to VARCHAR(36)
    identity_type_id,
    attribute_type_id,
    sort_order,
    is_required,
    override_validation_regex,
    override_default_value,
    is_active,
    created_at,
    updated_at
FROM mapping_backup;
