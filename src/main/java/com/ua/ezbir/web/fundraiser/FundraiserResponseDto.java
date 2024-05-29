package com.ua.ezbir.web.fundraiser;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FundraiserResponseDto {
    private Long id;

    private int amount;

    @JsonProperty("jar_link")
    private String jarLink;

    private String description;

    @JsonProperty("is_closed")
    private boolean isClosed;

    @JsonProperty("created_at")
    private Instant createdAt;

    private Set<Category> categories;

    private List<PostResponseDto> posts;

    private String username;

    @JsonProperty("user_id")
    private Long userId;
}
