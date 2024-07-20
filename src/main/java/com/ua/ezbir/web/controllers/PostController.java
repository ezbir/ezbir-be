package com.ua.ezbir.web.controllers;

import com.ua.ezbir.services.PostService;
import com.ua.ezbir.web.post.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;

    @PostMapping("/add")
    public PostResponseDto addPost(
            @RequestParam(name = "fundraiser_id") Long fundraiserId,
            @RequestParam("text") String text,
            @RequestPart("files") List<MultipartFile> files
    ) {
        return postService.addPost(fundraiserId, text, files);
    }

    @DeleteMapping("/{id}")
    public String deletePost(
            @PathVariable("id") Long postId,
            @RequestParam(name = "fundraiser_id") Long fundraiserId
    ) {
        return postService.deletePost(postId, fundraiserId);
    }

}
