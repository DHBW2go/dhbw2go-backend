package de.dhbw2go.backend.controller;

import de.dhbw2go.backend.entities.Role;
import de.dhbw2go.backend.entities.Role.RoleType;
import de.dhbw2go.backend.entities.User;
import de.dhbw2go.backend.payload.request.ChangePasswordRequest;
import de.dhbw2go.backend.payload.request.LoginRequest;
import de.dhbw2go.backend.payload.request.RegisterRequest;
import de.dhbw2go.backend.payload.response.EventResponse;
import de.dhbw2go.backend.repositories.RoleRepository;
import de.dhbw2go.backend.repositories.UserRepository;
import de.dhbw2go.backend.security.SecurityUserDetails;
import de.dhbw2go.backend.security.jwt.JWTHelper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
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

    @GetMapping(path = "/check/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventResponse> check(@PathVariable("email") final String email) {
        if (!this.userRepository.existsByEmail(email)) {
            return ResponseEntity.ok(new EventResponse(EventResponse.Event.Authentication.EMAIL_AVAILABLE));
        }
        return ResponseEntity.badRequest().body(new EventResponse(EventResponse.Event.Authentication.EMAIL_NOT_AVAILABLE));
    }

    @PutMapping(path = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventResponse> register(@Valid @RequestBody final RegisterRequest registerRequest) {
        if (!this.userRepository.existsByUsername(registerRequest.getUsername())) {
            if (!this.userRepository.existsByEmail(registerRequest.getEmail())) {

                final User user = new User();
                user.setUsername(registerRequest.getUsername());
                user.setEmail(registerRequest.getEmail());
                user.setPassword(this.passwordEncoder.encode(registerRequest.getPassword()));
                user.addRole(this.roleRepository.findByType(RoleType.USER).orElseGet(() -> {
                    final Role role = new Role();
                    role.setType(RoleType.USER);
                    return this.roleRepository.save(role);
                }));
                this.userRepository.save(user);

                return ResponseEntity.ok(new EventResponse(EventResponse.Event.Authentication.REGISTERED));
            }
            return ResponseEntity.badRequest().body(new EventResponse(EventResponse.Event.Authentication.EMAIL_NOT_AVAILABLE));
        }
        return ResponseEntity.badRequest().body(new EventResponse(EventResponse.Event.Authentication.USERNAME_NOT_AVAILABLE));
    }

    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventResponse> login(@Valid @RequestBody final LoginRequest loginRequest) {
        try {
            final Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final SecurityUserDetails securityUserDetails = (SecurityUserDetails) authentication.getPrincipal();
            final ResponseCookie responseCookie = this.jwtHelper.generateJWTCookie(securityUserDetails);
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(new EventResponse(EventResponse.Event.Authentication.LOGIN));
        } catch (final AuthenticationException exception) {
            return ResponseEntity.badRequest().body(new EventResponse(EventResponse.Event.Authentication.AUTHENTICATION_FAILED));
        }
    }

    @PostMapping(path = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventResponse> logout() {
        final ResponseCookie responseCookie = this.jwtHelper.getCleanJWTCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(new EventResponse(EventResponse.Event.Authentication.LOGOUT));
    }

    @PostMapping(path = "/change-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventResponse> changePassword(@Valid @RequestBody final ChangePasswordRequest changePasswordRequest) {
        final SecurityUserDetails securityUserDetails = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final User user = this.userRepository.findByEmail(securityUserDetails.getUsername()).orElseThrow(() -> new RuntimeException("There is no user with the email: " + securityUserDetails.getUsername()));
        if (this.passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            user.setPassword(this.passwordEncoder.encode(changePasswordRequest.getNewPassword()));
            this.userRepository.save(user);
            return ResponseEntity.ok(new EventResponse(EventResponse.Event.Authentication.CHANGED_PASSWORD));
        }
        return ResponseEntity.badRequest().body(new EventResponse(EventResponse.Event.Authentication.PASSWORD_UNMATCHED));
    }
}
