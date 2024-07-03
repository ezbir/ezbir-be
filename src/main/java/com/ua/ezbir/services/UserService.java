package com.ua.ezbir.services;

import com.ua.ezbir.domain.User;
import com.ua.ezbir.web.user.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserResponseDto createNewUser(UserRequestDto userRequestDto);
    Optional<User> findByEmail(String email);
    UserResponseDto getUserResponseDtoById(Long id);
    UserResponseDto getUserResponseDtoByEmail(String email);
    User getUser();
    List<UserResponseDto> fetchUsers(Optional<String> optionalPrefixName);
    UserResponseDto updateUser(Optional<String> description, Optional<Long> number);
    void saveUser(User user);
    String uploadPicture(MultipartFile multipartFile);
}
