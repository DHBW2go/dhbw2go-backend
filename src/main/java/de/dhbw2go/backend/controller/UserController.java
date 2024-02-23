package de.dhbw2go.backend.controller;


import de.dhbw2go.backend.entities.Image;
import de.dhbw2go.backend.entities.User;
import de.dhbw2go.backend.payload.response.basic.MessageResponse;
import de.dhbw2go.backend.repositories.ImageRepository;
import de.dhbw2go.backend.repositories.UserRepository;
import de.dhbw2go.backend.security.SecurityUserDetails;
import de.dhbw2go.backend.security.jwt.JWTHelper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@Tag(name = "User")
public class UserController {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTHelper jwtHelper;

    @GetMapping(path = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> info() {
        final SecurityUserDetails securityUserDetails = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(this.userRepository.findByEmail(securityUserDetails.getUsername()).get());
    }

    @PutMapping(path = "/change-picture", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> changePicture(@RequestParam("file") final MultipartFile multipartFile) {
        final SecurityUserDetails securityUserDetails = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final User user = this.userRepository.findByEmail(securityUserDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("There is no user with the email: " + securityUserDetails.getUsername()));
        final Image image = new Image();
        image.setName(multipartFile.getOriginalFilename());
        image.setType(multipartFile.getContentType());
        try {
            image.setData(multipartFile.getBytes());
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
        this.imageRepository.save(image);
        user.setImage(image);
        this.userRepository.save(user);
        final MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("You have been successfully changed your profile picture!");
        return ResponseEntity.ok(messageResponse);
    }

    @DeleteMapping(path = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> delete() {
        final SecurityUserDetails securityUserDetails = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //this.userRepository.deleteUserByEmail(securityUserDetails.getUsername());
        final ResponseCookie responseCookie = this.jwtHelper.getCleanJWTCookie();
        final MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("You have been successfully deleted your user data!");
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(messageResponse);
    }
}
