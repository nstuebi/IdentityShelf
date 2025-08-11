# IdentityShelf Admin Frontend

A React-based admin interface for managing identities and identity types in the IdentityShelf system.

## Features

### Identity Management
- View all identities in a paginated table
- Create new identities with dynamic attributes
- Edit existing identities
- Delete identities

### Identity Type Management (NEW!)
- **View Identity Types**: Browse all identity types with their attributes
- **Create Identity Types**: Define new identity types with custom attributes
- **Edit Identity Types**: Modify existing types and their attributes
- **Attribute Management**: 
  - Add/remove attributes to identity types
  - Configure data types (STRING, INTEGER, BOOLEAN, DATE, etc.)
  - Set required/optional flags
  - Define default values and validation rules
  - Control sort order and active status

## Navigation

- **Identities** (`/`): Main identities list
- **Types** (`/types`): Identity type management
- **New +**: Dropdown to create new identities or identity types

## Identity Types

The system supports configurable identity types such as:
- **EMPLOYEE**: Internal employees with employee ID, department, etc.
- **CUSTOMER**: External customers with customer number, company, etc.
- **TECHNICAL_USER**: System accounts with service name, environment, etc.

## Attribute Types

Each attribute can be configured with:
- **Data Types**: STRING, INTEGER, DECIMAL, BOOLEAN, DATE, DATETIME, EMAIL, PHONE, URL, SELECT, MULTI_SELECT
- **Validation**: Required/optional, regex patterns, default values
- **Display**: Custom display names, descriptions, sort order
- **Status**: Active/inactive attributes

## Development

Built with:
- React 18
- TypeScript
- React Router for navigation
- Inline styles for simplicity

## API Integration

The frontend integrates with the Spring Boot backend via REST APIs:
- `/api/identities` - Identity CRUD operations
- `/api/identity-types` - Identity type management
- `/api/identity-types/{name}/attributes` - Attribute management per type


