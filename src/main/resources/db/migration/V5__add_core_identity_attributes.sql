-- Add core identity attributes as attribute types for the default identity type
INSERT INTO attribute_types (id, identity_type_id, name, display_name, description, data_type, is_required, default_value, validation_regex, sort_order, is_active, created_at, updated_at) VALUES
('core-username', 'default-type', 'username', 'Username', 'Unique username for the identity', 'STRING', true, NULL, '^[a-zA-Z0-9_]{3,100}$', 1, true, NOW(), NOW()),
('core-email', 'default-type', 'email', 'Email', 'Email address for the identity', 'STRING', true, NULL, '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$', 2, true, NOW(), NOW()),
('core-firstname', 'default-type', 'first_name', 'First Name', 'First name of the person', 'STRING', false, NULL, '^[a-zA-Z\\s\\-\\'']{1,100}$', 3, true, NOW(), NOW()),
('core-lastname', 'default-type', 'last_name', 'Last Name', 'Last name of the person', 'STRING', false, NULL, '^[a-zA-Z\\s\\-\\'']{1,100}$', 4, true, NOW(), NOW());

-- Also add these core attributes to other identity types
INSERT INTO attribute_types (id, identity_type_id, name, display_name, description, data_type, is_required, default_value, validation_regex, sort_order, is_active, created_at, updated_at) VALUES
('emp-username', 'employee-type', 'username', 'Username', 'Unique username for the employee', 'STRING', true, NULL, '^[a-zA-Z0-9_]{3,100}$', 1, true, NOW(), NOW()),
('emp-email', 'employee-type', 'email', 'Email', 'Email address for the employee', 'STRING', true, NULL, '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$', 2, true, NOW(), NOW()),
('emp-firstname', 'employee-type', 'first_name', 'First Name', 'First name of the employee', 'STRING', false, NULL, '^[a-zA-Z\\s\\-\\'']{1,100}$', 3, true, NOW(), NOW()),
('emp-lastname', 'employee-type', 'last_name', 'Last Name', 'Last name of the employee', 'STRING', false, NULL, '^[a-zA-Z\\s\\-\\'']{1,100}$', 4, true, NOW(), NOW()),
('cust-username', 'customer-type', 'username', 'Username', 'Unique username for the customer', 'STRING', true, NULL, '^[a-zA-Z0-9_]{3,100}$', 1, true, NOW(), NOW()),
('cust-email', 'customer-type', 'email', 'Email', 'Email address for the customer', 'STRING', true, NULL, '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$', 2, true, NOW(), NOW()),
('cust-firstname', 'customer-type', 'first_name', 'First Name', 'First name of the customer', 'STRING', false, NULL, '^[a-zA-Z\\s\\-\\'']{1,100}$', 3, true, NOW(), NOW()),
('cust-lastname', 'customer-type', 'last_name', 'Last Name', 'Last name of the customer', 'STRING', false, NULL, '^[a-zA-Z\\s\\-\\'']{1,100}$', 4, true, NOW(), NOW()),
('tech-username', 'technical-user-type', 'username', 'Username', 'Unique username for the technical user', 'STRING', true, NULL, '^[a-zA-Z0-9_]{3,100}$', 1, true, NOW(), NOW()),
('tech-email', 'technical-user-type', 'email', 'Email', 'Email address for the technical user', 'STRING', true, NULL, '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$', 2, true, NOW(), NOW()),
('tech-firstname', 'technical-user-type', 'first_name', 'First Name', 'First name of the technical user', 'STRING', false, NULL, '^[a-zA-Z\\s\\-\\'']{1,100}$', 3, true, NOW(), NOW()),
('tech-lastname', 'technical-user-type', 'last_name', 'Last Name', 'Last name of the technical user', 'STRING', false, NULL, '^[a-zA-Z\\s\\-\\'']{1,100}$', 4, true, NOW(), NOW());
