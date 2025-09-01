-- Convert all VARCHAR(36) ID columns to proper UUID type for consistency and best practices

-- Step 1: Drop foreign key constraints that depend on columns we'll convert
ALTER TABLE identities DROP CONSTRAINT IF EXISTS fk_identities_identity_type;
ALTER TABLE identity_attribute_values DROP CONSTRAINT IF EXISTS fk_identity_attribute_values_attribute_type;
ALTER TABLE identity_attribute_values DROP CONSTRAINT IF EXISTS identity_attribute_values_attribute_type_id_fkey;

-- Drop mapping table constraints (these will be recreated later with proper UUID types)
ALTER TABLE identity_type_attribute_mappings DROP CONSTRAINT IF EXISTS fk_mapping_identity_type;
ALTER TABLE identity_type_attribute_mappings DROP CONSTRAINT IF EXISTS fk_mapping_attribute_type;

-- Step 2: Convert identity_types table
-- First update any non-UUID values to proper UUIDs
UPDATE identity_types 
SET id = gen_random_uuid()::text 
WHERE id !~ '^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$';

-- Update foreign key references to match the new UUIDs
UPDATE identities 
SET identity_type_id = (SELECT id FROM identity_types WHERE name = 'default')
WHERE identity_type_id = 'default-type';

-- Now convert to UUID type
ALTER TABLE identity_types ALTER COLUMN id TYPE UUID USING id::uuid;

-- Step 3: Convert attribute_types table  
-- First update any non-UUID values to proper UUIDs
UPDATE attribute_types 
SET id = gen_random_uuid()::text 
WHERE id !~ '^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$';

-- Update foreign key references in identity_attribute_values
UPDATE identity_attribute_values 
SET attribute_type_id = (
    SELECT new_at.id 
    FROM attribute_types new_at 
    WHERE new_at.name = (
        SELECT old_at.name 
        FROM attribute_types old_at 
        WHERE old_at.id = identity_attribute_values.attribute_type_id
    )
)
WHERE attribute_type_id !~ '^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$';

-- Now convert to UUID type
ALTER TABLE attribute_types ALTER COLUMN id TYPE UUID USING id::uuid;

-- Step 4: Convert identity_attribute_values table
ALTER TABLE identity_attribute_values 
    ALTER COLUMN id TYPE UUID USING id::uuid,
    ALTER COLUMN attribute_type_id TYPE UUID USING attribute_type_id::uuid;

-- Step 5: Convert identities table foreign key column
ALTER TABLE identities ALTER COLUMN identity_type_id TYPE UUID USING identity_type_id::uuid;

-- Step 6: Recreate the foreign key constraints
ALTER TABLE identities 
    ADD CONSTRAINT fk_identities_identity_type 
    FOREIGN KEY (identity_type_id) REFERENCES identity_types(id);

ALTER TABLE identity_attribute_values 
    ADD CONSTRAINT fk_identity_attribute_values_attribute_type 
    FOREIGN KEY (attribute_type_id) REFERENCES attribute_types(id);

-- Step 5: Convert identity_type_attribute_mappings table
-- First backup the data with UUID conversion for foreign keys
CREATE TEMPORARY TABLE mapping_backup_v11 AS 
SELECT 
    m.id::uuid as id,
    it.id as identity_type_id,
    at.id as attribute_type_id,
    m.sort_order,
    m.is_required,
    m.override_validation_regex,
    m.override_default_value,
    m.is_active,
    m.created_at,
    m.updated_at
FROM identity_type_attribute_mappings m
JOIN identity_types it ON m.identity_type_id = it.id::text
JOIN attribute_types at ON m.attribute_type_id = at.id::text;

-- Drop the table (constraints will be dropped too)
DROP TABLE identity_type_attribute_mappings;

-- Recreate with proper UUID types
CREATE TABLE identity_type_attribute_mappings (
    id UUID PRIMARY KEY,
    identity_type_id UUID NOT NULL,
    attribute_type_id UUID NOT NULL,
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

-- Restore data (already converted to UUID in backup)
INSERT INTO identity_type_attribute_mappings (
    id, identity_type_id, attribute_type_id, sort_order, is_required,
    override_validation_regex, override_default_value, is_active, created_at, updated_at
)
SELECT
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
FROM mapping_backup_v11;
