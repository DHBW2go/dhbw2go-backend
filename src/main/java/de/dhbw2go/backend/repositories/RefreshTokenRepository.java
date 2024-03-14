package de.dhbw2go.backend.repositories;

import de.dhbw2go.backend.entities.RefreshToken;
import de.dhbw2go.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    Optional<RefreshToken> findByToken(final String token);

    @Modifying
    int deleteByUser(final User user);
}
