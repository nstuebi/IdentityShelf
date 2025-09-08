# IdentityShelf Workspace Rules

## DDD Architecture Enforcement

When working on the IdentityShelf backend, always follow these DDD principles:

### Domain Layer (identity-core)
- **MUST**: Contain only pure business logic
- **MUST NOT**: Use Spring, JPA, Lombok, or any framework dependencies
- **MUST**: Define repository interfaces here
- **MUST**: Use explicit getters/setters, no annotations

### Persistence Layer (identity-data)  
- **MUST**: Implement repository interfaces from identity-core
- **MUST**: Use JPA entities for database mapping
- **CAN**: Use Lombok for boilerplate reduction
- **MUST**: Handle all database-specific concerns

### Service Layer
- **MUST**: Depend on identity-core for business logic
- **MUST**: Use identity-data for persistence
- **MUST NOT**: Mix domain logic with persistence concerns

### Code Organization
- **Domain entities**: `shared/identity-core/src/main/java/org/identityshelf/core/domain/`
- **JPA entities**: `shared/identity-data/src/main/java/org/identityshelf/data/entity/`
- **Repository interfaces**: `shared/identity-core/src/main/java/org/identityshelf/core/repository/`
- **Repository implementations**: `shared/identity-data/src/main/java/org/identityshelf/data/repository/`

### API Extension Decision Rule
- **MUST**: When extending the admin UI, always ask the user where the backend extension should be implemented
- **Options**: 
  - `services/admin-api/` - For admin-specific functionality only
  - `services/public-api/` - For functionality that could be useful for other API consumers
- **Rationale**: Ensures proper architectural separation and avoids unnecessary coupling between admin-specific features and public APIs

### Memory Rules
- Always use 'uuid' instead of 'id' for database column names when working with UUID data types
- Never edit existing database migration files; always create new ones
- Never automatically commit changes to git
- Always use proper UUID data type in PostgreSQL database columns with @Column(columnDefinition = "uuid")
