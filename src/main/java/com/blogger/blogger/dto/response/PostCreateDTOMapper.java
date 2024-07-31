package com.blogger.blogger.dto.response;


import com.blogger.blogger.model.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.function.Function;
@Service
@Slf4j
public class PostCreateDTOMapper implements Function<Post, PostResponseDTO> {

    @Override
    public PostResponseDTO apply(Post post) {
        if (post == null) {
            log.error("this is the issue=========================="); // Handle the case where post is null
        }
        log.info("chech this================No Problem======================{}",post);
        return PostResponseDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .category(post.getCategory())
                .description(post.getDescription())
                .createdAt(post.getCreatedAt())
                .likes(post.getTotalLikes())
                .liked(post.getLiked())
                .media(post.getMedia())
                .build();
    }

}
