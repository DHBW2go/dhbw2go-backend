package de.dhbw2go.backend.payload.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthenticationChangePasswordRequest {

    @NotNull
    @NotBlank
    @Size(min = 4, max = 128)
    private String oldPassword;

    @NotNull
    @NotBlank
    @Size(min = 4, max = 128)
    private String newPassword;
}