package com.routeoptima.logistics_engine.controller;
import com.routeoptima.logistics_engine.model.User;
import com.routeoptima.logistics_engine.repository.UserRepository;
import com.routeoptima.logistics_engine.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @PostMapping("/signup")
    public String register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // HASH IT!
        userRepository.save(user);
        return "User registered successfully!";
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        // Find user and check password
        User foundUser = userRepository.findByUsername(user.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            return jwtUtils.generateToken(foundUser.getUsername()); // Give them the badge!
        }
        throw new RuntimeException("Invalid credentials");
    }
}