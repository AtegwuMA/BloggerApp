package com.blogger.blogger.dtoMapperTest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

import com.blogger.blogger.dto.response.PostCreateDTOMapper;
import com.blogger.blogger.dto.response.PostResponseDTO;
import com.blogger.blogger.model.Post;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
@RunWith(MockitoJUnitRunner.class)
public class PostCreateDTOMapperTest {
    @InjectMocks
    private PostCreateDTOMapper postCreateDTOMapper;

    @Test
    public void testApply() {
        // Mock a Post object
        Post post = mock(Post.class);
        when(post.getId()).thenReturn(1L);
        when(post.getTitle()).thenReturn("Test Post");
        when(post.getDescription()).thenReturn("Test Description");
        // Mock other methods as needed

        // Call the apply method of the mapper
        PostResponseDTO postResponseDTO = postCreateDTOMapper.apply(post);

        // Verify the result
        assertEquals(Long.valueOf(1), postResponseDTO.getId());
        assertEquals("Test Post", postResponseDTO.getTitle());
        assertEquals("Test Description", postResponseDTO.getDescription());
        // Verify other attributes as needed
    }
}

