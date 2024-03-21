package de.dhbw2go.backend.payload.requests.dualis;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DualisCredentialsRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
