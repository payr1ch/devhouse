package com.example.devhouse.search;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping("/search")
    public ResponseEntity<Object> search(@RequestBody SearchRequest query) {
        Object searchResults = searchService.search(query);
        return ResponseEntity.ok(searchResults);
    }
}
