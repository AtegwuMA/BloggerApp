package com.blogger.blogger.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Data
@SuperBuilder
@RequiredArgsConstructor
@Table(name = "comments")
public class Comment extends PostCommentLike {

    @NotNull
    @Size(min = 10, message = "the description should be at least 10 Characters" )
    @Size(max = 1000, message = "the description cannot exceed 1000 Characters" )
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Post post;

    // Getters and Setters (Omitted for brevity)
}