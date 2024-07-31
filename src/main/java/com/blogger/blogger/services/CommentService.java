package com.blogger.blogger.services;



import com.blogger.blogger.dto.response.GenericResponse;
import com.blogger.blogger.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface CommentService {
    GenericResponse getAllCommentsByPostId(Long postId, Long userId, Pageable pageable);
    GenericResponse createComment(Long userId, Long postId, String content);
    GenericResponse updateComment(Long  userId, Long postId, Long commentId, String content);
    GenericResponse deleteComment(Long userId, Long postId, Long commentId);
    GenericResponse getCommentById(Long userId, Long postId, Long commentId);

}
