# Identity Core - Domain Layer

This module contains the core domain logic following Domain-Driven Design (DDD) principles.

## Structure

```
src/main/java/org/identityshelf/core/
├── domain/
│   ├── aggregate/           # Aggregate Roots
│   │   └── Identity.java    # Main Identity aggregate
│   ├── valueobject/         # Value Objects
│   │   ├── IdentityId.java
│   │   ├── DisplayName.java
│   │   ├── IdentityStatus.java
│   │   └── AttributeValue.java
│   ├── service/             # Domain Services
│   │   └── IdentityDomainService.java
│   ├── event/               # Domain Events
│   │   ├── DomainEvent.java
│   │   └── IdentityCreatedEvent.java
│   └── exception/           # Domain Exceptions
│       ├── DomainException.java
│       ├── IdentityNotFoundException.java
│       └── IdentityValidationException.java
├── repository/              # Repository Interfaces
│   └── IdentityRepository.java
└── application/             # Application Layer
    ├── service/             # Application Services (Use Cases)
    │   └── IdentityApplicationService.java
    ├── command/             # Commands (CQRS)
    │   └── CreateIdentityCommand.java
    ├── query/               # Queries (CQRS)
    │   └── FindIdentityByIdQuery.java
    └── dto/                 # Data Transfer Objects
        └── IdentityDto.java
```

## DDD Principles Applied

### 1. **Aggregates**
- `Identity` is the main aggregate root
- Encapsulates all business logic and invariants
- Manages its own lifecycle and state changes

### 2. **Value Objects**
- `IdentityId`: Immutable identifier
- `DisplayName`: Validated display name with business rules
- `IdentityStatus`: Enum with business behavior
- `AttributeValue`: Immutable attribute value

### 3. **Domain Services**
- `IdentityDomainService`: Business logic that doesn't belong to a single aggregate
- Contains complex validation and business rules

### 4. **Domain Events**
- `IdentityCreatedEvent`: Published when identity is created
- Enables loose coupling between bounded contexts

### 5. **Repository Pattern**
- `IdentityRepository`: Interface defined in domain layer
- Implementation will be in infrastructure layer

### 6. **Application Services**
- `IdentityApplicationService`: Orchestrates domain objects
- Implements use cases and coordinates between layers

### 7. **CQRS Support**
- Commands for write operations
- Queries for read operations
- Clear separation of concerns

## Key Features

### Rich Domain Model
- Business logic encapsulated in domain objects
- No anemic domain model
- Invariants enforced at the domain level

### Immutable Value Objects
- All value objects are immutable
- Proper equals/hashCode implementation
- Validation in constructors

### Domain Events
- Events published when significant domain changes occur
- Enables event-driven architecture
- Loose coupling between bounded contexts

### Clean Architecture
- No framework dependencies in domain layer
- Pure business logic
- Testable in isolation

## Usage Example

```java
// Create a new identity
Identity identity = Identity.create(
    DisplayName.of("John Doe"),
    "EMPLOYEE",
    Map.of("email", AttributeValue.of("john@example.com", "email"))
);

// Business operations
identity.activate();
identity.updateDisplayName(DisplayName.of("John Smith"));

// Domain events
List<DomainEvent> events = identity.getDomainEvents();
```

## Testing

The domain layer is designed to be easily testable:
- No external dependencies
- Pure business logic
- Clear interfaces
- Immutable value objects
