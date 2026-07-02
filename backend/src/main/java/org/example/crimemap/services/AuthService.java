package org.example.crimemap.services;

//Created this class for the same reason as the AppConfig


import lombok.AllArgsConstructor;
import org.example.crimemap.dto.AuthRequest;
import org.example.crimemap.dto.AuthResponse;
import org.example.crimemap.dto.RegisterRequest;
import org.example.crimemap.entities.User;
import org.example.crimemap.exception.UserAlreadyExistsException;
import org.example.crimemap.security.SecurityUser;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public AuthResponse registerUser(RegisterRequest request) {
        if (userService.findByEmail(request.email()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + request.email() + " already exists", "CONFLICT", HttpStatus.CONFLICT);
        }

        // Encrypt password before saving
        String passwordHash = passwordEncoder.encode(request.password());
        String email = request.email();

        User savedUser = userService.saveUser(email, passwordHash);

        String token = jwtService.generateToken(request.email());
        return new AuthResponse(token, savedUser.getId(), request.email());
    }

    public AuthResponse loginUser(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        User user = securityUser.getUser();

        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(
                token,
                user.getId(),
                user.getEmail()
        );
    }
}
