package de.dhbw2go.backend.payload.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthenticationLoginRequest {

    @Email
    @NotNull
    @NotBlank
    @Size(min = 4, max = 64)
    private String username;

    @NotNull
    @NotBlank
    @Size(min = 4, max = 128)
    private String password;
}