-- Create identity_types table
CREATE TABLE identity_types (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    display_name VARCHAR(200) NOT NULL,
    description TEXT,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- Create attribute_types table
CREATE TABLE attribute_types (
    id VARCHAR(36) PRIMARY KEY,
    identity_type_id VARCHAR(36) NOT NULL,
    name VARCHAR(100) NOT NULL,
    display_name VARCHAR(200) NOT NULL,
    description TEXT,
    data_type VARCHAR(50) NOT NULL,
    is_required BOOLEAN NOT NULL DEFAULT false,
    default_value TEXT,
    validation_regex TEXT,
    sort_order INTEGER NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    FOREIGN KEY (identity_type_id) REFERENCES identity_types(id) ON DELETE CASCADE
);

-- Create identity_attribute_values table
CREATE TABLE identity_attribute_values (
    id VARCHAR(36) PRIMARY KEY,
    identity_id UUID NOT NULL,
    attribute_type_id VARCHAR(36) NOT NULL,
    string_value TEXT,
    integer_value BIGINT,
    decimal_value DOUBLE PRECISION,
    boolean_value BOOLEAN,
    date_value TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    FOREIGN KEY (identity_id) REFERENCES identities(id) ON DELETE CASCADE,
    FOREIGN KEY (attribute_type_id) REFERENCES attribute_types(id) ON DELETE CASCADE
);

-- Insert default identity types FIRST
INSERT INTO identity_types (id, name, display_name, description, created_at, updated_at) VALUES
('default-type', 'DEFAULT', 'Default Identity', 'Default identity type for backward compatibility', NOW(), NOW()),
('employee-type', 'EMPLOYEE', 'Employee', 'Internal employee identity', NOW(), NOW()),
('customer-type', 'CUSTOMER', 'Customer', 'External customer identity', NOW(), NOW()),
('technical-user-type', 'TECHNICAL_USER', 'Technical User', 'System or service account', NOW(), NOW());

-- Add identity_type_id to identities table
ALTER TABLE identities ADD COLUMN identity_type_id VARCHAR(36) NOT NULL DEFAULT 'default-type';
ALTER TABLE identities ADD CONSTRAINT fk_identities_identity_type 
    FOREIGN KEY (identity_type_id) REFERENCES identity_types(id);

-- Create indexes for better performance
CREATE INDEX idx_attribute_types_type_id ON attribute_types(identity_type_id);
CREATE INDEX idx_attribute_types_name ON attribute_types(name);
CREATE INDEX idx_identity_attribute_values_identity_id ON identity_attribute_values(identity_id);
CREATE INDEX idx_identity_attribute_values_attribute_type_id ON identity_attribute_values(attribute_type_id);

-- Insert default attributes for EMPLOYEE type
INSERT INTO attribute_types (id, identity_type_id, name, display_name, description, data_type, is_required, sort_order, created_at, updated_at) VALUES
('emp-attr-1', 'employee-type', 'employee_id', 'Employee ID', 'Internal employee identifier', 'STRING', true, 1, NOW(), NOW()),
('emp-attr-2', 'employee-type', 'department', 'Department', 'Employee department', 'STRING', false, 2, NOW(), NOW()),
('emp-attr-3', 'employee-type', 'position', 'Position', 'Job position/title', 'STRING', false, 3, NOW(), NOW()),
('emp-attr-4', 'employee-type', 'hire_date', 'Hire Date', 'Date when employee was hired', 'DATE', false, 4, NOW(), NOW()),
('emp-attr-5', 'employee-type', 'is_manager', 'Is Manager', 'Whether employee is a manager', 'BOOLEAN', false, 5, NOW(), NOW());

-- Insert default attributes for CUSTOMER type
INSERT INTO attribute_types (id, identity_type_id, name, display_name, description, data_type, is_required, sort_order, created_at, updated_at) VALUES
('cust-attr-1', 'customer-type', 'customer_number', 'Customer Number', 'External customer identifier', 'STRING', true, 1, NOW(), NOW()),
('cust-attr-2', 'customer-type', 'company', 'Company', 'Customer company name', 'STRING', false, 2, NOW(), NOW()),
('cust-attr-3', 'customer-type', 'phone', 'Phone', 'Contact phone number', 'PHONE', false, 3, NOW(), NOW()),
('cust-attr-4', 'customer-type', 'address', 'Address', 'Customer address', 'STRING', false, 4, NOW(), NOW()),
('cust-attr-5', 'customer-type', 'customer_since', 'Customer Since', 'Date when customer was registered', 'DATE', false, 5, NOW(), NOW());

-- Insert default attributes for TECHNICAL_USER type
INSERT INTO attribute_types (id, identity_type_id, name, display_name, description, data_type, is_required, sort_order, created_at, updated_at) VALUES
('tech-attr-1', 'technical-user-type', 'service_name', 'Service Name', 'Name of the service or system', 'STRING', true, 1, NOW(), NOW()),
('tech-attr-2', 'technical-user-type', 'environment', 'Environment', 'Deployment environment', 'SELECT', false, 2, NOW(), NOW()),
('tech-attr-3', 'technical-user-type', 'api_key', 'API Key', 'API key for service authentication', 'STRING', false, 3, NOW(), NOW()),
('tech-attr-4', 'technical-user-type', 'last_access', 'Last Access', 'Last time the service was accessed', 'DATETIME', false, 4, NOW(), NOW()),
('tech-attr-5', 'technical-user-type', 'is_active', 'Is Active', 'Whether the service account is active', 'BOOLEAN', false, 5, NOW(), NOW());

-- Update existing identities to use default type
UPDATE identities SET identity_type_id = 'default-type' WHERE identity_type_id IS NULL;

-- Remove the default constraint after setting all existing records
ALTER TABLE identities ALTER COLUMN identity_type_id DROP DEFAULT;
