package com.blogger.blogger.dto.response;

import com.blogger.blogger.model.Comment;
import org.springframework.stereotype.Service;

import java.util.function.Function;
@Service
public class CommentDTOMapper implements Function<Comment, CommentResponseDTO> {
    @Override
    public CommentResponseDTO apply(Comment comment) {
        return CommentResponseDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .likes(comment.getTotalLikes())
                .liked(comment.getLiked())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
        }

    }
