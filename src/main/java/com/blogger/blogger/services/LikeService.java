package com.blogger.blogger.services;


public interface LikeService {
    void likePost(Long postId);
    void likeComment(Long commentId);

}
