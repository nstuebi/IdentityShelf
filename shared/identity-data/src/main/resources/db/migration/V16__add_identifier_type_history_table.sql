-- V16: Add history table for IdentifierType
-- This creates a history table to track all changes to identifier types

-- Create the history table for identifier_types
CREATE TABLE identifier_types_hst (
    -- History table specific fields
    hst_uuid uuid NOT NULL,
    hst_operation VARCHAR(10) NOT NULL CHECK (hst_operation IN ('INSERT', 'UPDATE', 'DELETE')),
    hst_timestamp TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
    hst_user VARCHAR(100),
    
    -- Original table fields (all as they were at time of change)
    uuid uuid NOT NULL,
    name VARCHAR(100) NOT NULL,
    display_name VARCHAR(200) NOT NULL,
    description VARCHAR(500),
    data_type VARCHAR(50) NOT NULL,
    validation_regex VARCHAR(500),
    default_value VARCHAR(500),
    is_unique BOOLEAN NOT NULL,
    is_searchable BOOLEAN NOT NULL,
    is_active BOOLEAN NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    
    -- Primary key for history table
    PRIMARY KEY (hst_uuid)
);

-- Create indexes for efficient history queries
CREATE INDEX idx_identifier_types_hst_uuid ON identifier_types_hst(uuid);
CREATE INDEX idx_identifier_types_hst_timestamp ON identifier_types_hst(hst_timestamp);
CREATE INDEX idx_identifier_types_hst_operation ON identifier_types_hst(hst_operation);
CREATE INDEX idx_identifier_types_hst_user ON identifier_types_hst(hst_user);

-- Create triggers to automatically populate history table
CREATE OR REPLACE FUNCTION fn_identifier_types_history_trigger()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO identifier_types_hst (
            hst_uuid, hst_operation, hst_timestamp, hst_user,
            uuid, name, display_name, description, data_type, validation_regex, 
            default_value, is_unique, is_searchable, is_active, created_at, updated_at
        ) VALUES (
            gen_random_uuid(), 'DELETE', NOW(), current_user,
            OLD.uuid, OLD.name, OLD.display_name, OLD.description, OLD.data_type, OLD.validation_regex,
            OLD.default_value, OLD.is_unique, OLD.is_searchable, OLD.is_active, OLD.created_at, OLD.updated_at
        );
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO identifier_types_hst (
            hst_uuid, hst_operation, hst_timestamp, hst_user,
            uuid, name, display_name, description, data_type, validation_regex, 
            default_value, is_unique, is_searchable, is_active, created_at, updated_at
        ) VALUES (
            gen_random_uuid(), 'UPDATE', NOW(), current_user,
            NEW.uuid, NEW.name, NEW.display_name, NEW.description, NEW.data_type, NEW.validation_regex,
            NEW.default_value, NEW.is_unique, NEW.is_searchable, NEW.is_active, NEW.created_at, NEW.updated_at
        );
        RETURN NEW;
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO identifier_types_hst (
            hst_uuid, hst_operation, hst_timestamp, hst_user,
            uuid, name, display_name, description, data_type, validation_regex, 
            default_value, is_unique, is_searchable, is_active, created_at, updated_at
        ) VALUES (
            gen_random_uuid(), 'INSERT', NOW(), current_user,
            NEW.uuid, NEW.name, NEW.display_name, NEW.description, NEW.data_type, NEW.validation_regex,
            NEW.default_value, NEW.is_unique, NEW.is_searchable, NEW.is_active, NEW.created_at, NEW.updated_at
        );
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- Create the triggers
CREATE TRIGGER trig_identifier_types_history_insert
    AFTER INSERT ON identifier_types
    FOR EACH ROW EXECUTE FUNCTION fn_identifier_types_history_trigger();

CREATE TRIGGER trig_identifier_types_history_update
    AFTER UPDATE ON identifier_types
    FOR EACH ROW EXECUTE FUNCTION fn_identifier_types_history_trigger();

CREATE TRIGGER trig_identifier_types_history_delete
    AFTER DELETE ON identifier_types
    FOR EACH ROW EXECUTE FUNCTION fn_identifier_types_history_trigger();

-- Add comments for documentation
COMMENT ON TABLE identifier_types_hst IS 'History table for identifier_types - tracks all changes with timestamps and operations';
COMMENT ON COLUMN identifier_types_hst.hst_uuid IS 'Unique identifier for this history record';
COMMENT ON COLUMN identifier_types_hst.hst_operation IS 'Type of operation: INSERT, UPDATE, or DELETE';
COMMENT ON COLUMN identifier_types_hst.hst_timestamp IS 'When this change occurred';
COMMENT ON COLUMN identifier_types_hst.hst_user IS 'Database user who made the change';
COMMENT ON COLUMN identifier_types_hst.uuid IS 'Original identifier type UUID';
