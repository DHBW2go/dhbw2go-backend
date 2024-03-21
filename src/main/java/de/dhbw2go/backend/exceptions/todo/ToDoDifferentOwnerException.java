package de.dhbw2go.backend.exceptions.todo;

public class ToDoDifferentOwnerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ToDoDifferentOwnerException(final String username, final int todoId) {
        super(String.format("User with username '%s' does not own the ToDo with id '%s'!", username, todoId));
    }
}
