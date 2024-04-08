package de.dhbw2go.backend.exceptions.user;

public class UserNameNotAvailableException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UserNameNotAvailableException(final String username) {
        super(String.format("Username is not available: %s", username));
    }
}
