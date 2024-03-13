package de.dhbw2go.backend.payload.requests.authentication;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthenticationRefreshRequest {

    @NotBlank
    private String refreshToken;
}
