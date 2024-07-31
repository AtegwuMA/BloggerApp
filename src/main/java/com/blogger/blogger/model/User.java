package com.blogger.blogger.model;


import com.blogger.blogger.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "users")
public class User extends AuditModel implements Serializable {

    @Size(max = 65)
    @NotBlank(message = "First Name is required")
    @Column(name = "first_name")
    private String firstName;

    @Size(max = 65)
    @NotBlank(message = "Last Name is required")
    @Column(name = "last_name")
    private String lastName;

    @Email
    @Size(max = 100)
    @NotBlank(message = "Email Name is required")
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

    @Enumerated(EnumType.STRING)
    @Size(max = 128)
    private Gender gender;
}
