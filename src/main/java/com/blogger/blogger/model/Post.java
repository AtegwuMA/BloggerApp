package com.blogger.blogger.model;

import com.blogger.blogger.enums.DesignCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Data
@SuperBuilder
@RequiredArgsConstructor
@Table(name = "posts")
public class Post extends PostCommentLike {

    @Size(max = 100)
    @Column(unique = true)
    private String title;

    @Enumerated(EnumType.STRING)
    private DesignCategory category;

    @Size(max = 3000, message = "the description cannot exceed 3000 Characters" )
    private String description;


    @Column(name = "media", length = 1000)
    private String media; // Store file content as byte array


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

}
