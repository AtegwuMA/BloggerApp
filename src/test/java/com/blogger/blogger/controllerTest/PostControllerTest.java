package com.blogger.blogger.controllerTest;

import com.blogger.blogger.controllers.PostController;
import com.blogger.blogger.dto.request.PostRequestDto;
import com.blogger.blogger.dto.response.GenericResponse;
import com.blogger.blogger.enums.DesignCategory;
import com.blogger.blogger.services.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PostControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private PostController postController;

    @Mock
    private PostService postService;

    private final Long userId = 1L;
    private final Long postId = 1L;
    private PostRequestDto postRequestDto;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(postController)
                .setValidator(new LocalValidatorFactoryBean())
                .build();

        // Initialize postRequestDto as needed for tests
        postRequestDto = new PostRequestDto("New Title", DesignCategory.MEN, "New Description", new MockMultipartFile("image.jpg", "image.jpg", MediaType.IMAGE_JPEG_VALUE, new byte[] {1, 2, 3, 4}));
    }

    @Test
    public void getAllPostsTest() throws Exception {
        // Mock postService.getAllPosts(userId, pageable) method
        when(postService.getAllPosts(userId, Pageable.unpaged())).thenReturn(new GenericResponse("00", "Success", HttpStatus.OK));

        this.mockMvc.perform(get("/api/users/{userId}/posts", userId))
                .andExpect(status().isOk());
        // Add more assertions as needed
    }

    @Test
    public void createDesignsTest() throws Exception {
        // Mock postService.createPost(postRequestDto, userId) method
        when(postService.createPost(postRequestDto, userId)).thenReturn(new GenericResponse("00", "Successfull+-y created", HttpStatus.CREATED));

        this.mockMvc.perform(post("/api/users/{userId}/designs", userId)
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isCreated());
        // Add more assertions as needed
    }

    @Test
    public void updateDesignsTest() throws Exception {
        // Mock postService.updatePost(userId, postId, postRequestDto) method
        when(postService.updatePost(userId, postId, postRequestDto)).thenReturn(new GenericResponse("00", "Success", HttpStatus.OK));

        this.mockMvc.perform(post("/api/users/{userId}/posts/{postId}", userId, postId)
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isOk());
        // Add more assertions as needed
    }

    @Test
    public void deletePostTest() throws Exception {
        // Mock postService.deletePost(userId, postId) method
        when(postService.deletePost(userId, postId)).thenReturn(new GenericResponse("02", "Bsd Request", HttpStatus.NO_CONTENT));

        this.mockMvc.perform(delete("/api/users/{userId}/posts/{postId}", userId, postId))
                .andExpect(status().isNoContent());
        // Add more assertions as needed
    }

    @Test
    public void getPostTest() throws Exception {
        // Mock postService.getPostById(userId, postId) method
        when(postService.getPostById(userId, postId)).thenReturn(new GenericResponse("00", "Success", HttpStatus.OK));

        this.mockMvc.perform(get("/api/users/{userId}/posts/{postId}", userId, postId))
                .andExpect(status().isOk());
        // Add more assertions as needed
    }

}