package de.dhbw2go.backend.security.jwt;

import de.dhbw2go.backend.security.SecurityUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.security.Key;
import java.util.Date;

@Component
public class JWTHelper {

    private static final Logger logger = LoggerFactory.getLogger(JWTHelper.class);
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    @Value("${youandmeme.jwt.cookie}")
    private String jwtCookie;
    @Value("${youandmeme.jwt.expiration}")
    private int jwtExpiration;

    public String getJWTFromCookies(final HttpServletRequest httpServletRequest) {
        final Cookie cookie = WebUtils.getCookie(httpServletRequest, this.jwtCookie);
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    public ResponseCookie generateJWTCookie(final SecurityUserDetails userPrincipal) {
        return ResponseCookie.from(this.jwtCookie, this.generateJWTFromEmail(userPrincipal.getUsername()))
                .path("/")
                .maxAge(24 * 60 * 60)
                .httpOnly(true)
                .build();
    }

    public ResponseCookie getCleanJWTCookie() {
        return ResponseCookie.from(this.jwtCookie, null)
                .path("/")
                .build();
    }

    public String getEmailFromJWT(final String jwtToken) {
        return Jwts.parser().setSigningKey(JWTHelper.key).build().parseClaimsJws(jwtToken).getBody().getSubject();
    }

    public boolean validateJWT(final String jwtToken) {
        try {
            Jwts.parser().setSigningKey(JWTHelper.key).build().parseClaimsJws(jwtToken);
            return true;
        } catch (final MalformedJwtException exception) {
            JWTHelper.logger.debug("Invalid JWT token: {}", exception.getMessage());
        } catch (final ExpiredJwtException exception) {
            JWTHelper.logger.debug("Expired JWT token: {}", exception.getMessage());
        } catch (final UnsupportedJwtException exception) {
            JWTHelper.logger.debug("Unsupported JWT token: {}", exception.getMessage());
        } catch (final IllegalArgumentException exception) {
            JWTHelper.logger.debug("Empty JWT token: {}", exception.getMessage());
        }
        return false;
    }

    public String generateJWTFromEmail(final String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + this.jwtExpiration))
                .signWith(JWTHelper.key)
                .compact();
    }
}
