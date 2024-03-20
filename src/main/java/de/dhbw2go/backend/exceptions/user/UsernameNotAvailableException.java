package de.dhbw2go.backend.exceptions.user;

public class UsernameNotAvailableException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UsernameNotAvailableException(final String username) {
        super(String.format("Username is not available: %s", username));
    }
}
