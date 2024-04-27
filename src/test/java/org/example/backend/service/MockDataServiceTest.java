package org.example.backend.service;

import org.example.backend.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class MockDataServiceTest extends AbstractIntegrationTest {
    @Autowired
    private MockDataService mockDataService;


    @Test
    void createMockLibraries() {
        mockDataService.createMockLibraries(1);
        assertEquals(1, libraryRepository.count());
        assertNotEquals(0, addressRepository.count());
        assertNotEquals(0, libraryStaffRepository.count());
        assertNotEquals(0, libraryEventRepository.count());
        assertNotEquals(0, membershipRepository.count());
        assertNotEquals(0, bookRepository.count());
        assertNotEquals(0, authorRepository.count());
        assertNotEquals(0, tagRepository.count());
        assertNotEquals(0, bookTagRepository.count());
        assertNotEquals(0, userRepository.count());
        assertNotEquals(0, borrowedBookRepository.count());
        assertNotEquals(0, reviewRepository.count());
        assertNotEquals(0, eventParticipantRepository.count());
    }
}