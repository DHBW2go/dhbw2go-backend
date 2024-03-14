package de.dhbw2go.backend.payload.requests.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthenticationRegisterRequest {

    @Email
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String name;

    @NotBlank
    private String location;

    @NotBlank
    private String faculty;

    @NotBlank
    private String program;

    @NotBlank
    private String course;
}
