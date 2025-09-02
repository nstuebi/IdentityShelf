-- Remove username and email attribute mappings from CUSTOMER identity type
-- CUSTOMER type should not require username/email login credentials

DELETE FROM identity_type_attribute_mappings 
WHERE identity_type_uuid = (SELECT uuid FROM identity_types WHERE name = 'CUSTOMER')
  AND attribute_type_uuid IN (
    SELECT uuid FROM attribute_types WHERE name IN ('username', 'email')
  );
