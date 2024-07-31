package com.blogger.blogger.controllers;

import com.blogger.blogger.dto.request.PostRequestDto;
import com.blogger.blogger.dto.response.GenericResponse;
import com.blogger.blogger.services.LikeService;
import com.blogger.blogger.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/{userId}")
public class PostController {

    private final PostService postService;

    private final LikeService likeService;

    @GetMapping("/posts")
    public ResponseEntity<GenericResponse> getAllPosts(@PathVariable Long userId, Pageable pageable) {
        GenericResponse response = postService.getAllPosts(userId, pageable);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @PostMapping("/designs")
    public ResponseEntity<GenericResponse> createDesigns(@PathVariable(value = "userId") Long userId,
                                                @Valid @RequestBody PostRequestDto postRequestDto) {
        GenericResponse response = postService.createPost(postRequestDto, userId);

    return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping("/posts/{postId}")
    public ResponseEntity<GenericResponse> updateDesigns(@PathVariable(value = "userId") Long userId,
                                                         @PathVariable(value = "postId") Long postId,
                                                         @Valid @RequestBody PostRequestDto postRequestDto
                                                         ) throws IOException {
        GenericResponse response = postService.updatePost(userId, postId, postRequestDto);

    return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<GenericResponse> deletePost(@PathVariable Long userId, @PathVariable Long postId) {
        GenericResponse response = postService.deletePost(userId, postId);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<GenericResponse> getPost(@PathVariable Long userId, @PathVariable Long postId) {
        GenericResponse response = postService.getPostById(userId, postId);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }


}
