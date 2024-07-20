package com.ua.ezbir.services;

import com.ua.ezbir.domain.Post;
import com.ua.ezbir.web.post.PostResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    Post savePost(Post post);
    PostResponseDto addPost(Long fundraiserId, String text, List<MultipartFile> files);
    String deletePost(Long postId, Long fundraiserId);
}
