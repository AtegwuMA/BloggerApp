package com.blogger.blogger.dto.response;

import com.blogger.blogger.model.Post;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.util.Date;

@Data
@SuperBuilder
@RequiredArgsConstructor
public class CommentResponseDTO {
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private String content;
    private Long likes;
    private Boolean liked;

}
