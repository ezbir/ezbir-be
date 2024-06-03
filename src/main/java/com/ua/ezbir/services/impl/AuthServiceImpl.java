package com.ua.ezbir.services.impl;

import com.ua.ezbir.config.CustomUserDetailsService;
import com.ua.ezbir.config.JwtTokenUtils;
import com.ua.ezbir.domain.exceptions.BadRequestException;
import com.ua.ezbir.domain.exceptions.UnauthorizedException;
import com.ua.ezbir.services.AuthService;
import com.ua.ezbir.services.UserService;
import com.ua.ezbir.web.user.JwtRequestDto;
import com.ua.ezbir.web.user.JwtResponseDto;
import com.ua.ezbir.web.user.UserRequestDto;
import com.ua.ezbir.web.user.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final CustomUserDetailsService userDetailsService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    public JwtResponseDto createAuthToken(JwtRequestDto request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),
                    request.getPassword()));
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Email or password is wrong");
        }

        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
            String token = jwtTokenUtils.createToken(userDetails);

            return new JwtResponseDto(userService.getUserResponseDtoByEmail(request.getEmail()), token);
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public UserResponseDto createNewUser(UserRequestDto userRequestDto) {
        if (!userRequestDto.getPassword().equals(userRequestDto.getRepeatPassword())) {
            throw new BadRequestException("Passwords don't match");
        }

        if (userService.findByEmail(userRequestDto.getEmail()).isPresent()) {
            throw new BadRequestException("User already exists");
        }

        return userService.createNewUser(userRequestDto);
    }
}
