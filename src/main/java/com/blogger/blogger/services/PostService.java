package com.blogger.blogger.services;

import com.blogger.blogger.dto.request.PostRequestDto;
import com.blogger.blogger.dto.response.GenericResponse;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface PostService {
    GenericResponse getAllPosts(Long userId, Pageable pageable);
    GenericResponse createPost(PostRequestDto postRequestDto, Long userId);
    GenericResponse updatePost(Long postId, Long userId, PostRequestDto postRequestDto) throws IOException;
    GenericResponse deletePost(Long userId, Long postId);
    GenericResponse getPostById(Long userId, Long postId);


}
