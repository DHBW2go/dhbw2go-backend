package de.dhbw2go.backend.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationCheckResponse {

    private boolean used;
}
