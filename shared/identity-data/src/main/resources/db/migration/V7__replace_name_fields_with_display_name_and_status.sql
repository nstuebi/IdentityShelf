-- Migration to replace firstName/lastName with displayName and add status field

-- Add new columns
ALTER TABLE identities ADD COLUMN display_name VARCHAR(255);
ALTER TABLE identities ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE';


-- Set status based on existing data (assuming all existing identities are ACTIVE)
UPDATE identities SET status = 'ACTIVE' WHERE status IS NULL;

-- Make display_name NOT NULL after data migration
ALTER TABLE identities ALTER COLUMN display_name SET NOT NULL;

-- Drop old columns
ALTER TABLE identities DROP COLUMN first_name;
ALTER TABLE identities DROP COLUMN last_name;

-- Add constraint for status values
ALTER TABLE identities ADD CONSTRAINT chk_status_values 
    CHECK (status IN ('ACTIVE', 'SUSPENDED', 'ARCHIVED', 'ESTABLISHED'));

-- Create index on status for better query performance
CREATE INDEX idx_identities_status ON identities(status);
