-- Fix decimal_value column type to match JPA entity (Double -> DOUBLE PRECISION)
ALTER TABLE identity_attribute_values ALTER COLUMN decimal_value TYPE DOUBLE PRECISION;
