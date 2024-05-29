package com.ua.ezbir.factories;

import com.ua.ezbir.domain.User;
import com.ua.ezbir.web.user.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserDtoFactory {
    private final FundraiserDtoFactory fundraiserDtoFactory;

    public UserResponseDto makeUserResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .description(user.getDescription())
                .number(user.getNumber())
                .photoUrl(user.getPhotoUrl())
                .createdAt(user.getCreatedAt())
                .fundraisers(
                        user
                                .getFundraisers()
                                .stream()
                                .map(fundraiserDtoFactory::makeFundraiserResponseDto)
                                .collect(Collectors.toList())
                )
                .build();
    }
}
