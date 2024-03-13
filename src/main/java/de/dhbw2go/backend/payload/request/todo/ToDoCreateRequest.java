package de.dhbw2go.backend.payload.request.todo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ToDoCreateRequest {

    @NotNull
    @NotBlank
    @Size(max = 1024)
    private String text;
}
