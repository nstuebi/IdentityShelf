-- Create the new n:m mapping table between identity types and attribute types
CREATE TABLE identity_type_attribute_mappings (
    id UUID PRIMARY KEY,
    identity_type_id VARCHAR(255) NOT NULL,
    attribute_type_id VARCHAR(255) NOT NULL,
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

-- Migrate existing data from attribute_types to the new mapping table
INSERT INTO identity_type_attribute_mappings (
    id, 
    identity_type_id, 
    attribute_type_id, 
    sort_order, 
    is_required, 
    override_validation_regex, 
    override_default_value, 
    is_active, 
    created_at, 
    updated_at
)
SELECT 
    gen_random_uuid(),
    at.identity_type_id,
    at.id,
    at.sort_order,
    at.is_required,
    NULL, -- No overrides initially
    NULL, -- No overrides initially
    at.is_active,
    at.created_at,
    at.updated_at
FROM attribute_types at 
WHERE at.identity_type_id IS NOT NULL;

-- Remove the old foreign key and related columns from attribute_types
ALTER TABLE attribute_types 
    DROP CONSTRAINT IF EXISTS fk_attribute_identity_type,
    DROP COLUMN IF EXISTS identity_type_id,
    DROP COLUMN IF EXISTS sort_order,
    DROP COLUMN IF EXISTS is_required;
