package com.blogger.blogger.dto.response;

import com.blogger.blogger.enums.DesignCategory;
import com.blogger.blogger.model.Comment;
import com.blogger.blogger.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;


import java.util.Date;

@Data
@SuperBuilder
@RequiredArgsConstructor
public class PostResponseDTO {
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private String title;
    private DesignCategory category;
    private String description;
    private Long likes;
    private Boolean liked;
    private String media;


}
