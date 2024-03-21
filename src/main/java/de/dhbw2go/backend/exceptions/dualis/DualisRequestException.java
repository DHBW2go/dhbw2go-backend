package de.dhbw2go.backend.exceptions.dualis;

public class DualisRequestException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DualisRequestException(final String url) {
        super(String.format("Failed to request URL: %s", url));
    }
}
