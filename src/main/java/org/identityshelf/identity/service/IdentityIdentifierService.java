package org.identityshelf.identity.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.identityshelf.identity.domain.Identity;
import org.identityshelf.identity.domain.IdentifierType;
import org.identityshelf.identity.domain.IdentityIdentifier;
import org.identityshelf.identity.repository.IdentityIdentifierRepository;
import org.identityshelf.identity.repository.IdentityRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class IdentityIdentifierService {

    private final IdentityIdentifierRepository identifierRepository;
    private final IdentityRepository identityRepository;
    private final IdentifierTypeService identifierTypeService;

    // Core search functionality
    @Transactional(readOnly = true)
    public List<IdentityIdentifier> findIdentitiesByIdentifierValue(String value) {
        log.debug("Searching for identities with identifier value: {}", value);
        return identifierRepository.findByIdentifierValueWithIdentity(value);
    }

    @Transactional(readOnly = true)
    public Optional<IdentityIdentifier> findIdentityByTypeAndValue(String identifierTypeName, String value) {
        IdentifierType type = identifierTypeService.getIdentifierTypeByName(identifierTypeName);
        return identifierRepository.findByTypeAndValueWithIdentity(type.getUuid(), value);
    }

    @Transactional(readOnly = true)
    public Optional<IdentityIdentifier> findIdentityByTypeAndValue(UUID identifierTypeUuid, String value) {
        return identifierRepository.findByTypeAndValueWithIdentity(identifierTypeUuid, value);
    }

    @Transactional(readOnly = true)
    public List<IdentityIdentifier> findIdentifierSuggestions(String partialValue) {
        return identifierRepository.findByIdentifierValueStartingWithIgnoreCase(partialValue);
    }

    @Transactional(readOnly = true)
    public List<IdentityIdentifier> searchIdentifiers(UUID typeUuid, String value, Boolean verified, Boolean primary) {
        return identifierRepository.findByCriteria(typeUuid, value, verified, primary);
    }

    // Identity-specific operations
    @Transactional(readOnly = true)
    public List<IdentityIdentifier> getIdentifiersForIdentity(UUID identityUuid) {
        return identifierRepository.findByIdentityUuidWithType(identityUuid);
    }

    @Transactional(readOnly = true)
    public Optional<IdentityIdentifier> getPrimaryIdentifierForIdentity(UUID identityUuid) {
        return identifierRepository.findPrimaryByIdentityUuid(identityUuid);
    }

    @Transactional
    public IdentityIdentifier addIdentifierToIdentity(UUID identityUuid, UUID identifierTypeUuid, 
                                                     String value, boolean isPrimary) {
        // Validate identity exists
        Identity identity = identityRepository.findById(identityUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Identity not found"));

        // Validate identifier type exists
        IdentifierType identifierType = identifierTypeService.getIdentifierType(identifierTypeUuid.toString());

        // Validate the identifier value against regex if provided
        if (identifierType.getValidationRegex() != null && !identifierType.getValidationRegex().trim().isEmpty()) {
            if (!Pattern.matches(identifierType.getValidationRegex(), value)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                        "Identifier value does not match required pattern for " + identifierType.getDisplayName());
            }
        }

        // Check for uniqueness if required
        if (identifierType.isUnique()) {
            if (identifierRepository.existsByIdentifierTypeUuidAndIdentifierValueAndActiveTrue(identifierTypeUuid, value)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, 
                        "An identifier of type '" + identifierType.getDisplayName() + "' with value '" + value + "' already exists");
            }
        }

        // If this should be primary, unset any existing primary identifier
        if (isPrimary) {
            Optional<IdentityIdentifier> existingPrimary = getPrimaryIdentifierForIdentity(identityUuid);
            if (existingPrimary.isPresent()) {
                IdentityIdentifier existing = existingPrimary.get();
                existing.setPrimary(false);
                identifierRepository.save(existing);
                log.info("Removed primary status from existing identifier: {} for identity: {}", 
                        existing.getUuid(), identityUuid);
            }
        }

        IdentityIdentifier identifier = new IdentityIdentifier(identity, identifierType, value, isPrimary);
        IdentityIdentifier saved = identifierRepository.save(identifier);
        
        log.info("Added identifier {} ({}) to identity: {}", 
                identifierType.getDisplayName(), value, identityUuid);
        
        return saved;
    }

    @Transactional
    public IdentityIdentifier updateIdentifier(UUID identifierUuid, String newValue, boolean isPrimary) {
        IdentityIdentifier identifier = identifierRepository.findById(identifierUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Identifier not found"));

        IdentifierType identifierType = identifier.getIdentifierType();

        // Validate the new value against regex if provided
        if (identifierType.getValidationRegex() != null && !identifierType.getValidationRegex().trim().isEmpty()) {
            if (!Pattern.matches(identifierType.getValidationRegex(), newValue)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                        "Identifier value does not match required pattern for " + identifierType.getDisplayName());
            }
        }

        // Check for uniqueness if required (excluding current identifier)
        if (identifierType.isUnique() && !identifier.getIdentifierValue().equals(newValue)) {
            if (identifierRepository.existsByIdentifierTypeUuidAndIdentifierValueAndActiveTrueAndUuidNot(
                    identifierType.getUuid(), newValue, identifierUuid)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, 
                        "An identifier of type '" + identifierType.getDisplayName() + "' with value '" + newValue + "' already exists");
            }
        }

        // If this should be primary, unset any existing primary identifier for the same identity
        if (isPrimary && !identifier.isPrimary()) {
            Optional<IdentityIdentifier> existingPrimary = getPrimaryIdentifierForIdentity(identifier.getIdentity().getUuid());
            if (existingPrimary.isPresent()) {
                IdentityIdentifier existing = existingPrimary.get();
                existing.setPrimary(false);
                identifierRepository.save(existing);
                log.info("Removed primary status from existing identifier: {} for identity: {}", 
                        existing.getUuid(), identifier.getIdentity().getUuid());
            }
        }

        identifier.setIdentifierValue(newValue);
        identifier.setPrimary(isPrimary);
        IdentityIdentifier saved = identifierRepository.save(identifier);
        
        log.info("Updated identifier {} to value: {}", identifierUuid, newValue);
        
        return saved;
    }

    @Transactional
    public void deleteIdentifier(UUID identifierUuid) {
        IdentityIdentifier identifier = identifierRepository.findById(identifierUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Identifier not found"));

        // Soft delete by marking as inactive
        identifier.setActive(false);
        identifierRepository.save(identifier);
        
        log.info("Deactivated identifier: {} for identity: {}", 
                identifierUuid, identifier.getIdentity().getUuid());
    }

    @Transactional
    public IdentityIdentifier verifyIdentifier(UUID identifierUuid, String verifiedBy) {
        IdentityIdentifier identifier = identifierRepository.findById(identifierUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Identifier not found"));

        identifier.markAsVerified(verifiedBy);
        IdentityIdentifier saved = identifierRepository.save(identifier);
        
        log.info("Verified identifier: {} by: {}", identifierUuid, verifiedBy);
        
        return saved;
    }

    @Transactional(readOnly = true)
    public List<IdentityIdentifier> getVerifiedIdentifiersForIdentity(UUID identityUuid) {
        return identifierRepository.findByIdentityUuidAndVerifiedTrueAndActiveTrue(identityUuid);
    }

    @Transactional(readOnly = true)
    public long getIdentifierCountByType(UUID identifierTypeUuid) {
        return identifierRepository.countByIdentifierTypeUuid(identifierTypeUuid);
    }
}
