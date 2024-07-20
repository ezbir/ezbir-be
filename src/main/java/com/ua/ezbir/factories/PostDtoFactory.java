package com.ua.ezbir.factories;

import com.ua.ezbir.domain.Post;
import com.ua.ezbir.web.post.PostResponseDto;
import org.springframework.stereotype.Component;

@Component
public class PostDtoFactory {
    public PostResponseDto makePostResponseDto(Post post) {
        return PostResponseDto.builder()
                .id(post.getId())
                .text(post.getText())
                .createdAt(post.getCreatedAt())
                .contentUrls(post.getContentUrls())
                .build();
    }
}
