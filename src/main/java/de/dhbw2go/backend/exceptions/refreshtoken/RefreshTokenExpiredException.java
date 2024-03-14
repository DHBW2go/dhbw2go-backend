package de.dhbw2go.backend.exceptions.refreshtoken;

public class RefreshTokenExpiredException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RefreshTokenExpiredException(final String token) {
        super(String.format("RefreshToken is expired: %s", token));
    }
}
