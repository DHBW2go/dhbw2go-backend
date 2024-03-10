package de.dhbw2go.backend.controller;

import de.dhbw2go.backend.entities.Role;
import de.dhbw2go.backend.entities.Role.RoleType;
import de.dhbw2go.backend.entities.User;
import de.dhbw2go.backend.payload.request.ChangePasswordRequest;
import de.dhbw2go.backend.payload.request.LoginRequest;
import de.dhbw2go.backend.payload.request.RegisterRequest;
import de.dhbw2go.backend.repositories.RoleRepository;
import de.dhbw2go.backend.repositories.UserRepository;
import de.dhbw2go.backend.security.SecurityUserDetails;
import de.dhbw2go.backend.security.jwt.JWTHelper;
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

    @PutMapping(path = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> register(@Valid @RequestBody final RegisterRequest registerRequest) {
        if (!this.userRepository.existsByUsername(registerRequest.getUsername())) {
            final User user = new User();
            user.setUsername(registerRequest.getUsername());
            user.setPassword(this.passwordEncoder.encode(registerRequest.getPassword()));
            user.addRole(this.roleRepository.findByType(RoleType.USER).orElseGet(() -> {
                final Role role = new Role();
                role.setType(RoleType.USER);
                return this.roleRepository.save(role);
            }));
            this.userRepository.save(user);
            try {
                final Pair<SecurityUserDetails, ResponseCookie> login = this.login(registerRequest.getUsername(), registerRequest.getPassword());
                return ResponseEntity.status(HttpStatus.OK)
                        .header(HttpHeaders.SET_COOKIE, login.getSecond().toString())
                        .body(login.getFirst().getUser());
            } catch (final AuthenticationException exception) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> login(@Valid @RequestBody final LoginRequest loginRequest) {
        try {
            final Pair<SecurityUserDetails, ResponseCookie> login = this.login(loginRequest.getUsername(), loginRequest.getPassword());
            return ResponseEntity.status(HttpStatus.OK)
                    .header(HttpHeaders.SET_COOKIE, login.getSecond().toString())
                    .body(login.getFirst().getUser());
        } catch (final AuthenticationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping(path = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> logout() {
        final ResponseCookie responseCookie = this.jwtHelper.getCleanJWTCookie();
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString()).build();
    }

    @PostMapping(path = "/change-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changePassword(final Authentication authentication, @Valid @RequestBody final ChangePasswordRequest changePasswordRequest) {
        final SecurityUserDetails securityUserDetails = (SecurityUserDetails) authentication.getPrincipal();
        if (this.passwordEncoder.matches(changePasswordRequest.getOldPassword(), securityUserDetails.getPassword())) {
            securityUserDetails.getUser().setPassword(this.passwordEncoder.encode(changePasswordRequest.getNewPassword()));
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
}
