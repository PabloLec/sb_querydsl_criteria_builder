package dev.pablolec.backend.controller;

import dev.pablolec.backend.db.model.Library;
import dev.pablolec.backend.service.MockDataService;
import dev.pablolec.querybuilder.CriteriaQueryBuilder;
import dev.pablolec.querybuilder.model.SearchCriterion;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/library")
@RequiredArgsConstructor
@Slf4j
public class LibraryController {
    private final CriteriaQueryBuilder criteriaQueryBuilder;
    private final MockDataService mockDataService;

    @PostMapping("/query")
    @Cacheable("libraries")
    public List<Library> getLibrariesByQuery(@RequestBody List<SearchCriterion> query) {
        log.info("Received query: {}", query);
        return criteriaQueryBuilder.buildQuery(query, Library.class).fetch();
    }

    @PostMapping("/mock")
    public void insertMockData(@RequestParam int count) {
        mockDataService.createMockLibraries(count);
    }
}
