package de.dhbw2go.backend.controller;


import de.dhbw2go.backend.entities.User;
import de.dhbw2go.backend.security.SecurityUserDetails;
import de.dhbw2go.backend.services.UserService;
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
    private UserService userService;

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
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
    })
    @GetMapping(path = "/check/{username}")
    public ResponseEntity<?> check(@PathVariable("username") final String username) {
        if (this.userService.checkUserByUsername(username)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "401", content = {@Content(schema = @Schema())})
    })
    @DeleteMapping(path = "/delete")
    public ResponseEntity<?> delete(final Authentication authentication) {
        final SecurityUserDetails securityUserDetails = (SecurityUserDetails) authentication.getPrincipal();

        //TODO: Delete User
        //  -> Cascade delete ToDos
        //  -> Delete RefreshToken

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
