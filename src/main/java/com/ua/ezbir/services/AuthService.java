package com.ua.ezbir.services;

import com.ua.ezbir.web.user.JwtRequestDto;
import com.ua.ezbir.web.user.JwtResponseDto;
import com.ua.ezbir.web.user.UserRequestDto;
import com.ua.ezbir.web.user.UserResponseDto;

public interface AuthService {
    JwtResponseDto createAuthToken(JwtRequestDto jwtRequestDto);
    String registerNewUser(UserRequestDto userRequestDto);
    UserResponseDto verifyEmail(String inputToken);
}
