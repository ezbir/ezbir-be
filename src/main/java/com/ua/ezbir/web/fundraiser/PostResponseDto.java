package com.ua.ezbir.web.fundraiser;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class PostResponseDto {
    private Long id;

    private String text;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("photos_url")
    private List<String> photosUrl;
}
