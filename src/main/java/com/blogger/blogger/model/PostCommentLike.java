package com.blogger.blogger.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
@ToString
@SuperBuilder
@RequiredArgsConstructor
public abstract class PostCommentLike extends AuditModel {

    private Boolean liked;

    @Column(name = "likes")
    private Long totalLikes;

    public Boolean isLiked() {
        return liked != null ? liked : Boolean.FALSE;
    }
}
