package com.moviebooking.identity_service.controller;


import com.moviebooking.identity_service.dto.AuthResponse;
import com.moviebooking.identity_service.dto.UserLoginRequest;
import com.moviebooking.identity_service.dto.UserRegisterRequest;
import com.moviebooking.identity_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public String register(@RequestBody UserRegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody UserLoginRequest request) {
        return authService.login(request);
    }
}