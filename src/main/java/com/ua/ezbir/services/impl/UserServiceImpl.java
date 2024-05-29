package com.ua.ezbir.services.impl;

import com.ua.ezbir.domain.User;
import com.ua.ezbir.domain.VerificationToken;
import com.ua.ezbir.domain.exceptions.BadRequestException;
import com.ua.ezbir.domain.exceptions.UserNotFoundException;
import com.ua.ezbir.factories.UserDtoFactory;
import com.ua.ezbir.repository.UserRepository;
import com.ua.ezbir.repository.VerificationTokenRepository;
import com.ua.ezbir.services.MailService;
import com.ua.ezbir.services.UserService;
import com.ua.ezbir.web.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;
    private final UserDtoFactory userDtoFactory;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Override
    @Transactional
    public void saveUser(User user) {
        if (user != null) {
            userRepository.save(user);
        } else {
            throw new NullPointerException();
        }
    }

    @Override
    @Transactional
    public UserResponseDto createNewUser(UserRequestDto userRequestDto) {
        User user = User.builder()
                .username(userRequestDto.getUsername())
                .email(userRequestDto.getEmail())
                .password(passwordEncoder.encode(userRequestDto.getPassword()))
                .enabled(false) // by default, 'enabled' is set to false
                .build();       // when the user verifies their email, 'enabled' will be set to true

        userRepository.save(user);

        String token = UUID.randomUUID().toString(); // generate token by uuid
        verificationTokenRepository.save( // create verify token and save in db
                VerificationToken.builder()
                        .token(token)
                        .user(user)
                        .build()
        );

        mailService.sendVerificationEmail(user.getEmail(), token); // send message to user email

        return userDtoFactory.makeUserResponseDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserResponseDtoById(Long id) {
        return userDtoFactory
                .makeUserResponseDto(
                        userRepository.findById(id)
                                .orElseThrow(() -> new BadRequestException("User not found"))
                );
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserResponseDtoByEmail(String email) {
        return userDtoFactory
                .makeUserResponseDto(
                        userRepository.findByEmail(email)
                                .orElseThrow(() -> new BadRequestException("User not found"))
                );
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return userRepository
                .findByEmail(userDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(Optional<String> description, Optional<Long> number) {
        User user = getUser();

        description.ifPresent(user::setDescription);
        number.ifPresent(user::setNumber);

        saveUser(user);

        return userDtoFactory.makeUserResponseDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public VerificationToken verifyToken(String inputToken) {
        VerificationToken token = verificationTokenRepository.findByToken(inputToken);
        if (token == null || token.getExpiryDate().isBefore(Instant.now())) {
            throw new BadRequestException("Invalid or expired token");
        }
        return token;
    }

    @Override
    @Transactional
    public String verifyEmail(String inputToken) {
        User user = verifyToken(inputToken).getUser();
        user.setEnabled(true);
        saveUser(user);

        return "Email was successful verified";
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> fetchUsers(Optional<String> optionalPrefixName) {
        optionalPrefixName = optionalPrefixName.filter(prefixName -> !prefixName.trim().isEmpty());

        Stream<User> userStream = optionalPrefixName
                .map(userRepository::streamAllByUsernameStartsWithIgnoreCase)
                .orElseGet(userRepository::streamAllBy);

        return userStream
                .map(userDtoFactory::makeUserResponseDto)
                .collect(Collectors.toList());
    }
}
