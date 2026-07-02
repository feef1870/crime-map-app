package org.example.crimemap.controllers;

import lombok.AllArgsConstructor;
import org.example.crimemap.dto.AuthRequest;
import org.example.crimemap.dto.AuthResponse;
import org.example.crimemap.dto.RegisterRequest;
import org.example.crimemap.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {

    private AuthService authService;

    @PostMapping("/auth/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest user) {
        return ResponseEntity.ok(authService.registerUser(user));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.loginUser(request));
    }
}
