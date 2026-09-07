package com.challenge.userapi.controllers;

import com.challenge.userapi.dtos.BatchResponseDto;
import com.challenge.userapi.dtos.UserDto;
import com.challenge.userapi.services.UserBatchService;
import com.challenge.userapi.services.UserGeneratorService;
import com.challenge.userapi.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/users")
@AllArgsConstructor
public class UserController {

    private final UserBatchService userBatchService;
    private final UserService userService;

    @GetMapping("/generate")
    public ResponseEntity<List<UserDto>> getUsers(@RequestParam int count) {
        UserGeneratorService userGeneratorService = new UserGeneratorService();
        return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.json")
                                .contentType(MediaType.APPLICATION_JSON)
                                        .body(userGeneratorService.generateUsers(count));

    }

    @PostMapping(value = "/batch", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BatchResponseDto> uploadUsers(@RequestParam("file") MultipartFile file) throws IOException {
        BatchResponseDto response = userBatchService.processBatchUpload(file);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getMyProfile(Authentication authentication) {
        String currentUsername = authentication.getName();
        UserDto userDto = userService.getUserByUsername(currentUsername);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        UserDto user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }
}
