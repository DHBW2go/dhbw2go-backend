package de.dhbw2go.backend.payload.requests.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthenticationLoginRequest {

    @Email
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
