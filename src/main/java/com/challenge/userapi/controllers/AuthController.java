package com.challenge.userapi.controllers;

import com.challenge.userapi.dtos.AuthResponseDto;
import com.challenge.userapi.dtos.LoginRequestDto;
import com.challenge.userapi.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping
    public ResponseEntity<AuthResponseDto> auth(@RequestBody LoginRequestDto loginDto) throws IOException {
        AuthResponseDto response = authService.login(loginDto);
        return ResponseEntity.ok(response);
    }
}
