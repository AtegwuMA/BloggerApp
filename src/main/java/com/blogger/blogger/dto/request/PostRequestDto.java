package com.blogger.blogger.dto.request;

import com.blogger.blogger.enums.DesignCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@SuperBuilder
@RequiredArgsConstructor
@AllArgsConstructor
public class PostRequestDto {

    @Size(max = 100, message = "the title cannot exceed 100 Characters")
    private String title;

    @Size(min = 2, message = "the description should be at least 2 Characters" )
    @Size(max = 40, message = "the description cannot exceed 40 Characters" )
    private DesignCategory category;

    @Size(min = 10, message = "the description should be at least 10 Characters" )
    @Size(max = 1000, message = "the description cannot exceed 1000 Characters" )
    private String description;

    @NotNull(message = "Media file must be provided")
    private MultipartFile media;

}
