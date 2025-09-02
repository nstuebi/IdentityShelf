-- Remove direct username and email columns from identities table
-- These are now handled as dynamic attributes through the EAV pattern

-- Drop the unique constraints first
ALTER TABLE identities DROP CONSTRAINT IF EXISTS identities_username_key;
ALTER TABLE identities DROP CONSTRAINT IF EXISTS identities_email_key;

-- Drop the columns
ALTER TABLE identities DROP COLUMN username;
ALTER TABLE identities DROP COLUMN email;

-- Add the missing mappings for username and email attributes to all identity types
-- Get the attribute type UUIDs and identity type UUIDs, then create the mappings

-- Create mappings for DEFAULT identity type
INSERT INTO identity_type_attribute_mappings (uuid, identity_type_uuid, attribute_type_uuid, sort_order, is_required, is_active, created_at, updated_at)
SELECT 
    gen_random_uuid(),
    it.uuid,
    at.uuid,
    CASE 
        WHEN at.name = 'username' THEN 1
        WHEN at.name = 'email' THEN 2
    END as sort_order,
    true as is_required,
    true as is_active,
    NOW(),
    NOW()
FROM identity_types it, attribute_types at 
WHERE it.name = 'DEFAULT' 
  AND at.name IN ('username', 'email')
  AND NOT EXISTS (
    SELECT 1 FROM identity_type_attribute_mappings m 
    WHERE m.identity_type_uuid = it.uuid 
      AND m.attribute_type_uuid = at.uuid
  );

-- Create mappings for CUSTOMER identity type
INSERT INTO identity_type_attribute_mappings (uuid, identity_type_uuid, attribute_type_uuid, sort_order, is_required, is_active, created_at, updated_at)
SELECT 
    gen_random_uuid(),
    it.uuid,
    at.uuid,
    CASE 
        WHEN at.name = 'username' THEN 1
        WHEN at.name = 'email' THEN 2
    END as sort_order,
    true as is_required,
    true as is_active,
    NOW(),
    NOW()
FROM identity_types it, attribute_types at 
WHERE it.name = 'CUSTOMER' 
  AND at.name IN ('username', 'email')
  AND NOT EXISTS (
    SELECT 1 FROM identity_type_attribute_mappings m 
    WHERE m.identity_type_uuid = it.uuid 
      AND m.attribute_type_uuid = at.uuid
  );

-- Create mappings for EMPLOYEE identity type
INSERT INTO identity_type_attribute_mappings (uuid, identity_type_uuid, attribute_type_uuid, sort_order, is_required, is_active, created_at, updated_at)
SELECT 
    gen_random_uuid(),
    it.uuid,
    at.uuid,
    CASE 
        WHEN at.name = 'username' THEN 1
        WHEN at.name = 'email' THEN 2
    END as sort_order,
    true as is_required,
    true as is_active,
    NOW(),
    NOW()
FROM identity_types it, attribute_types at 
WHERE it.name = 'EMPLOYEE' 
  AND at.name IN ('username', 'email')
  AND NOT EXISTS (
    SELECT 1 FROM identity_type_attribute_mappings m 
    WHERE m.identity_type_uuid = it.uuid 
      AND m.attribute_type_uuid = at.uuid
  );

-- Create mappings for TECHNICAL_USER identity type
INSERT INTO identity_type_attribute_mappings (uuid, identity_type_uuid, attribute_type_uuid, sort_order, is_required, is_active, created_at, updated_at)
SELECT 
    gen_random_uuid(),
    it.uuid,
    at.uuid,
    CASE 
        WHEN at.name = 'username' THEN 1
        WHEN at.name = 'email' THEN 2
    END as sort_order,
    true as is_required,
    true as is_active,
    NOW(),
    NOW()
FROM identity_types it, attribute_types at 
WHERE it.name = 'TECHNICAL_USER' 
  AND at.name IN ('username', 'email')
  AND NOT EXISTS (
    SELECT 1 FROM identity_type_attribute_mappings m 
    WHERE m.identity_type_uuid = it.uuid 
      AND m.attribute_type_uuid = at.uuid
  );
