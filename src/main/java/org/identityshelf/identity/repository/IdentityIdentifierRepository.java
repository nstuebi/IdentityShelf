package org.identityshelf.identity.repository;

import org.identityshelf.identity.domain.IdentityIdentifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IdentityIdentifierRepository extends JpaRepository<IdentityIdentifier, UUID> {

    // Core search functionality - find identities by identifier value
    @Query("SELECT ii FROM IdentityIdentifier ii " +
           "JOIN FETCH ii.identity i " +
           "JOIN FETCH ii.identifierType it " +
           "WHERE ii.identifierValue = :value AND ii.active = true AND it.searchable = true")
    List<IdentityIdentifier> findByIdentifierValueWithIdentity(@Param("value") String value);

    // Type-specific search - find identities by identifier type and value
    @Query("SELECT ii FROM IdentityIdentifier ii " +
           "JOIN FETCH ii.identity i " +
           "JOIN FETCH ii.identifierType it " +
           "WHERE it.uuid = :typeUuid AND ii.identifierValue = :value AND ii.active = true")
    Optional<IdentityIdentifier> findByTypeAndValueWithIdentity(@Param("typeUuid") UUID typeUuid, @Param("value") String value);

    // Find all identifiers for a specific identity
    List<IdentityIdentifier> findByIdentityUuidAndActiveTrue(UUID identityUuid);

    // Find identifiers by identity with type information
    @Query("SELECT ii FROM IdentityIdentifier ii " +
           "JOIN FETCH ii.identifierType it " +
           "WHERE ii.identity.uuid = :identityUuid AND ii.active = true " +
           "ORDER BY ii.primary DESC, it.displayName ASC")
    List<IdentityIdentifier> findByIdentityUuidWithType(@Param("identityUuid") UUID identityUuid);

    // Find primary identifier for an identity
    @Query("SELECT ii FROM IdentityIdentifier ii " +
           "JOIN FETCH ii.identifierType it " +
           "WHERE ii.identity.uuid = :identityUuid AND ii.primary = true AND ii.active = true")
    Optional<IdentityIdentifier> findPrimaryByIdentityUuid(@Param("identityUuid") UUID identityUuid);

    // Search with partial matching (for autocomplete/suggestions)
    @Query("SELECT ii FROM IdentityIdentifier ii " +
           "JOIN FETCH ii.identity i " +
           "JOIN FETCH ii.identifierType it " +
           "WHERE LOWER(ii.identifierValue) LIKE LOWER(CONCAT(:value, '%')) " +
           "AND ii.active = true AND it.searchable = true " +
           "ORDER BY ii.identifierValue ASC")
    List<IdentityIdentifier> findByIdentifierValueStartingWithIgnoreCase(@Param("value") String value);

    // Find identifiers by type
    List<IdentityIdentifier> findByIdentifierTypeUuidAndActiveTrue(UUID identifierTypeUuid);

    // Check for uniqueness within a type
    boolean existsByIdentifierTypeUuidAndIdentifierValueAndActiveTrue(UUID identifierTypeUuid, String identifierValue);

    // Check for uniqueness excluding a specific identifier (for updates)
    boolean existsByIdentifierTypeUuidAndIdentifierValueAndActiveTrueAndUuidNot(
            UUID identifierTypeUuid, String identifierValue, UUID excludeUuid);

    // Find verified identifiers
    List<IdentityIdentifier> findByIdentityUuidAndVerifiedTrueAndActiveTrue(UUID identityUuid);

    // Advanced search combining multiple criteria
    @Query("SELECT ii FROM IdentityIdentifier ii " +
           "JOIN FETCH ii.identity i " +
           "JOIN FETCH ii.identifierType it " +
           "WHERE (:typeUuid IS NULL OR it.uuid = :typeUuid) " +
           "AND (:value IS NULL OR LOWER(ii.identifierValue) LIKE LOWER(CONCAT('%', :value, '%'))) " +
           "AND (:verified IS NULL OR ii.verified = :verified) " +
           "AND (:primary IS NULL OR ii.primary = :primary) " +
           "AND ii.active = true " +
           "ORDER BY ii.primary DESC, it.displayName ASC, ii.identifierValue ASC")
    List<IdentityIdentifier> findByCriteria(
            @Param("typeUuid") UUID typeUuid,
            @Param("value") String value,
            @Param("verified") Boolean verified,
            @Param("primary") Boolean primary);

    // Count identifiers by type (for statistics)
    @Query("SELECT COUNT(ii) FROM IdentityIdentifier ii WHERE ii.identifierType.uuid = :typeUuid AND ii.active = true")
    long countByIdentifierTypeUuid(@Param("typeUuid") UUID typeUuid);

    // Find duplicate identifier values across types
    @Query("SELECT ii.identifierValue, COUNT(ii) FROM IdentityIdentifier ii " +
           "WHERE ii.active = true " +
           "GROUP BY ii.identifierValue " +
           "HAVING COUNT(ii) > 1")
    List<Object[]> findDuplicateIdentifierValues();
}
