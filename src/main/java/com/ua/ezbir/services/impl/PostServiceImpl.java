package com.ua.ezbir.services.impl;

import com.ua.ezbir.domain.Fundraiser;
import com.ua.ezbir.domain.Post;
import com.ua.ezbir.domain.User;
import com.ua.ezbir.domain.exceptions.BadRequestException;
import com.ua.ezbir.domain.exceptions.DatabaseException;
import com.ua.ezbir.factories.PostDtoFactory;
import com.ua.ezbir.repository.PostRepository;
import com.ua.ezbir.services.FundraiserService;
import com.ua.ezbir.services.PostService;
import com.ua.ezbir.services.UserService;
import com.ua.ezbir.web.post.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostDtoFactory postDtoFactory;
    private final FundraiserService fundraiserService;
    private final UserService userService;
    private final S3Service s3Service;

    @Override
    @Transactional
    public Post savePost(Post post) {
        try {
            return postRepository.save(post);
        } catch (Exception e) {
            throw new DatabaseException();
        }
    }

    @Override
    @Transactional
    public PostResponseDto addPost(Long fundraiserId, String text, List<MultipartFile> files) {

        files.stream()
                .filter(file -> file.isEmpty() || !isValidContentType(file.getContentType()))
                .findAny()
                .ifPresent(file -> {
                    throw new BadRequestException("File is empty or wrong content type: " + file.getOriginalFilename());
                });

        User user = userService.getUser();

        Fundraiser fundraiser = fundraiserService.findFundraiserById(fundraiserId);
        fundraiserService.checkFundraiserAccess(fundraiser, user);

        Post post = savePost(new Post());
        List<String> contentUrls = post.getContentUrls();

        for (int i = 0; i < files.size(); i++) {
            String contentType = files.get(i).getContentType();
            String fileName = String.format(
                    "users/%d/fundraiser-%d/post-%d/%d.%s",
                    user.getId(),
                    fundraiserId,
                    post.getId(),
                    i,
                    contentType.substring(contentType.lastIndexOf("/") + 1)
            );

            try {
                s3Service.uploadFile(fileName, files.get(i));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            contentUrls.add(fileName);
        }

        post.setText(text);
        post.setFundraiser(fundraiser);
        post.setContentUrls(contentUrls);

        return postDtoFactory.makePostResponseDto(savePost(post));
    }

    private boolean isValidContentType(String contentType) {
        return contentType != null && (
                contentType.startsWith("image/") ||
                        contentType.startsWith("video/")
        );
    }

    @Override
    public String deletePost(Long postId, Long fundraiserId) {
        User user = userService.getUser();

        Fundraiser fundraiser = fundraiserService.findFundraiserById(fundraiserId);
        fundraiserService.checkFundraiserAccess(fundraiser, user);

        Post post = fundraiser.getPosts()
                .stream()
                .filter(p -> p.getId().equals(postId))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("Post not found"));

        // if post has content -> delete files in s3
        post.getContentUrls().stream()
                .findFirst()
                .map(path -> path.substring(0, path.lastIndexOf("/")))
                .ifPresent(s3Service::deleteFolder);

        try {
            postRepository.delete(post);
        } catch (Exception e) {
            throw new DatabaseException();
        }

        fundraiser.getPosts().remove(post);
        fundraiserService.saveFundraiser(fundraiser);

        return "Post was successful deleted";
    }
}
