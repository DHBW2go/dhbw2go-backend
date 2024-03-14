package de.dhbw2go.backend.payload.responses.authentication;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthenticationTokenResponse {

    @NotBlank
    private String accessToken;

    @NotBlank
    private String refreshToken;

    @NotBlank
    private String tokenType = "Bearer";
}
