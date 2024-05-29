package com.ua.ezbir.web.user;

import lombok.*;

@Getter
@Setter
public class JwtResponseDto extends UserResponseDto {
    private String token;

    public JwtResponseDto(UserResponseDto user, String token) {
        super(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getDescription(),
                user.getNumber(),
                user.getPhotoUrl(),
                user.getCreatedAt(),
                user.getFundraisers()
        );
        this.token = token;
    }
}
