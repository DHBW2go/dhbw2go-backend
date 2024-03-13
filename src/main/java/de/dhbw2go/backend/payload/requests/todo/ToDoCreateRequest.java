package de.dhbw2go.backend.payload.requests.todo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ToDoCreateRequest {

    @NotBlank
    private String text;
}
