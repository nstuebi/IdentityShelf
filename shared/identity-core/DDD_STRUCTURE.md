# Clean Domain-Driven Design Structure

## ✅ What's in the Domain Layer (Pure Business Logic)

### Domain Layer (`domain/`)
```
domain/
├── aggregate/           # Aggregate Roots
│   └── Identity.java    # Rich domain model with business logic
├── valueobject/         # Immutable Value Objects
│   ├── IdentityId.java
│   ├── DisplayName.java
│   ├── IdentityStatus.java
│   └── AttributeValue.java
├── service/             # Domain Services
│   └── IdentityDomainService.java
├── event/               # Domain Events
│   ├── DomainEvent.java
│   └── IdentityCreatedEvent.java
└── exception/           # Domain Exceptions
    ├── DomainException.java
    ├── IdentityNotFoundException.java
    └── IdentityValidationException.java
```

### Application Layer (`application/`)
```
application/
├── service/             # Application Services (Use Cases)
│   └── IdentityApplicationService.java
├── command/             # Commands (CQRS)
│   └── CreateIdentityCommand.java
├── query/               # Queries (CQRS)
│   └── FindIdentityByIdQuery.java
└── dto/                 # Data Transfer Objects
    └── IdentityDto.java
```

### Repository Layer (`repository/`)
```
repository/
└── IdentityRepository.java  # Repository interface (contract)
```

## ✅ What's NOT in the Domain Layer (Moved to Infrastructure)

### ❌ Removed from Domain:
- JPA entities with `@Entity` annotations
- Lombok annotations (`@Data`, `@NoArgsConstructor`, etc.)
- Spring annotations (`@Service`, `@Transactional`, etc.)
- Framework-specific imports
- Database-related code

### ✅ These belong in `shared/identity-data/`:
- JPA entities for persistence
- Repository implementations
- Database configuration
- Migration scripts

## Key DDD Principles Applied

1. **✅ Pure Domain Logic** - No framework dependencies
2. **✅ Rich Domain Model** - Business logic in domain objects
3. **✅ Immutable Value Objects** - Type-safe, validated
4. **✅ Domain Events** - Loose coupling
5. **✅ Repository Pattern** - Interface in domain, implementation in infrastructure
6. **✅ Clean Architecture** - Dependencies point inward
7. **✅ CQRS Support** - Commands and queries separated

## Benefits

- **Testable** - No external dependencies
- **Maintainable** - Clear separation of concerns
- **Extensible** - Easy to add business rules
- **Type-Safe** - Value objects prevent invalid states
- **Framework-Agnostic** - Can work with any persistence layer
