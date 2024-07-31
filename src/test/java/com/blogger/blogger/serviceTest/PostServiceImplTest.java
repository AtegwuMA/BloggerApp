package com.blogger.blogger.serviceTest;

import com.blogger.blogger.dto.request.PostRequestDto;
import com.blogger.blogger.dto.response.GenericResponse;
import com.blogger.blogger.dto.response.PostCreateDTOMapper;
import com.blogger.blogger.dto.response.PostResponseDTO;
import com.blogger.blogger.dto.response.PostUpdateDTOMapper;
import com.blogger.blogger.enums.DesignCategory;
import com.blogger.blogger.exceptions.AppRequestException;
import com.blogger.blogger.model.Post;
import com.blogger.blogger.model.User;
import com.blogger.blogger.repository.PostRepository;
import com.blogger.blogger.repository.UserRepository;
import com.blogger.blogger.services.serviceImpl.PostServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class PostServiceImplTest {
    @Mock
    private PostRepository postRepository;
    @Mock
    private PostCreateDTOMapper postDTOMapper;
    @Mock
    private PostUpdateDTOMapper postUpdateDTOMapper;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private PostServiceImpl postService;

    @Test
    public void testCreatePost() {
        PostRequestDto postRequestDto = PostRequestDto.builder()
                .title("Test Title")
                .category(DesignCategory.MEN)
                .description("Test Description")
                .media(new MockMultipartFile("test_file.jpg", new byte[0]))
                .build();

        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        Post post = Post.builder()
                .title(postRequestDto.getTitle())
                .category(postRequestDto.getCategory())
                .description(postRequestDto.getDescription())
                .media("test_file.jpg")
                .user(user)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        // Mock behavior of postDTOMapper
        when(postDTOMapper.apply(any(Post.class))).thenAnswer(invocation ->
             PostResponseDTO.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .category(post.getCategory())
                    .description(post.getDescription())
                    .likes(post.getTotalLikes())
                    .liked(post.isLiked())
                    .media(post.getMedia())
                    .build()
        );

        GenericResponse response = postService.createPost(postRequestDto, userId);

        assertNotNull(response);
        assertEquals("00", response.getCode());
        assertEquals("Successfully created", response.getMessage());
        assertEquals(HttpStatus.CREATED, response.getHttpStatus());

        PostResponseDTO postResponseDTO = (PostResponseDTO) response.getData();
        assertNotNull(postResponseDTO);
        assertEquals("Test Title", postResponseDTO.getTitle());
        assertEquals("Test Description", postResponseDTO.getDescription());
        assertEquals("test_file.jpg", postResponseDTO.getMedia());
        assertEquals(userId, user.getId());
    }


    @Test
    public void testGetAllPosts() {
        // Mock data for testing
        Long userId = 1L;
        Long userId1 = 2L;
        User user = new User();
        User user1 = new User();
        user.setId(userId);
        user1.setId(userId1);

        Pageable pageable = PageRequest.of(0, 10);

        List<Post> mockPosts = Arrays.asList(
                Post.builder().id(1L).title("Title 1").category(DesignCategory.MEN).description("Description 1")
                        .media("Media 1").user(user).build(),
                Post.builder().id(2L).title("Title 2").category(DesignCategory.WOMEN).description("Description 2")
                        .media("Media 2").user(user1).build()
        );

        Page<Post> mockPostsPage = new PageImpl<>(mockPosts, pageable, mockPosts.size());

        // Mock behavior of postRepository
        when(postRepository.findAllByUserId(eq(userId), eq(pageable))).thenReturn(mockPostsPage);

        // Mock behavior of postDTOMapper
        when(postDTOMapper.apply(any(Post.class))).thenAnswer(invocation -> {
            Post post = invocation.getArgument(0);
            return PostResponseDTO.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .description(post.getDescription())
                    .likes(post.getTotalLikes())
                    .liked(post.isLiked())
                    .media(post.getMedia())
                    .build();
        });

        // Call the method under test
        GenericResponse response = postService.getAllPosts(userId, pageable);

        // Assertions
        assertNotNull(response);
        assertEquals("00", response.getCode());
        assertEquals("Successful", response.getMessage());
        assertEquals(HttpStatus.OK, response.getHttpStatus());

        Page<PostResponseDTO> postResponseDTOS = (Page<PostResponseDTO>) response.getData();
        assertNotNull(postResponseDTOS);
        assertEquals(2, postResponseDTOS.getContent().size());
        assertEquals(1L, postResponseDTOS.getContent().get(0).getId());
        assertEquals("Title 1", postResponseDTOS.getContent().get(0).getTitle());
        assertEquals("Description 1", postResponseDTOS.getContent().get(0).getDescription());
        assertEquals(2L, postResponseDTOS.getContent().get(1).getId());
        assertEquals("Title 2", postResponseDTOS.getContent().get(1).getTitle());
        assertEquals("Description 2", postResponseDTOS.getContent().get(1).getDescription());
    }


    @Test
    public void testUpdatePost() throws IOException {
        // Mock data for testing
        Long postId = 1L;
        Long userId = 1L;

        PostRequestDto postRequestDto = new PostRequestDto("New Title",
                DesignCategory.MEN, "New Description",
                new MockMultipartFile("image.jpg", "image.jpg",
                MediaType.IMAGE_JPEG_VALUE, new byte[] {1, 2, 3, 4}));

        PostResponseDTO postResponseDTO = PostResponseDTO.builder()
                .title("Old Title")
                .category(DesignCategory.WOMEN)
                .description("Old Description")
                .media("old_image.jpg")
                .build();

        Post post = Post.builder()
                .id(postId)
                .title("New Title")
                .category(DesignCategory.MEN)
                .description("New Description")
                .media("new_image.jpg")
                .user(new User())
                .build();

        // Mock behavior of userRepository
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        // Mock behavior for saving a post
        when(postRepository.save(any(Post.class))).thenReturn(post);

        // Mock behavior of postUpdateDTOMapper
        when(postUpdateDTOMapper.apply(any(Post.class))).thenReturn(postResponseDTO);

        // Mock behavior for finding a post by postId
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // Call the method under test
        GenericResponse response = postService.updatePost(userId, postId, postRequestDto);

        // Assertions
        assertNotNull(response);
        assertEquals("00", response.getCode());
        assertEquals("Post updated successfully", response.getMessage());
        assertEquals(HttpStatus.ACCEPTED, response.getHttpStatus());

        PostResponseDTO updatedPostDTO = (PostResponseDTO) response.getData();
        assertNotNull(updatedPostDTO);
        assertEquals(postRequestDto.getTitle(), updatedPostDTO.getTitle());
        assertEquals(postRequestDto.getDescription(), updatedPostDTO.getDescription());
        assertNotEquals("old_image.jpg", updatedPostDTO.getMedia(), "Media should be updated");

    }

    @Test
    public void testDeletePost() {
        // Mock data for testing
        Long postId = 1L;
        Long userId = 1L;

        Post post = new Post();
        post.setId(postId);
        post.setMedia("image.jpg");

        // Mock behavior of postRepository
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // Call the method under test
        GenericResponse response = postService.deletePost(userId, postId);

        // Assertions
        assertNotNull(response);
        assertEquals("00", response.getCode());
        assertEquals("Successfully deleted", response.getMessage());
        assertEquals(HttpStatus.OK, response.getHttpStatus());

        // Verify that postRepository.delete() method was called once with the correct post object
        verify(postRepository, times(1)).delete(post);
    }

    @Test(expected = AppRequestException.class)
    public void testDeletePostPostNotFound() {
        // Mock data for testing
        Long postId = 1L;
        Long userId = 1L;

        // Mock behavior of postRepository
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // Call the method under test which should throw AppRequestException
        postService.deletePost(userId, postId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeletePostWithNullId() {
        // Call the method under test with null postId and userId, should throw IllegalArgumentException
        postService.deletePost(null, null);
    }

    @Test
    public void testGetPostById() {
        // Mock data for testing
        Long postId = 1L;
        Long userId = 1L;

        Post post = new Post();
        post.setId(postId);
        post.setTitle("Test Post");

        PostResponseDTO postResponseDTO = new PostResponseDTO();
        postResponseDTO.setId(post.getId());
        postResponseDTO.setTitle(post.getTitle());

        // Mock behavior of postRepository
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // Mock behavior of postDTOMapper
        when(postDTOMapper.apply(post)).thenReturn(postResponseDTO);

        // Call the method under test
        GenericResponse response = postService.getPostById(userId, postId);

        // Assertions
        assertNotNull(response);
        assertEquals("00", response.getCode());
        assertEquals("Successful", response.getMessage());
        assertEquals(HttpStatus.OK, response.getHttpStatus());

        // Check if the response contains the correct PostResponseDTO object
        assertEquals(postResponseDTO, response.getData());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPostByIdWithNullId() {
        // Call the method under test with null postId and userId, should throw IllegalArgumentException
        postService.getPostById(null, null);
    }
}
