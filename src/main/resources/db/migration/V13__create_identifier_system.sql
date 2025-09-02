-- Create identifier system with optimized search capabilities

-- Step 1: Create identifier_types table
CREATE TABLE identifier_types (
    uuid UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    display_name VARCHAR(200) NOT NULL,
    description VARCHAR(500),
    data_type VARCHAR(50) NOT NULL,
    validation_regex VARCHAR(500),
    default_value VARCHAR(500),
    is_unique BOOLEAN NOT NULL DEFAULT TRUE,
    is_searchable BOOLEAN NOT NULL DEFAULT TRUE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- Step 2: Create identity_type_identifier_mappings table
CREATE TABLE identity_type_identifier_mappings (
    uuid UUID PRIMARY KEY,
    identity_type_uuid UUID NOT NULL,
    identifier_type_uuid UUID NOT NULL,
    sort_order INTEGER NOT NULL DEFAULT 0,
    is_required BOOLEAN NOT NULL DEFAULT FALSE,
    is_primary_candidate BOOLEAN NOT NULL DEFAULT FALSE,
    override_validation_regex TEXT,
    override_default_value TEXT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

    -- Foreign key constraints
    CONSTRAINT fk_mapping_identifier_identity_type FOREIGN KEY (identity_type_uuid) REFERENCES identity_types(uuid) ON DELETE CASCADE,
    CONSTRAINT fk_mapping_identifier_type FOREIGN KEY (identifier_type_uuid) REFERENCES identifier_types(uuid) ON DELETE CASCADE,

    -- Unique constraint to prevent duplicate mappings
    CONSTRAINT uk_identity_type_identifier UNIQUE (identity_type_uuid, identifier_type_uuid)
);

-- Step 3: Create identity_identifiers table with comprehensive indexing for fast searches
CREATE TABLE identity_identifiers (
    uuid UUID PRIMARY KEY,
    identity_uuid UUID NOT NULL,
    identifier_type_uuid UUID NOT NULL,
    identifier_value VARCHAR(500) NOT NULL,
    is_primary BOOLEAN NOT NULL DEFAULT FALSE,
    is_verified BOOLEAN NOT NULL DEFAULT FALSE,
    verified_at TIMESTAMP WITH TIME ZONE,
    verified_by VARCHAR(100),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

    -- Foreign key constraints
    CONSTRAINT fk_identity_identifier_identity FOREIGN KEY (identity_uuid) REFERENCES identities(uuid) ON DELETE CASCADE,
    CONSTRAINT fk_identity_identifier_type FOREIGN KEY (identifier_type_uuid) REFERENCES identifier_types(uuid) ON DELETE CASCADE
);

-- Step 4: Create indexes for optimal search performance
-- Primary search index: Find identities by identifier value
CREATE INDEX idx_identity_identifier_value ON identity_identifiers(identifier_value);

-- Type-specific search: Find identifiers by type
CREATE INDEX idx_identity_identifier_type ON identity_identifiers(identifier_type_uuid);

-- Identity lookup: Find all identifiers for an identity
CREATE INDEX idx_identity_identifier_identity ON identity_identifiers(identity_uuid);

-- Combined search: Find by type and value (most common search pattern)
CREATE INDEX idx_identity_identifier_search ON identity_identifiers(identifier_type_uuid, identifier_value);

-- Unique constraint: Prevent duplicate identifiers of the same type (conditional on uniqueness)
CREATE UNIQUE INDEX idx_identity_identifier_unique 
ON identity_identifiers(identifier_type_uuid, identifier_value) 
WHERE is_active = TRUE;

-- Primary identifier lookup: Find primary identifiers quickly
CREATE INDEX idx_identity_identifier_primary ON identity_identifiers(identity_uuid, is_primary) WHERE is_primary = TRUE;

-- Verification status search
CREATE INDEX idx_identity_identifier_verified ON identity_identifiers(is_verified, verified_at);

-- Step 5: Create indexes for mapping tables
CREATE INDEX idx_mapping_identifier_identity_type ON identity_type_identifier_mappings(identity_type_uuid);
CREATE INDEX idx_mapping_identifier_type ON identity_type_identifier_mappings(identifier_type_uuid);
CREATE INDEX idx_mapping_identifier_sort_order ON identity_type_identifier_mappings(identity_type_uuid, sort_order);

-- Step 6: Create indexes for identifier_types
CREATE INDEX idx_identifier_type_name ON identifier_types(name);
CREATE INDEX idx_identifier_type_searchable ON identifier_types(is_searchable) WHERE is_searchable = TRUE;

-- Step 7: Insert some common identifier types
INSERT INTO identifier_types (
    uuid, name, display_name, description, data_type, validation_regex, 
    is_unique, is_searchable, is_active, created_at, updated_at
) VALUES 
(
    gen_random_uuid(),
    'ssn',
    'Social Security Number',
    'US Social Security Number for identity verification',
    'STRING',
    '^\d{3}-?\d{2}-?\d{4}$',
    TRUE,
    TRUE,
    TRUE,
    NOW(),
    NOW()
),
(
    gen_random_uuid(),
    'passport',
    'Passport Number',
    'International passport number',
    'STRING',
    '^[A-Z0-9]{6,12}$',
    TRUE,
    TRUE,
    TRUE,
    NOW(),
    NOW()
),
(
    gen_random_uuid(),
    'driver_license',
    'Driver License Number',
    'State-issued driver license number',
    'STRING',
    '^[A-Z0-9]{8,20}$',
    TRUE,
    TRUE,
    TRUE,
    NOW(),
    NOW()
),
(
    gen_random_uuid(),
    'employee_id',
    'Employee ID',
    'Company employee identification number',
    'STRING',
    '^[A-Z0-9]{3,15}$',
    TRUE,
    TRUE,
    TRUE,
    NOW(),
    NOW()
),
(
    gen_random_uuid(),
    'tax_id',
    'Tax ID Number',
    'Tax identification number',
    'STRING',
    '^\d{2}-?\d{7}$',
    TRUE,
    TRUE,
    TRUE,
    NOW(),
    NOW()
);
