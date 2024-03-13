package de.dhbw2go.backend.controller;


import de.dhbw2go.backend.entities.User;
import de.dhbw2go.backend.payload.response.AuthenticationCheckResponse;
import de.dhbw2go.backend.repositories.UserRepository;
import de.dhbw2go.backend.security.SecurityUserDetails;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@Tag(name = "User")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = User.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", content = {@Content(schema = @Schema())})
    })
    @GetMapping(path = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> info(final Authentication authentication) {
        final SecurityUserDetails securityUserDetails = (SecurityUserDetails) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(securityUserDetails.getUser());
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = AuthenticationCheckResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
    })
    @GetMapping(path = "/check/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationCheckResponse> check(@PathVariable("username") final String username) {
        final boolean used = this.userRepository.existsByUsername(username);
        final AuthenticationCheckResponse authenticationCheckResponse = new AuthenticationCheckResponse(used);
        return ResponseEntity.status(HttpStatus.OK).body(authenticationCheckResponse);
    }


    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "401", content = {@Content(schema = @Schema())})
    })
    @DeleteMapping(path = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(final Authentication authentication) {
        final SecurityUserDetails securityUserDetails = (SecurityUserDetails) authentication.getPrincipal();
        //TODO: Delete User
        //  -> Cascade delete ToDos
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
