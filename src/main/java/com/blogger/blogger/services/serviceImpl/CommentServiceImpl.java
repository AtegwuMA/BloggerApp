package com.blogger.blogger.services.serviceImpl;

import com.blogger.blogger.dto.response.CommentDTOMapper;
import com.blogger.blogger.dto.response.CommentResponseDTO;
import com.blogger.blogger.dto.response.GenericResponse;
import com.blogger.blogger.exceptions.AppRequestException;
import com.blogger.blogger.model.Comment;
import com.blogger.blogger.repository.CommentRepository;
import com.blogger.blogger.repository.PostRepository;
import com.blogger.blogger.services.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentDTOMapper commentDTOMapper;
    private final PostRepository postRepository;


    public CommentServiceImpl(CommentRepository commentRepository, CommentDTOMapper commentDTOMapper, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.commentDTOMapper = commentDTOMapper;
        this.postRepository = postRepository;
    }

    public GenericResponse getAllCommentsByPostId(Long postId, Long userId, Pageable pageable) {
        if (postId == null || userId == null) {
            throw new AppRequestException("UserId, PostId cannot be null");
        }

        Page<Comment> comments = commentRepository.findByPostId(postId, pageable);

        List<CommentResponseDTO> commentResponseDTOs = comments
                .stream()
                .map(new CommentDTOMapper()) // Assuming CommentDTOMapper is accessible
                .collect(Collectors.toList());

        Page<CommentResponseDTO> commentResponseDTOPage = new PageImpl<>(commentResponseDTOs,
                                        comments.getPageable(), comments.getTotalElements());

        return new GenericResponse("00", "Successful", HttpStatus.OK, commentResponseDTOPage);
    }

    public GenericResponse createComment(Long userId, Long postId, String content) {
        if (postId == null || userId == null) {
            throw new AppRequestException("UserId and PostId cannot be null");
        }
        if (content == null || content.isEmpty()) {
            throw new AppRequestException("Content cannot be empty");
        }

        return postRepository.findById(postId).map(post -> {
            Comment comment = new Comment();
            comment.setPost(post);
            comment.setContent(content);
            Comment comment1 = commentRepository.save(comment);
            CommentResponseDTO commentResponseDTO = commentDTOMapper.apply(comment1);
            return new GenericResponse ("00", "Successfully created", HttpStatus.CREATED, commentResponseDTO);
        }).orElseThrow(() -> new AppRequestException("PostId " + postId + " not found"));
    }

    public GenericResponse updateComment(Long  userId, Long postId, Long commentId, String content) {
        if (postId == null || userId == null)  {
            throw new AppRequestException("UserId and PostId cannot be null");
        }
        if (content == null ||content.isEmpty()) {
            throw new AppRequestException("Comment text cannot be empty");
        }
        if (!postRepository.existsById(postId)) {
            throw new AppRequestException("PostId " + postId + " not found");
        }

        return commentRepository.findById(commentId).map(comment -> {
            comment.setContent(content);
            Comment comment1 = commentRepository.save(comment);
            CommentResponseDTO commentResponseDTO = commentDTOMapper.apply(comment1);
            return new GenericResponse("00", "Updated successfully",
                                        HttpStatus.ACCEPTED, commentResponseDTO);
        }).orElseThrow(() -> new AppRequestException("CommentId " + commentId + " not found"));
    }

    public GenericResponse deleteComment(Long userId, Long postId, Long commentId) {
        if (userId == null || postId == null || commentId == null) {
            throw new AppRequestException("UserId, PostId and CommentId cannot be null");
        }

        return commentRepository.findByIdAndPostId(commentId, postId).map(comment -> {
            commentRepository.delete(comment);
            return new GenericResponse("00", "Successfully deleted", HttpStatus.OK);
        }).orElseThrow(() -> new AppRequestException("Comment not found with id " + commentId + " and postId " + postId));
    }

    public GenericResponse getCommentById(Long userId, Long postId, Long commentId) {
        if (userId == null || postId == null || commentId == null) {
               throw new AppRequestException("UserId, PostId and CommentId cannot be null");
        }
        Comment comment = commentRepository.findById(commentId).get();
        CommentResponseDTO commentResponseDTO = commentDTOMapper.apply(comment);
        return new GenericResponse("00", "Successful", HttpStatus.OK, commentResponseDTO);
    }

}


//    public GenericResponse getAllCommentsByPostId(Long postId, Long userId, Pageable pageable) {
//        if (postId == null || userId == null) {
//            throw new AppException("UserId, PostId cannot be null");
//        }
//        Page<Comment> comments = commentRepository.findByPostId(postId, pageable);
//        return new GenericResponse("00", "Successful", HttpStatus.OK, comments);
//    }