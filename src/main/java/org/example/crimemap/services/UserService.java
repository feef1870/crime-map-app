package org.example.crimemap.services;

import lombok.AllArgsConstructor;
import org.example.crimemap.entities.User;
import org.example.crimemap.repositories.UserRepository;
import org.example.crimemap.security.SecurityUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService  implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);

        return user.map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found " + username));
    }

    public String registerUser(User user) {
        // Encrypt password before saving
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        user.setTrustScore(5);
        userRepository.save(user);
        return "User added successfully!";
    }
}
