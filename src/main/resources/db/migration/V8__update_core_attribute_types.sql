-- Migration to remove first_name and last_name attribute types since they are now direct fields

-- Remove the first_name and last_name attribute types since they are now direct columns on the identities table
DELETE FROM attribute_types WHERE name IN ('first_name', 'last_name');

-- Note: display_name and status are now direct columns on the identities table, not dynamic attributes
