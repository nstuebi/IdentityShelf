# DDD Architecture Rules for Identity Backend

When working on the IdentityShelf backend, strictly follow Domain-Driven Design (DDD) principles:

## 1. Domain Layer (identity-core)
- **Purpose**: Contains pure business logic and domain concepts
- **Location**: `shared/identity-core/`
- **Contents**:
  - Domain entities (aggregates)
  - Value objects
  - Domain services
  - Repository interfaces
  - Domain events
  - Domain exceptions
  - Business rules and invariants
- **Restrictions**:
  - NO framework dependencies (Spring, JPA, Lombok)
  - NO persistence annotations
  - NO external library dependencies except basic Java types
  - Pure Java classes with explicit getters/setters

## 2. Persistence Layer (identity-data)
- **Purpose**: Handles data persistence and database operations
- **Location**: `shared/identity-data/`
- **Contents**:
  - JPA entities (database representations)
  - Repository implementations
  - Database-specific configurations
  - Data access objects (DAOs)
- **Dependencies**:
  - Implements repository interfaces from identity-core
  - Uses JPA/Hibernate for persistence
  - Can use Lombok for boilerplate reduction

## 3. Service Layer Architecture
- **Services depend on**: identity-core (business logic)
- **Services use**: identity-data (persistence)
- **Never mix**: Domain logic with persistence concerns
- **Pattern**: Service → Domain → Persistence

## 4. Clean Architecture Principles
- **Domain layer is the core**: Framework-agnostic, pure business logic
- **Dependency direction**: All layers depend on domain layer, never reverse
- **Interface segregation**: Define contracts in domain layer
- **Dependency inversion**: Depend on abstractions, not concretions

## 5. Repository Pattern
- **Define interfaces**: In identity-core
- **Implement**: In identity-data
- **Example**:
  ```java
  // identity-core
  public interface IdentityRepository {
      Identity findById(IdentityId id);
      void save(Identity identity);
  }
  
  // identity-data
  @Repository
  public class JpaIdentityRepository implements IdentityRepository {
      // JPA implementation
  }
  ```

## 6. Domain Events
- **Define**: In identity-core
- **Purpose**: Cross-aggregate communication
- **Example**: IdentityCreatedEvent, IdentityUpdatedEvent

## 7. Value Objects
- **Define**: In identity-core
- **Purpose**: Complex domain concepts (Email, PhoneNumber, etc.)
- **Characteristics**: Immutable, no identity, equality by value

## 8. Aggregates
- **Design**: Clear boundaries and consistency rules
- **Root**: Single aggregate root per aggregate
- **Invariants**: Enforce business rules within aggregate boundaries

## 9. Implementation Guidelines
- **Domain entities**: Rich behavior, not just data containers
- **Repository interfaces**: Define in domain, implement in data layer
- **Domain services**: For complex business logic that doesn't belong to entities
- **Application services**: Orchestrate domain operations and external services
- **DTOs**: For data transfer, separate from domain entities

## 10. File Organization
```
shared/identity-core/
├── domain/
│   ├── aggregate/          # Aggregate roots
│   ├── valueobject/        # Value objects
│   ├── service/           # Domain services
│   ├── event/             # Domain events
│   └── exception/         # Domain exceptions
├── repository/            # Repository interfaces
└── application/           # Application services

shared/identity-data/
├── entity/                # JPA entities
├── repository/            # Repository implementations
└── config/                # Database configuration
```

## 11. Code Quality Rules
- **Domain layer**: Pure Java, no annotations except validation
- **Persistence layer**: Can use JPA annotations and Lombok
- **Services**: Can use Spring annotations
- **Separation**: Clear boundaries between layers
- **Testing**: Domain layer should be easily testable without frameworks
