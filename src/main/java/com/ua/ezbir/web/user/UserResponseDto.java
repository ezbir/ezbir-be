package com.ua.ezbir.web.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ua.ezbir.web.fundraiser.FundraiserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long id;

    private String email;

    private String username;

    private String description;

    private long number;

    @JsonProperty("photo_url")
    private String photoUrl;

    @JsonProperty("created_at")
    private Instant createdAt;

    private List<FundraiserResponseDto> fundraisers;
}
