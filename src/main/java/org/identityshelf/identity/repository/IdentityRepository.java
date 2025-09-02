package org.identityshelf.identity.repository;

import java.util.UUID;
import org.identityshelf.identity.domain.Identity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdentityRepository extends JpaRepository<Identity, UUID> {
}


