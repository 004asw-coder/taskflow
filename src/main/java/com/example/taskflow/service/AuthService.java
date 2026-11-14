package com.example.taskflow.service;

import com.example.taskflow.dto.AuthResponse;
import com.example.taskflow.dto.LoginRequest;
import com.example.taskflow.dto.RegisterRequest;
import com.example.taskflow.entity.Role;
import com.example.taskflow.entity.User;
import com.example.taskflow.repository.UserRepository;
import com.example.taskflow.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtService jwt;
    private final AuthenticationManager authManager;

    public AuthResponse register(RegisterRequest req) {
        if (userRepo.existsByUsername(req.username())) {
            throw new IllegalArgumentException("Username already taken");
        }
        if (userRepo.existsByEmail(req.email())) {
            throw new IllegalArgumentException("Email already registered");
        }
        User user = User.builder()
                .username(req.username())
                .email(req.email())
                .password(encoder.encode(req.password()))
                .role(Role.USER)
                .build();
        userRepo.save(user);
        return new AuthResponse(jwt.generate(user.getUsername(), user.getRole().name()),
                user.getUsername(), user.getRole().name());
    }

    public AuthResponse login(LoginRequest req) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password()));
        User user = userRepo.findByUsername(req.username()).orElseThrow();
        return new AuthResponse(jwt.generate(user.getUsername(), user.getRole().name()),
                user.getUsername(), user.getRole().name());
    }
}