package de.dhbw2go.backend.controller;


import de.dhbw2go.backend.entities.User;
import de.dhbw2go.backend.payload.requests.user.UserChangeDetailsRequest;
import de.dhbw2go.backend.services.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema())},
                    description = ""),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())},
                    description = "")
    })
    @SecurityRequirements
    @GetMapping(path = "/check/{username}")
    public ResponseEntity<?> check(@PathVariable("username") final String username) {
        if (this.userService.checkUserByUsername(username)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = User.class), mediaType = "application/json")},
                    description = ""),
            @ApiResponse(responseCode = "401", content = {@Content(schema = @Schema())},
                    description = "")
    })
    @GetMapping(path = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> info(final Authentication authentication) {
        final User user = (User) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = User.class), mediaType = "application/json")},
                    description = ""),
            @ApiResponse(responseCode = "401", content = {@Content(schema = @Schema())},
                    description = "")
    })
    @PostMapping(path = "/change-details/")
    public ResponseEntity<User> changeDetails(final Authentication authentication, @Valid @RequestBody final UserChangeDetailsRequest userChangeDetailsRequest) {
        final User user = (User) authentication.getPrincipal();

        this.userService.changeUserDetails(user, userChangeDetailsRequest.getName(),
                userChangeDetailsRequest.getLocation(), userChangeDetailsRequest.getFaculty(),
                userChangeDetailsRequest.getProgram(), userChangeDetailsRequest.getCourse(),
                userChangeDetailsRequest.getImage());
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema())},
                    description = ""),
            @ApiResponse(responseCode = "401", content = {@Content(schema = @Schema())},
                    description = "")
    })
    @DeleteMapping(path = "/delete")
    public ResponseEntity<?> delete(final Authentication authentication) {
        final User user = (User) authentication.getPrincipal();

        //TODO: Delete User
        //  -> Cascade delete ToDos
        //  -> Delete RefreshToken

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
