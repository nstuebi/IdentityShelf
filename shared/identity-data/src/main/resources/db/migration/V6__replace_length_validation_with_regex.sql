-- Remove min_length and max_length columns as we're replacing them with regex validation
ALTER TABLE attribute_types DROP COLUMN IF EXISTS min_length;
ALTER TABLE attribute_types DROP COLUMN IF EXISTS max_length;

-- Update any existing attributes with appropriate regex patterns
-- Phone: optional +, digits, spaces, hyphens, parentheses
UPDATE attribute_types SET validation_regex = '^(\+?[\d\s\-\(\)]{7,20})?$' WHERE name = 'phone';

-- URL: standard URL format
UPDATE attribute_types SET validation_regex = '^https?://[^\s/$.?#].[^\s]*$' WHERE name = 'url';
