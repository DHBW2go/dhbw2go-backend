package de.dhbw2go.backend.exceptions.refreshtoken;

public class RefreshTokenNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RefreshTokenNotFoundException(final String token) {
        super(String.format("RefreshToken was not found: %s", token));
    }
}
