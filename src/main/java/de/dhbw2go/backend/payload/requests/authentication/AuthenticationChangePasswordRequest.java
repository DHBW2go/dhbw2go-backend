package de.dhbw2go.backend.payload.requests.authentication;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthenticationChangePasswordRequest {

    @NotBlank
    @Size(min = 4, max = 128)
    private String oldPassword;

    @NotBlank
    @Size(min = 4, max = 128)
    private String newPassword;
}
