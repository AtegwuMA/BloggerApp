package com.blogger.blogger.dto.request;

import com.blogger.blogger.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.validation.constraints.*;
import java.util.Date;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {
    @NotBlank(message = "First Name is required")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    private String lastName;


    @Size(max = 100)
    @NotBlank(message = "Email Name is required")
    @Email(message = "A valid email is required")
    @Column(unique = true)
    private String email;

    @NotNull
    @NotBlank(message = "Password Name is required")
    @Size(max = 128, message = "Password must be less than or equal to 128 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()-+=]).{8,}$", message = "Password must contain at least 8 characters, one digit, one lowercase letter, one uppercase letter, and one special character")
    private String password;

    @NotNull
    @Size(max = 128)
    private Date dob;

    @Size(max = 128)
    private Gender gender;
}
