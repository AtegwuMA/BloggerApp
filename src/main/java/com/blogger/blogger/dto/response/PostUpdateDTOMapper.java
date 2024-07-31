package com.blogger.blogger.dto.response;


import com.blogger.blogger.model.Post;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class PostUpdateDTOMapper implements Function<Post, PostResponseDTO> {

    @Override
    public PostResponseDTO apply(Post post){
        return PostResponseDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .category(post.getCategory())
                .description(post.getDescription())
                .updatedAt(post.getUpdatedAt())
                .likes(post.getTotalLikes())
                .liked(post.getLiked())
                .media(post.getMedia())
                .build();
    }

}
