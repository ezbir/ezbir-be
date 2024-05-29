package com.ua.ezbir.web.controllers;

import com.ua.ezbir.services.AuthService;
import com.ua.ezbir.web.user.JwtRequestDto;
import com.ua.ezbir.web.user.JwtResponseDto;
import com.ua.ezbir.web.user.UserRequestDto;
import com.ua.ezbir.web.user.UserResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public JwtResponseDto login(@RequestBody @Valid JwtRequestDto jwtRequestDto) {
        return authService.createAuthToken(jwtRequestDto);
    }

    @PostMapping("/register")
    public UserResponseDto register(@RequestBody @Valid UserRequestDto userRequestDto) {
        return authService.createNewUser(userRequestDto);
    }
}
