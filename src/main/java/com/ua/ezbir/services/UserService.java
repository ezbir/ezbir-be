package com.ua.ezbir.services;

import com.ua.ezbir.domain.User;
import com.ua.ezbir.domain.VerificationToken;
import com.ua.ezbir.web.user.*;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserResponseDto createNewUser(UserRequestDto userRequestDto);
    Optional<User> findByEmail(String email);
    UserResponseDto getUserResponseDtoById(Long id);
    UserResponseDto getUserResponseDtoByEmail(String email);
    User getUser();
    List<UserResponseDto> fetchUsers(Optional<String> optionalPrefixName);
    VerificationToken verifyToken(String inputToken);
    String verifyEmail(String inputToken);
    UserResponseDto updateUser(Optional<String> description, Optional<Long> number);
    void saveUser(User user);
}
