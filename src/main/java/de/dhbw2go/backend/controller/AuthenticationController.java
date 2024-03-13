package de.dhbw2go.backend.controller;

import de.dhbw2go.backend.entities.Role;
import de.dhbw2go.backend.entities.Role.RoleType;
import de.dhbw2go.backend.entities.User;
import de.dhbw2go.backend.payload.request.user.AuthenticationChangePasswordRequest;
import de.dhbw2go.backend.payload.request.user.AuthenticationLoginRequest;
import de.dhbw2go.backend.payload.request.user.AuthenticationRegisterRequest;
import de.dhbw2go.backend.repositories.RoleRepository;
import de.dhbw2go.backend.repositories.UserRepository;
import de.dhbw2go.backend.security.SecurityUserDetails;
import de.dhbw2go.backend.security.jwt.JWTHelper;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authentication")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@Tag(name = "Authentication")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTHelper jwtHelper;

    private Role userRole;

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = User.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
    })
    @PutMapping(path = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> register(@Valid @RequestBody final AuthenticationRegisterRequest authenticationRegisterRequest) {
        if (!this.userRepository.existsByUsername(authenticationRegisterRequest.getUsername())) {
            final User user = new User();
            user.setUsername(authenticationRegisterRequest.getUsername());
            user.setPassword(this.passwordEncoder.encode(authenticationRegisterRequest.getPassword()));
            user.addRole(this.getUserRole());
            this.userRepository.save(user);
            try {
                final Pair<SecurityUserDetails, ResponseCookie> login = this.login(authenticationRegisterRequest.getUsername(), authenticationRegisterRequest.getPassword());
                return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.SET_COOKIE, login.getSecond().toString()).body(login.getFirst().getUser());
            } catch (final AuthenticationException exception) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = User.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
    })
    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> login(@Valid @RequestBody final AuthenticationLoginRequest authenticationLoginRequest) {
        try {
            final Pair<SecurityUserDetails, ResponseCookie> login = this.login(authenticationLoginRequest.getUsername(), authenticationLoginRequest.getPassword());
            return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.SET_COOKIE, login.getSecond().toString()).body(login.getFirst().getUser());
        } catch (final AuthenticationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = User.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", content = {@Content(schema = @Schema())}),
    })
    @PostMapping(path = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> logout() {
        final ResponseCookie responseCookie = this.jwtHelper.getCleanJWTCookie();
        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.SET_COOKIE, responseCookie.toString()).build();
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = User.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "401", content = {@Content(schema = @Schema())})
    })
    @PostMapping(path = "/change-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changePassword(final Authentication authentication, @Valid @RequestBody final AuthenticationChangePasswordRequest authenticationChangePasswordRequest) {
        final SecurityUserDetails securityUserDetails = (SecurityUserDetails) authentication.getPrincipal();
        if (this.passwordEncoder.matches(authenticationChangePasswordRequest.getOldPassword(), securityUserDetails.getPassword())) {
            securityUserDetails.getUser().setPassword(this.passwordEncoder.encode(authenticationChangePasswordRequest.getNewPassword()));
            this.userRepository.save(securityUserDetails.getUser());
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    private Pair<SecurityUserDetails, ResponseCookie> login(final String username, final String password) {
        final Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final SecurityUserDetails securityUserDetails = (SecurityUserDetails) authentication.getPrincipal();
        final ResponseCookie responseCookie = this.jwtHelper.generateJWTCookie(securityUserDetails);
        return Pair.of(securityUserDetails, responseCookie);
    }

    private Role getUserRole() {
        if (userRole == null) {
            this.userRole = this.roleRepository.findByType(RoleType.USER).orElseGet(() -> {
                final Role role = new Role();
                role.setType(RoleType.USER);
                return this.roleRepository.save(role);
            });
        }
        return this.userRole;
    }
}
