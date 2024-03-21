package de.dhbw2go.backend.exceptions.todo;

public class ToDoNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ToDoNotFoundException(final int todoId) {
        super(String.format("ToDo with id '%s' was not found!", todoId));
    }
}
