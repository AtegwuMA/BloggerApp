package com.blogger.blogger.serviceTest;

import com.blogger.blogger.dto.response.CommentDTOMapper;
import com.blogger.blogger.dto.response.CommentResponseDTO;
import com.blogger.blogger.dto.response.GenericResponse;
import com.blogger.blogger.exceptions.AppRequestException;
import com.blogger.blogger.model.Comment;
import com.blogger.blogger.model.Post;
import com.blogger.blogger.model.User;
import com.blogger.blogger.repository.CommentRepository;
import com.blogger.blogger.repository.PostRepository;
import com.blogger.blogger.services.serviceImpl.CommentServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CommentServiceTest {

    @Mock
    private PostRepository postRepository;
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentDTOMapper commentDTOMapper;

    @InjectMocks
    private CommentServiceImpl commentService;


    @Test
    public void testGetCommentById_Success() {
        Long userId = 1L;
        Long postId = 1L;
        Long commentId = 1L;

        Post post = new Post();
        post.setId(postId);

        Comment comment = Comment.builder()
                .id(commentId)
                .content("Test comment")
                .createdAt(new Date())
                .post(post)
                .build();

        CommentResponseDTO commentResponseDTO = CommentResponseDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .likes(comment.getTotalLikes())
                .liked(comment.isLiked())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();

        // Mocking behavior for getting comment by ID
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // Mocking behavior for mapping comment to CommentResponseDTO
        when(commentDTOMapper.apply(comment)).thenReturn(commentResponseDTO);

        // Call the method under test
        GenericResponse response = commentService.getCommentById(userId, postId, commentId);

        // Verify the returned GenericResponse
        assertNotNull(response);
        assertEquals("00", response.getCode());
        assertEquals("Successful", response.getMessage());
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(commentResponseDTO, response.getData());

        // Verify interactions with mocks
        verify(commentRepository).findById(commentId);
        verify(commentDTOMapper).apply(comment);
    }

    @Test
    public void testUpdateComment_Success() {
        Long userId = 1L;
        Long postId = 1L;
        Long commentId = 1L;
        String content = "Updated comment";

        User user = new User();
        user.setId(userId);

        Post post = new Post();
        post.setId(postId);

        Comment comment = Comment.builder()
                .id(commentId)
                .createdAt(new Date())
                .updatedAt(new Date())
                .content("Old comment")
                .post(post)
                .build();

        Comment updatedComment = Comment.builder()
                .id(commentId)
                .post(post)
                .content(content)
                .createdAt(comment.getCreatedAt())
                .build();

        CommentResponseDTO commentResponseDTO = CommentResponseDTO.builder()
                .id(updatedComment.getId())
                .content(updatedComment.getContent())
                .likes(updatedComment.getTotalLikes())
                .liked(updatedComment.isLiked())
                .createdAt(updatedComment.getCreatedAt())
                .updatedAt(updatedComment.getUpdatedAt())
                .build();

        when(postRepository.existsById(postId)).thenReturn(true);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(commentRepository.save(any(Comment.class))).thenReturn(updatedComment);
        when(commentDTOMapper.apply(updatedComment)).thenReturn(commentResponseDTO);

        GenericResponse response = commentService.updateComment(userId, postId, commentId, content);

        assertNotNull(response);
        assertEquals("00", response.getCode());
        assertEquals("Updated successfully", response.getMessage());
        assertEquals(HttpStatus.ACCEPTED, response.getHttpStatus());
        assertEquals(commentResponseDTO, response.getData());
    }

    @Test(expected = AppRequestException.class)
    public void testUpdateComment_NullUserId() {
        Long userId = null;
        Long postId = 1L;
        Long commentId = 1L;
        String content = "Updated comment";

        commentService.updateComment(userId, postId, commentId, content);
    }

    @Test(expected = AppRequestException.class)
    public void testUpdateComment_EmptyContent() {
        Long userId = 1L;
        Long postId = 1L;
        Long commentId = 1L;
        String content = "";

        commentService.updateComment(userId, postId, commentId, content);
    }

    @Test(expected = AppRequestException.class)
    public void testUpdateComment_PostNotFound() {
        Long userId = 1L;
        Long postId = 1L;
        Long commentId = 1L;
        String content = "Updated comment";

        when(postRepository.existsById(postId)).thenReturn(false);

        commentService.updateComment(userId, postId, commentId, content);
    }

    @Test(expected = AppRequestException.class)
    public void testUpdateComment_CommentNotFound() {
        Long userId = 1L;
        Long postId = 1L;
        Long commentId = 1L;
        String content = "Updated comment";

        when(postRepository.existsById(postId)).thenReturn(true);
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        commentService.updateComment(userId, postId, commentId, content);
    }

    @Test
    public void testDeleteComment_Success() {
        Long userId = 1L;
        Long postId = 1L;
        Long commentId = 1L;
        Boolean liked = true;
        Long totalLikes = 3L;

        Comment comment = Comment.builder()
                .id(commentId)
                .createdAt(new Date())
                .content("Test comment")
                .liked(liked)
                .totalLikes(totalLikes)
                .build();

        when(commentRepository.findByIdAndPostId(commentId, postId)).thenReturn(Optional.of(comment));

        GenericResponse response = commentService.deleteComment(userId, postId, commentId);

        assertNotNull(response);
        assertEquals("00", response.getCode());
        assertEquals("Successfully deleted", response.getMessage());
        assertEquals(HttpStatus.OK, response.getHttpStatus());

        verify(commentRepository, times(1)).delete(comment);
    }

    @Test(expected = AppRequestException.class)
    public void testDeleteComment_NullParams() {
        Long userId = null;
        Long postId = 1L;
        Long commentId = 1L;

        commentService.deleteComment(userId, postId, commentId);
    }

    @Test(expected = AppRequestException.class)
    public void testDeleteComment_CommentNotFound() {
        Long userId = 1L;
        Long postId = 1L;
        Long commentId = 1L;

        when(commentRepository.findByIdAndPostId(commentId, postId)).thenReturn(Optional.empty());

        commentService.deleteComment(userId, postId, commentId);
    }


    @Test(expected = AppRequestException.class)
    public void testGetCommentById_NullParams() {
        commentService.getCommentById(null, 1L, 1L);
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetCommentById_CommentNotFound() {
        Long userId = 1L;
        Long postId = 1L;
        Long commentId = 1L;

        // Mocking behavior for comment not found
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        commentService.getCommentById(userId, postId, commentId);
    }
}
