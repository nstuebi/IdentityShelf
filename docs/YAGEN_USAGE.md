# yaGen Integration Guide

## Overview

yaGen (Yet Another Generator) is now fully integrated into the IdentityShelf project, enabling automatic history table generation and enhanced DDL capabilities through annotations.

## Available yaGen Annotations

### Core Audit Annotations

#### `@Auditable`
Enables automatic history table generation for the annotated entity.

```java
@Entity
@Table(name = "example_table")
@Auditable
public class ExampleEntity {
    // entity fields
}
```

#### `@TemporalEntity`
Marks an entity for temporal/historical tracking with enhanced metadata.

```java
@Entity
@Table(name = "example_table")
@Auditable
@TemporalEntity
public class ExampleEntity {
    // entity fields
}
```

### Other Available yaGen Annotations

- **`@Index`**: Define custom database indexes
- **`@UniqueConstraint`**: Create unique constraints
- **`@CheckConstraint`**: Add check constraints
- **`@Default`**: Set default values for columns
- **`@Sequence`**: Define custom sequences
- **`@CascadeDelete`**: Configure cascade delete behavior
- **`@NoForeignKeyConstraint`**: Disable foreign key generation
- **`@Generated`**: Mark generated columns
- **`@Deferrable`**: Make constraints deferrable

## Implementation Examples

### Basic Entity with History

```java
@Entity
@Table(name = "my_entities")
@Auditable
@TemporalEntity
@Data
@NoArgsConstructor
public class MyEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", columnDefinition = "uuid")
    private UUID uuid;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
        updatedAt = OffsetDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}
```

### Entity with Custom Constraints

```java
@Entity
@Table(name = "products")
@Auditable
@CheckConstraint("price > 0")
@Index({"category", "active"})
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;
    
    @Column(name = "name", nullable = false)
    @UniqueConstraint
    private String name;
    
    @Column(name = "price")
    @Default("0.00")
    private BigDecimal price;
    
    @Column(name = "category")
    private String category;
    
    @Column(name = "active")
    @Default("true")
    private boolean active;
}
```

## Currently Enabled Entities

The following entities in IdentityShelf are already configured with yaGen annotations:

1. **`IdentifierType`** - Full audit trail with `@Auditable` and `@TemporalEntity`
2. **`AttributeType`** - Full audit trail with `@Auditable` and `@TemporalEntity`
3. **`Identity`** - Full audit trail with `@Auditable` and `@TemporalEntity`

## History Table Generation

### Automatic Generation

When yaGen is enabled, history tables are automatically generated with the following features:

- **Table Suffix**: `_HST` (configurable via `spring.jpa.properties.yagen.generator.history.suffix`)
- **Operation Tracking**: INSERT, UPDATE, DELETE operations
- **Timestamp Tracking**: When changes occurred
- **User Tracking**: Who made the changes
- **Complete Data Snapshot**: Full entity state at time of change

### Manual Query Examples

```java
// Service method to get entity history
@Service
public class EntityHistoryService {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public List<IdentifierTypeHistory> getIdentifierTypeHistory(UUID entityId) {
        return entityManager.createQuery(
            "SELECT h FROM IdentifierTypeHistory h WHERE h.uuid = :entityId ORDER BY h.timestamp DESC",
            IdentifierTypeHistory.class)
            .setParameter("entityId", entityId)
            .getResultList();
    }
}
```

## Configuration

### Application Properties

The following properties control yaGen behavior:

```properties
# yaGen Configuration
spring.jpa.properties.yagen.generator.output.dir=target/generated-ddl
spring.jpa.properties.yagen.generator.history.enabled=true
spring.jpa.properties.yagen.generator.history.suffix=_HST
spring.jpa.properties.yagen.generator.history.tables=identifier_types,attribute_types,identities
spring.jpa.properties.yagen.generator.history.mode=AUTO
```

### Gradle Dependencies

```gradle
// yaGen for enhanced DDL generation and audit support
implementation 'com.github.gekoh.yagen:yagen-api:5.10.2'
implementation 'com.github.gekoh.yagen:yagen-generator-lib:5.10.2'
```

## DDL Generation

### Generate DDL with yaGen

```bash
# Generate DDL including history tables
./gradlew generateDDL

# Check generated output
cat target/generated-ddl/create.sql
```

### Expected Output

History tables will be automatically generated with names like:
- `identifier_types_hst`
- `attribute_types_hst`
- `identities_hst`

## Migration Strategy

### From Manual History Tables to yaGen

If you have existing manual history tables (like the `identifier_types_hst` created by V16 migration), you can:

1. **Keep Both**: Manual triggers + yaGen (for comparison)
2. **Migrate**: Remove manual triggers and rely on yaGen
3. **Hybrid**: Use manual for some entities, yaGen for others

### Recommended Approach

For new entities, always use yaGen annotations:

```java
@Entity
@Table(name = "new_entity")
@Auditable
@TemporalEntity
public class NewEntity {
    // entity definition
}
```

## Best Practices

1. **Always Import**: Include both yaGen imports
   ```java
   import com.github.gekoh.yagen.api.Auditable;
   import com.github.gekoh.yagen.api.TemporalEntity;
   ```

2. **Combine Annotations**: Use both `@Auditable` and `@TemporalEntity` for full functionality

3. **Test DDL Generation**: Always verify generated DDL before applying to production

4. **Monitor Performance**: History tables can grow large; implement archiving strategies

5. **Document Changes**: Update this guide when adding new yaGen features

## Troubleshooting

### Import Errors

If you see "cannot be resolved to a type" errors:
1. Ensure gradle dependencies are correct
2. Refresh IDE/restart
3. Clean and rebuild project

### DDL Generation Issues

1. Check yaGen configuration in `application.properties`
2. Verify entity annotations are correct
3. Review generated DDL for conflicts

### Runtime Issues

1. Check Hibernate compatibility
2. Verify database schema matches generated DDL
3. Review entity mapping configurations
