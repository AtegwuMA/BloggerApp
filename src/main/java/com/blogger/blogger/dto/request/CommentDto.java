package com.blogger.blogger.dto.request;

import com.blogger.blogger.model.Post;

import javax.validation.constraints.Size;

public class CommentDto {
    @Size(min = 10, message = "the description should be at least 10 Characters" )
    @Size(max = 1000, message = "the description cannot exceed 1000 Characters" )
    private String content;

}
