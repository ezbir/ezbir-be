package com.ua.ezbir.services.impl;

import com.ua.ezbir.domain.User;
import com.ua.ezbir.domain.exceptions.BadRequestException;
import com.ua.ezbir.domain.exceptions.DatabaseException;
import com.ua.ezbir.domain.exceptions.UserNotFoundException;
import com.ua.ezbir.factories.UserDtoFactory;
import com.ua.ezbir.repository.UserRepository;
import com.ua.ezbir.services.UserService;
import com.ua.ezbir.web.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserDtoFactory userDtoFactory;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;

    @Override
    @Transactional
    public User saveUser(User user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new DatabaseException();
        }
    }

    @Override
    @Transactional
    public UserResponseDto createNewUser(UserRequestDto userRequestDto) {
        User user = User.builder()
                .username(userRequestDto.getUsername())
                .email(userRequestDto.getEmail())
                .password(passwordEncoder.encode(userRequestDto.getPassword()))
                .build();

        userRepository.save(user);

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
        String userEmail = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository
                .findByEmail(userEmail)
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
    public List<UserResponseDto> fetchUsers(Optional<String> optionalPrefixName) {
        optionalPrefixName = optionalPrefixName.filter(prefixName -> !prefixName.trim().isEmpty());

        Stream<User> userStream = optionalPrefixName
                .map(userRepository::streamAllByUsernameStartsWithIgnoreCase)
                .orElseGet(userRepository::streamAllBy);

        return userStream
                .map(userDtoFactory::makeUserResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public String uploadPicture(MultipartFile picture) {
        if (picture.isEmpty()) {
            throw new BadRequestException("Picture is empty");
        }

        String contentType = picture.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BadRequestException("Not supported content type");
        }

        User user = getUser();

        String pictureName = String.format(
                "users/%d/picture.%s",
                user.getId(),
                contentType.substring(contentType.lastIndexOf("/") + 1)
        );

        try {
            s3Service.uploadFile(pictureName, picture);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        user.setPhotoUrl(s3Service.getUrl() + "/" + pictureName);
        saveUser(user);

        return "Profile picture was uploaded";
    }
}
