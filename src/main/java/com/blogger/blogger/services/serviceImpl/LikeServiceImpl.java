package com.blogger.blogger.services.serviceImpl;


import com.blogger.blogger.exceptions.AppRequestException;
import com.blogger.blogger.model.Comment;
import com.blogger.blogger.model.Post;
import com.blogger.blogger.repository.CommentRepository;
import com.blogger.blogger.repository.PostRepository;
import com.blogger.blogger.services.LikeService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {


    private final PostRepository postRepository;


    private final CommentRepository commentRepository;

    public void likePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppRequestException("Post not found with id: " + postId));

        // Toggle the like status for the post
        post.setLiked(!post.isLiked());

        Long totalLikes = post.getTotalLikes();
        long updatedLikes = totalLikes != null ? (post.isLiked() ? totalLikes + 1 : totalLikes - 1) : 1L;
        post.setTotalLikes(updatedLikes);


        postRepository.save(post);
    }

    public void likeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppRequestException("Comment not found with id: " + commentId));

        // Toggle the like status for the comment
        comment.setLiked(!comment.isLiked());

        Long totalLikes = comment.getTotalLikes();
        long updatedLikes = totalLikes != null ? (comment.isLiked() ? totalLikes + 1 : totalLikes - 1) : 1L;
        comment.setTotalLikes(updatedLikes);

        commentRepository.save(comment);
    }


}

