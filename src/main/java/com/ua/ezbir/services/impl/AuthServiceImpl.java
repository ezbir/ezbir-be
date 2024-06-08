package com.ua.ezbir.services.impl;

import com.ua.ezbir.config.CustomUserDetailsService;
import com.ua.ezbir.config.JwtTokenUtils;
import com.ua.ezbir.domain.VerificationToken;
import com.ua.ezbir.domain.exceptions.BadRequestException;
import com.ua.ezbir.domain.exceptions.UnauthorizedException;
import com.ua.ezbir.repository.VerificationTokenRepository;
import com.ua.ezbir.services.AuthService;
import com.ua.ezbir.services.MailService;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final VerificationTokenRepository verificationTokenRepository;
    private final CustomUserDetailsService userDetailsService;
    private final MailService mailService;
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
    @Transactional
    public String registerNewUser(UserRequestDto userRequestDto) {
        if (!userRequestDto.getPassword().equals(userRequestDto.getRepeatPassword())) {
            throw new BadRequestException("Passwords don't match");
        }

        if (userService.findByEmail(userRequestDto.getEmail()).isPresent()) {
            throw new BadRequestException("User already exists");
        }

        String token = UUID.randomUUID().toString(); // generate token by uuid
        verificationTokenRepository.save( // create verify token and save in db
                VerificationToken.builder()
                        .token(token)
                        .email(userRequestDto.getEmail())
                        .username(userRequestDto.getUsername())
                        .password(userRequestDto.getPassword())
                        .build()
        );

        mailService.sendVerificationEmail(userRequestDto.getEmail(), token); // send message to user email

        return "The verification token was sent successfully";
    }

    @Override
    @Transactional
    public UserResponseDto verifyEmail(String inputToken) {
        VerificationToken token = verificationTokenRepository.findByToken(inputToken);
        if (token == null || token.getExpiryDate().isBefore(Instant.now())) {
            throw new BadRequestException("Invalid or expired token");
        }

        UserRequestDto userRequestDto = UserRequestDto.builder()
                .email(token.getEmail())
                .username(token.getUsername())
                .password(token.getPassword())
                .build();

        return userService.createNewUser(userRequestDto);
    }

}
