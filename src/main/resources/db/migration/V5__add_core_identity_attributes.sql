-- Add core identity attributes as attribute types for the default identity type
INSERT INTO attribute_types (id, identity_type_id, name, display_name, description, data_type, is_required, min_length, max_length, sort_order, created_at, updated_at) VALUES
('core-username', 'default-type', 'username', 'Username', 'Unique username for the identity', 'STRING', true, 3, 100, 1, NOW(), NOW()),
('core-email', 'default-type', 'email', 'Email', 'Email address for the identity', 'STRING', true, null, 320, 2, NOW(), NOW()),
('core-firstname', 'default-type', 'first_name', 'First Name', 'First name of the person', 'STRING', false, null, 100, 3, NOW(), NOW()),
('core-lastname', 'default-type', 'last_name', 'Last Name', 'Last name of the person', 'STRING', false, null, 100, 4, NOW(), NOW());

-- Also add these core attributes to other identity types
INSERT INTO attribute_types (id, identity_type_id, name, display_name, description, data_type, is_required, min_length, max_length, sort_order, created_at, updated_at) VALUES
('emp-username', 'employee-type', 'username', 'Username', 'Unique username for the employee', 'STRING', true, 3, 100, 1, NOW(), NOW()),
('emp-email', 'employee-type', 'email', 'Email', 'Email address for the employee', 'STRING', true, null, 320, 2, NOW(), NOW()),
('emp-firstname', 'employee-type', 'first_name', 'First Name', 'First name of the employee', 'STRING', false, null, 100, 3, NOW(), NOW()),
('emp-lastname', 'employee-type', 'last_name', 'Last Name', 'Last name of the employee', 'STRING', false, null, 100, 4, NOW(), NOW()),
('cust-username', 'customer-type', 'username', 'Username', 'Unique username for the customer', 'STRING', true, 3, 100, 1, NOW(), NOW()),
('cust-email', 'customer-type', 'email', 'Email', 'Email address for the customer', 'STRING', true, null, 320, 2, NOW(), NOW()),
('cust-firstname', 'customer-type', 'first_name', 'First Name', 'First name of the customer', 'STRING', false, null, 100, 3, NOW(), NOW()),
('cust-lastname', 'customer-type', 'last_name', 'Last Name', 'Last name of the customer', 'STRING', false, null, 100, 4, NOW(), NOW()),
('tech-username', 'technical-user-type', 'username', 'Username', 'Unique username for the technical user', 'STRING', true, 3, 100, 1, NOW(), NOW()),
('tech-email', 'technical-user-type', 'email', 'Email', 'Email address for the technical user', 'STRING', true, null, 320, 2, NOW(), NOW()),
('tech-firstname', 'technical-user-type', 'first_name', 'First Name', 'First name of the technical user', 'STRING', false, null, 100, 3, NOW(), NOW()),
('tech-lastname', 'technical-user-type', 'last_name', 'Last Name', 'Last name of the technical user', 'STRING', false, null, 100, 4, NOW(), NOW());
