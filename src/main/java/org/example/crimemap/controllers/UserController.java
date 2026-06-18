package org.example.crimemap.controllers;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.crimemap.entities.User;
import org.example.crimemap.services.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {

    private AuthenticationManager authenticationManager;
    private UserService userService;

    @PostMapping("/auth/register")
    public String register(@RequestBody User user) {
        return userService.registerUser(user);
    }

}
