package de.dhbw2go.backend.controller;

import de.dhbw2go.backend.entities.Role;
import de.dhbw2go.backend.entities.Role.RoleType;
import de.dhbw2go.backend.entities.User;
import de.dhbw2go.backend.payload.request.authentication.ChangePasswordRequest;
import de.dhbw2go.backend.payload.request.authentication.LoginRequest;
import de.dhbw2go.backend.payload.request.authentication.RegisterRequest;
import de.dhbw2go.backend.payload.response.basic.MessageResponse;
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
    public ResponseEntity<MessageResponse> check(@PathVariable("email") final String email) {
        if (!this.userRepository.existsByEmail(email)) {
            final MessageResponse messageResponse = new MessageResponse();
            messageResponse.setMessage("The selected email address is not taken!");
            return ResponseEntity.ok(messageResponse);
        }
        final MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("The selected email address is already taken!");
        return ResponseEntity.badRequest().body(messageResponse);
    }

    @PutMapping(path = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody final RegisterRequest registerRequest) {
        if (!this.userRepository.existsByUsername(registerRequest.getUsername())) {
            if (!this.userRepository.existsByEmail(registerRequest.getEmail())) {
                final User user = new User();
                user.setUsername(registerRequest.getUsername());
                user.setEmail(registerRequest.getEmail());
                user.setPassword(this.passwordEncoder.encode(registerRequest.getPassword()));
                user.setLevel(1);
                user.setExperience(0);
                user.addRole(this.roleRepository.findByType(RoleType.USER).orElseGet(() -> {
                    final Role role = new Role();
                    role.setType(RoleType.USER);
                    return this.roleRepository.save(role);
                }));
                this.userRepository.save(user);
                final MessageResponse messageResponse = new MessageResponse();
                messageResponse.setMessage("You have been successfully signed up!");
                return ResponseEntity.ok(messageResponse);
            }
            final MessageResponse messageResponse = new MessageResponse();
            messageResponse.setMessage("The selected email address is already in use!");
            return ResponseEntity.badRequest().body(messageResponse);
        }
        final MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("The selected username is already taken!");
        return ResponseEntity.badRequest().body(messageResponse);
    }

    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> login(@Valid @RequestBody final LoginRequest loginRequest) {
        final Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final SecurityUserDetails securityUserDetails = (SecurityUserDetails) authentication.getPrincipal();
        final ResponseCookie responseCookie = this.jwtHelper.generateJWTCookie(securityUserDetails);
        final MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("You have been successfully signed in!");
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(messageResponse);
    }

    @PostMapping(path = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> logout() {
        final ResponseCookie responseCookie = this.jwtHelper.getCleanJWTCookie();
        final MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("You have been successfully signed out!");
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(messageResponse);
    }

    @PostMapping(path = "/change-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> changePassword(@Valid @RequestBody final ChangePasswordRequest changePasswordRequest) {
        final SecurityUserDetails securityUserDetails = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final User user = this.userRepository.findByEmail(securityUserDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("There is no user with the email: " + securityUserDetails.getUsername()));
        if (this.passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            user.setPassword(this.passwordEncoder.encode(changePasswordRequest.getNewPassword()));
            this.userRepository.save(user);
            final MessageResponse messageResponse = new MessageResponse();
            messageResponse.setMessage("You have been successfully changed your password!");
            return ResponseEntity.ok(messageResponse);
        }
        final MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("The current password doesn't matched!");
        return ResponseEntity.badRequest().body(messageResponse);
    }
}
