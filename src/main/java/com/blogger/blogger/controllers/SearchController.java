package com.blogger.blogger.controllers;

import com.blogger.blogger.dto.response.GenericResponse;
import com.blogger.blogger.services.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;
    @GetMapping("/search")
    public ResponseEntity<GenericResponse> searchPostsOrComments(
        @RequestParam String keyword, Pageable pageable) {
        GenericResponse response = searchService.searchPostsOrComments(keyword, pageable);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }
}
