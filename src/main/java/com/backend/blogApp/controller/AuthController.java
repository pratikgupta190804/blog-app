package com.backend.blogApp.controller;

import com.backend.blogApp.entity.CustomUserDetails;
import com.backend.blogApp.entity.Role;
import com.backend.blogApp.entity.RoleEnum;
import com.backend.blogApp.entity.User;
import com.backend.blogApp.dto.AuthRequest;
import com.backend.blogApp.dto.AuthResponse;
import com.backend.blogApp.dto.RegisterRequest;
import com.backend.blogApp.repository.RoleRepo;
import com.backend.blogApp.repository.UserRepo;
import com.backend.blogApp.service.JwtService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Public APIs", description = "Register, Login for users")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ------------------ LOGIN ------------------
    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = userRepo.findByEmail(request.getUsername())
                .map(user -> new CustomUserDetails(user))
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(userDetails);
        return new AuthResponse(token);
    }

    // ------------------ REGISTER ------------------
    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        Optional<User> existingUser = userRepo.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            return "Email already in use";
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Set default role (e.g., ROLE_USER)
        Role role = roleRepo.findByRole(RoleEnum.USER)
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        user.setRole(role);

        userRepo.save(user);
        return "User registered successfully";
    }
}
