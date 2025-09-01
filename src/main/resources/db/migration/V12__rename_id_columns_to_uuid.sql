-- Rename all 'id' columns to 'uuid' for consistent naming convention

-- Step 1: Drop foreign key constraints that reference columns we'll rename
ALTER TABLE identities DROP CONSTRAINT IF EXISTS fk_identities_identity_type;
ALTER TABLE identity_attribute_values DROP CONSTRAINT IF EXISTS fk_identity_attribute_values_attribute_type;
ALTER TABLE identity_type_attribute_mappings DROP CONSTRAINT IF EXISTS fk_mapping_identity_type;
ALTER TABLE identity_type_attribute_mappings DROP CONSTRAINT IF EXISTS fk_mapping_attribute_type;

-- Step 2: Rename primary key columns to 'uuid'
ALTER TABLE identities RENAME COLUMN id TO uuid;
ALTER TABLE identity_types RENAME COLUMN id TO uuid;
ALTER TABLE attribute_types RENAME COLUMN id TO uuid;
ALTER TABLE identity_attribute_values RENAME COLUMN id TO uuid;
ALTER TABLE identity_type_attribute_mappings RENAME COLUMN id TO uuid;

-- Step 3: Rename foreign key columns to match the new naming convention
ALTER TABLE identities RENAME COLUMN identity_type_id TO identity_type_uuid;
ALTER TABLE identity_attribute_values RENAME COLUMN identity_id TO identity_uuid;
ALTER TABLE identity_attribute_values RENAME COLUMN attribute_type_id TO attribute_type_uuid;
ALTER TABLE identity_type_attribute_mappings RENAME COLUMN identity_type_id TO identity_type_uuid;
ALTER TABLE identity_type_attribute_mappings RENAME COLUMN attribute_type_id TO attribute_type_uuid;

-- Step 4: Recreate foreign key constraints with new column names
ALTER TABLE identities 
    ADD CONSTRAINT fk_identities_identity_type 
    FOREIGN KEY (identity_type_uuid) REFERENCES identity_types(uuid);

ALTER TABLE identity_attribute_values 
    ADD CONSTRAINT fk_identity_attribute_values_identity
    FOREIGN KEY (identity_uuid) REFERENCES identities(uuid);

ALTER TABLE identity_attribute_values 
    ADD CONSTRAINT fk_identity_attribute_values_attribute_type 
    FOREIGN KEY (attribute_type_uuid) REFERENCES attribute_types(uuid);

ALTER TABLE identity_type_attribute_mappings 
    ADD CONSTRAINT fk_mapping_identity_type 
    FOREIGN KEY (identity_type_uuid) REFERENCES identity_types(uuid) ON DELETE CASCADE;

ALTER TABLE identity_type_attribute_mappings 
    ADD CONSTRAINT fk_mapping_attribute_type 
    FOREIGN KEY (attribute_type_uuid) REFERENCES attribute_types(uuid) ON DELETE CASCADE;

-- Step 5: Update indexes to use new column names
DROP INDEX IF EXISTS idx_mapping_identity_type;
DROP INDEX IF EXISTS idx_mapping_attribute_type;
DROP INDEX IF EXISTS idx_mapping_sort_order;

CREATE INDEX idx_mapping_identity_type ON identity_type_attribute_mappings(identity_type_uuid);
CREATE INDEX idx_mapping_attribute_type ON identity_type_attribute_mappings(attribute_type_uuid);
CREATE INDEX idx_mapping_sort_order ON identity_type_attribute_mappings(identity_type_uuid, sort_order);

-- Step 6: Update unique constraints to use new column names
ALTER TABLE identity_type_attribute_mappings DROP CONSTRAINT IF EXISTS uk_identity_type_attribute;
ALTER TABLE identity_type_attribute_mappings 
    ADD CONSTRAINT uk_identity_type_attribute 
    UNIQUE (identity_type_uuid, attribute_type_uuid);
