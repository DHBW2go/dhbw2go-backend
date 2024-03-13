package de.dhbw2go.backend.services;

import de.dhbw2go.backend.entities.RefreshToken;
import de.dhbw2go.backend.entities.User;
import de.dhbw2go.backend.exceptions.refreshtoken.RefreshTokenExpiredException;
import de.dhbw2go.backend.exceptions.refreshtoken.RefreshTokenNotFoundException;
import de.dhbw2go.backend.repositories.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Value("${dhbw2go.jwt.expiration.refresh}")
    private long jwtRefreshExpiration;

    public RefreshToken createRefreshToken(final User user) {
        final RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiration(Instant.now().plusMillis(this.jwtRefreshExpiration));
        refreshToken.setUser(user);
        return this.refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(final UUID token) throws RefreshTokenNotFoundException, RefreshTokenExpiredException {
        final Optional<RefreshToken> refreshTokenOptional = this.refreshTokenRepository.findByToken(token.toString());
        if (refreshTokenOptional.isPresent()) {
            final RefreshToken refreshToken = refreshTokenOptional.get();
            if (refreshTokenOptional.get().getExpiration().isAfter(Instant.now())) {
                return refreshToken;
            }
            this.refreshTokenRepository.delete(refreshToken);
            throw new RefreshTokenExpiredException(refreshToken.getToken());
        }
        throw new RefreshTokenNotFoundException(token.toString());

    }
}
