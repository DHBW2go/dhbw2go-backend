package de.dhbw2go.backend.controller;


import de.dhbw2go.backend.entities.User;
import de.dhbw2go.backend.repositories.UserRepository;
import de.dhbw2go.backend.security.SecurityUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@Tag(name = "User")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> info(final Authentication authentication) {
        final SecurityUserDetails securityUserDetails = (SecurityUserDetails) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.OK)
                .body(securityUserDetails.getUser());
    }
}
