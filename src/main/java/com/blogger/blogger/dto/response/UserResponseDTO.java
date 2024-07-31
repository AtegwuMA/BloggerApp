package com.blogger.blogger.dto.response;

import com.blogger.blogger.enums.Gender;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
@Data
@Builder
public class UserResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Gender gender;
    private Date createdAt;
    private Date updatedAt;
}
