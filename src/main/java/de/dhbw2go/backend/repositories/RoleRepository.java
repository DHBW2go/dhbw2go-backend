package de.dhbw2go.backend.repositories;

import de.dhbw2go.backend.entities.Role;
import de.dhbw2go.backend.entities.Role.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByType(final RoleType roleType);
}
