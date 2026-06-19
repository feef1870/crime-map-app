package org.example.crimemap.controllers;

import lombok.AllArgsConstructor;
import org.example.crimemap.dto.AuthRequest;
import org.example.crimemap.dto.AuthResponse;
import org.example.crimemap.dto.RegisterRequest;
import org.example.crimemap.services.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {

    private AuthService authService;

    @PostMapping("/auth/register")
    public AuthResponse register(@RequestBody RegisterRequest user) {
        return authService.registerUser(user);
    }

    @PostMapping("/auth/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        return authService.loginUser(request);
    }

    @GetMapping("/incidents")
    public String incidents() {
        return "Here will be our incidents!";
    }

}
