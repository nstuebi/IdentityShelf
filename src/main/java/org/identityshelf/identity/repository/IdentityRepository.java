package org.identityshelf.identity.repository;

import java.util.UUID;
import org.identityshelf.identity.domain.Identity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdentityRepository extends JpaRepository<Identity, UUID> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsernameAndUuidNot(String username, UUID uuid);
    boolean existsByEmailAndUuidNot(String email, UUID uuid);
}


