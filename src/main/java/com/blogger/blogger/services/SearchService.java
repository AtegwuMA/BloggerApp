package com.blogger.blogger.services;

import com.blogger.blogger.dto.response.GenericResponse;
import org.springframework.data.domain.Pageable;

public interface SearchService {
    GenericResponse searchPostsOrComments(String keyword, Pageable pageable);
}
