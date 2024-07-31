package com.blogger.blogger.services.serviceImpl;

import com.blogger.blogger.dto.response.GenericResponse;
import com.blogger.blogger.dto.response.PostCreateDTOMapper;
import com.blogger.blogger.dto.response.PostUpdateDTOMapper;
import com.blogger.blogger.model.Comment;
import com.blogger.blogger.model.Post;
import com.blogger.blogger.repository.CommentRepository;
import com.blogger.blogger.repository.PostRepository;
import com.blogger.blogger.services.SearchService;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Builder
@Slf4j
public class SearchServiceImpl implements SearchService{

    private final PostRepository postRepository;

    private final PostCreateDTOMapper postCreateDTOMapper;

    private final CommentRepository commentRepository;

    private final PostUpdateDTOMapper postUpdateDTOMapper;

    public SearchServiceImpl(PostRepository postRepository, PostCreateDTOMapper postCreateDTOMapper, CommentRepository commentRepository, PostUpdateDTOMapper postUpdateDTOMapper) {
        this.postRepository = postRepository;
        this.postCreateDTOMapper = postCreateDTOMapper;
        this.commentRepository = commentRepository;
        this.postUpdateDTOMapper = postUpdateDTOMapper;
    }

    @Override
    public GenericResponse searchPostsOrComments(String keyword, Pageable pageable) {

        // Search for posts containing the keyword
        Page<Post> postsPage = postRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                keyword, keyword, pageable);

        // Search for comments containing the keyword
        Page<Comment> commentsPage = commentRepository.findByContentContainingIgnoreCase(keyword, pageable);

        // Combine the posts and comments into a single list
        List<Object> combinedResults = new ArrayList<>();
        combinedResults.addAll(postsPage.getContent());
        combinedResults.addAll(commentsPage.getContent());

        // Sort the combined results based on the pageable sort
        List<Object> sortedResults = combinedResults.stream()
                .sorted((o1, o2) -> {
                    List<Sort.Order> orders = pageable.getSort().toList();

                    for (Sort.Order order : orders) {
                        if (order.getProperty().equals("createdAt")) {
                            Date value1 = ((Post) o1).getCreatedAt();
                            Date value2 = ((Post) o2).getCreatedAt();
                            int comparison = value1.compareTo(value2) * (order.getDirection().isAscending() ? 1 : -1);

                            if (comparison != 0) {
                                return comparison;
                            }
                        }
                    }

                    return 0;
                })
                .collect(Collectors.toList());
        Page<?> searched = new PageImpl<>(sortedResults, pageable, combinedResults.size());
        return new GenericResponse("00", "Successful", HttpStatus.OK, searched);
    }

}
