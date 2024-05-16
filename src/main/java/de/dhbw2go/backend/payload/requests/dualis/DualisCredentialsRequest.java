package de.dhbw2go.backend.payload.requests.dualis;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DualisCredentialsRequest {

    @Email
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
