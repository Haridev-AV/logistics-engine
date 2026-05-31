package com.routeoptima.logistics_engine.controller;
import com.routeoptima.logistics_engine.dto.AuthRequest;
import com.routeoptima.logistics_engine.dto.AuthResponse;
import com.routeoptima.logistics_engine.model.User;
import com.routeoptima.logistics_engine.repository.UserRepository;
import com.routeoptima.logistics_engine.security.JwtUtils;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> register(@RequestBody AuthRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        // Find user and check password
        User foundUser = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(request.getPassword(), foundUser.getPassword())) {
            return ResponseEntity.ok(new AuthResponse(jwtUtils.generateToken(foundUser.getUsername())));
        }
        throw new RuntimeException("Invalid credentials");
    }
}