package de.dhbw2go.backend.repositories;

import de.dhbw2go.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(final String username);

    boolean existsByUsername(final String username);
}
