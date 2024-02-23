package de.dhbw2go.backend.payload.request.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotNull
    @NotBlank
    @Size(min = 4, max = 32)
    private String username;

    @Email
    @NotNull
    @NotBlank
    @Size(min = 4, max = 64)
    private String email;

    @NotNull
    @NotBlank
    @Size(min = 4, max = 128)
    private String password;
}
