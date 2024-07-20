package com.ua.ezbir.web.post;

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

    @JsonProperty("content_urls")
    private List<String> contentUrls;
}
