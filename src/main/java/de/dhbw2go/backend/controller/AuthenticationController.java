package de.dhbw2go.backend.controller;

import de.dhbw2go.backend.entities.RefreshToken;
import de.dhbw2go.backend.entities.User;
import de.dhbw2go.backend.exceptions.refreshtoken.RefreshTokenExpiredException;
import de.dhbw2go.backend.exceptions.refreshtoken.RefreshTokenNotFoundException;
import de.dhbw2go.backend.exceptions.user.UserNameNotAvailableException;
import de.dhbw2go.backend.jwt.JWTHelper;
import de.dhbw2go.backend.payload.requests.authentication.AuthenticationChangePasswordRequest;
import de.dhbw2go.backend.payload.requests.authentication.AuthenticationLoginRequest;
import de.dhbw2go.backend.payload.requests.authentication.AuthenticationRefreshRequest;
import de.dhbw2go.backend.payload.requests.authentication.AuthenticationRegisterRequest;
import de.dhbw2go.backend.payload.responses.authentication.AuthenticationTokenResponse;
import de.dhbw2go.backend.services.RefreshTokenService;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/authentication")
@Tag(name = "Authentication")
public class AuthenticationController {

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTHelper jwtHelper;

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = AuthenticationTokenResponse.class), mediaType = "application/json")},
                    description = ""),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())},
                    description = ""),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())},
                    description = "")
    })
    @SecurityRequirements
    @PutMapping(path = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationTokenResponse> register(@Valid @RequestBody final AuthenticationRegisterRequest authenticationRegisterRequest) {
        try {
            this.userService.createUser(authenticationRegisterRequest.getUsername().toLowerCase(), authenticationRegisterRequest.getPassword(),
                    authenticationRegisterRequest.getName(), authenticationRegisterRequest.getLocation(),
                    authenticationRegisterRequest.getFaculty(), authenticationRegisterRequest.getProgram(),
                    authenticationRegisterRequest.getCourse());
            final AuthenticationTokenResponse authenticationTokenResponse = this.login(authenticationRegisterRequest.getUsername().toLowerCase(), authenticationRegisterRequest.getPassword());
            return ResponseEntity.status(HttpStatus.OK).body(authenticationTokenResponse);
        } catch (final UserNameNotAvailableException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (final AuthenticationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = AuthenticationTokenResponse.class), mediaType = "application/json")},
                    description = ""),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())},
                    description = "")
    })
    @SecurityRequirements
    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationTokenResponse> login(@Valid @RequestBody final AuthenticationLoginRequest authenticationLoginRequest) {
        try {
            final AuthenticationTokenResponse authenticationTokenResponse = this.login(authenticationLoginRequest.getUsername().toLowerCase(), authenticationLoginRequest.getPassword());
            return ResponseEntity.status(HttpStatus.OK).body(authenticationTokenResponse);
        } catch (final AuthenticationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = AuthenticationTokenResponse.class), mediaType = "application/json")},
                    description = ""),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())},
                    description = ""),
            @ApiResponse(responseCode = "410", content = {@Content(schema = @Schema())},
                    description = ""),
    })
    @SecurityRequirements
    @PostMapping(path = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationTokenResponse> refresh(@Valid @RequestBody final AuthenticationRefreshRequest authenticationRefreshRequest) {
        try {
            final RefreshToken refreshToken = this.refreshTokenService.verifyExpiration(UUID.fromString(authenticationRefreshRequest.getRefreshToken()));
            final String accessToken = this.jwtHelper.generateJWT(refreshToken.getUser().getUsername());
            final AuthenticationTokenResponse authenticationTokenResponse = new AuthenticationTokenResponse();
            authenticationTokenResponse.setAccessToken(accessToken);
            authenticationTokenResponse.setRefreshToken(refreshToken.getToken());
            return ResponseEntity.status(HttpStatus.OK).body(authenticationTokenResponse);
        } catch (final RefreshTokenNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (final RefreshTokenExpiredException exception) {
            return ResponseEntity.status(HttpStatus.GONE).build();
        }
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema())},
                    description = ""),
            @ApiResponse(responseCode = "401", content = {@Content(schema = @Schema())},
                    description = ""),
    })
    @DeleteMapping(path = "/logout")
    public ResponseEntity<?> logout(final Authentication authentication) {
        final User user = (User) authentication.getPrincipal();

        //TODO: Logout User
        //  -> Remove RefreshToken

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema())},
                    description = ""),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())},
                    description = ""),
            @ApiResponse(responseCode = "401", content = {@Content(schema = @Schema())},
                    description = "")
    })
    @PostMapping(path = "/change-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changePassword(final Authentication authentication, @Valid @RequestBody final AuthenticationChangePasswordRequest authenticationChangePasswordRequest) {
        final User user = (User) authentication.getPrincipal();
        boolean isUserPasswordChanged = this.userService.changeUserPassword(user, authenticationChangePasswordRequest.getNewPassword());
        if (isUserPasswordChanged) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    private AuthenticationTokenResponse login(final String username, final String password) throws AuthenticationException {
        final Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final User user = (User) authentication.getPrincipal();
        final String accessToken = this.jwtHelper.generateJWT(username);
        final RefreshToken refreshToken = this.refreshTokenService.createRefreshToken(user);
        final AuthenticationTokenResponse authenticationTokenResponse = new AuthenticationTokenResponse();
        authenticationTokenResponse.setAccessToken(accessToken);
        authenticationTokenResponse.setRefreshToken(refreshToken.getToken());
        return authenticationTokenResponse;
    }
}
