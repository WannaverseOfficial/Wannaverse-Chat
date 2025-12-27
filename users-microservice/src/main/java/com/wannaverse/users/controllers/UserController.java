package com.wannaverse.users.controllers;

import com.wannaverse.users.persistence.User;
import com.wannaverse.users.services.UserService;

import jakarta.annotation.Nullable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final List<String> ACCEPTED_TYPES =
            List.of(
                    MediaType.IMAGE_PNG_VALUE,
                    MediaType.IMAGE_JPEG_VALUE,
                    MediaType.IMAGE_GIF_VALUE);
    private static final String AVATAR_DIRECTORY_PATH = "avatars";
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> createOrUpdate(@RequestBody User user, Principal principal) {
        String userId = principal.getName();

        userService
                .getUserById(userId)
                .ifPresentOrElse(
                        u -> userService.updateUser(u, user),
                        () -> userService.createUser(userId, user));

        return ResponseEntity.ok().build();
    }

    @GetMapping("/avatar")
    public ResponseEntity<?> getAvatar(String userId) throws IOException {
        if (userId.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        Path avatarPath = Path.of(AVATAR_DIRECTORY_PATH + "/" + userId);

        if (!Files.exists(avatarPath)) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<>(
                Files.readAllBytes(avatarPath), headers, HttpStatusCode.valueOf(200));
    }

    @PostMapping("/avatar")
    public ResponseEntity<String> uploadAvatar(
            @RequestBody @Nullable MultipartFile multipartFile, Principal principal)
            throws IOException {

        if (multipartFile == null) {
            return ResponseEntity.badRequest().body("No image.");
        }

        String contentType = multipartFile.getContentType();

        if (!ACCEPTED_TYPES.contains(contentType)) {
            return ResponseEntity.badRequest().body("Shit file type");
        }

        Path uploadPath = Paths.get(AVATAR_DIRECTORY_PATH);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        multipartFile.transferTo(uploadPath.resolve(principal.getName()));

        return ResponseEntity.ok("Profile picture has been uploaded.");
    }
}
