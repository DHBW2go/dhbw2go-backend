package de.dhbw2go.backend.repositories;

import de.dhbw2go.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(final String email);

    boolean existsByUsername(final String username);

    boolean existsByEmail(final String email);

    void deleteByEmail(final String email);
}
