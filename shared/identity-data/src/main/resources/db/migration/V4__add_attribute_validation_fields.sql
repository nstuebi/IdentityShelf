-- Add min_length and max_length columns to attribute_types table for configurable validation
ALTER TABLE attribute_types ADD COLUMN min_length INTEGER;
ALTER TABLE attribute_types ADD COLUMN max_length INTEGER;

-- Update existing username attribute to have min_length = 3 and max_length = 100
UPDATE attribute_types SET min_length = 3, max_length = 100 WHERE name = 'username';

-- Update existing email attribute to have max_length = 320
UPDATE attribute_types SET max_length = 320 WHERE name = 'email';

-- Update existing first_name and last_name attributes to have max_length = 100
UPDATE attribute_types SET max_length = 100 WHERE name IN ('first_name', 'last_name');
