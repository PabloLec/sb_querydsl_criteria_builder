package dev.pablolec.backend.controller;

import dev.pablolec.backend.service.MockDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/library")
@RequiredArgsConstructor
@Slf4j
@Profile("mock")
public class MockController {
    private final MockDataService mockDataService;

    @PostMapping("/mock")
    public void insertMockData(@RequestParam int count) {
        mockDataService.createMockLibraries(count);
    }
}
