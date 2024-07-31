package com.blogger.blogger.controllers;

import com.blogger.blogger.dto.response.GenericResponse;
import com.blogger.blogger.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<GenericResponse> getAllCommentsByPostId(@PathVariable Long postId,
                                                                  @RequestParam Long userId, Pageable pageable) {
        GenericResponse response = commentService.getAllCommentsByPostId(postId, userId, pageable);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<GenericResponse> createComment(@RequestParam Long userId,
                                                         @PathVariable Long postId,
                                                         @RequestBody Map<String, String> requestBody) {
        String content = requestBody.get("content");
        GenericResponse response = commentService.createComment(userId, postId, content);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @PutMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<GenericResponse> updateComment(@RequestParam Long userId,
                                                         @PathVariable Long postId,
                                                         @PathVariable Long commentId,
                                                         @RequestBody Map<String, String> requestBody) {
        String content = requestBody.get("content");
        GenericResponse response = commentService.updateComment(userId, postId, commentId, content);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<GenericResponse> deleteComment(@RequestParam Long userId,
                                                         @PathVariable Long postId,
                                                         @PathVariable Long commentId) {
        GenericResponse response = commentService.deleteComment(userId, postId, commentId);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @GetMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<GenericResponse> getCommentById(@RequestParam Long userId,
                                                          @PathVariable Long postId,
                                                          @PathVariable Long commentId) {
        GenericResponse response = commentService.getCommentById(userId, postId, commentId);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }
}
