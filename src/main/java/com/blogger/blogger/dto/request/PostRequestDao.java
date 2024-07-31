package com.blogger.blogger.dto.request;


import com.blogger.blogger.dto.response.PostResponseDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;
@Service
public class PostRequestDao implements Function<PostResponseDTO, PostRequestDto> {
    @Override
    public PostRequestDto apply(PostResponseDTO postResponseDTO){
        return PostRequestDto.builder()
                .title(postResponseDTO.getTitle())
                .category(postResponseDTO.getCategory())
                .description(postResponseDTO.getDescription())
                .build();
    }

}
