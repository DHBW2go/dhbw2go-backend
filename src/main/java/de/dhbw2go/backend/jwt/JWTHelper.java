package de.dhbw2go.backend.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(JWTHelper.class);
    private static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    @Value("${dhbw2go.jwt.expiration.token}")
    private long jwtTokenExpiration;


    public String getJWTFromHeader(final HttpServletRequest httpServletRequest) {
        final String authorizationHeader = httpServletRequest.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    public String generateJWT(final String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + this.jwtTokenExpiration))
                .signWith(JWTHelper.KEY)
                .compact();
    }

    public String getUsernameFromJWT(final String jwtToken) {
        return Jwts.parser().setSigningKey(JWTHelper.KEY).build().parseClaimsJws(jwtToken).getBody().getSubject();
    }

    public boolean validateJWT(final String jwtToken) {
        try {
            Jwts.parser().setSigningKey(JWTHelper.KEY).build().parseClaimsJws(jwtToken);
            return true;
        } catch (final SignatureException exception) {
            JWTHelper.LOGGER.error("Invalid JWT signature: {}", exception.getMessage());
        } catch (final MalformedJwtException exception) {
            JWTHelper.LOGGER.debug("Invalid JWT token: {}", exception.getMessage());
        } catch (final ExpiredJwtException exception) {
            JWTHelper.LOGGER.debug("Expired JWT token: {}", exception.getMessage());
        } catch (final UnsupportedJwtException exception) {
            JWTHelper.LOGGER.debug("Unsupported JWT token: {}", exception.getMessage());
        } catch (final IllegalArgumentException exception) {
            JWTHelper.LOGGER.debug("Empty JWT token: {}", exception.getMessage());
        }
        return false;
    }
}
