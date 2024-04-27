package org.example.backend;

import org.example.backend.db.model.Book;
import org.example.backend.db.model.Library;
import org.example.backend.db.model.Membership;
import org.example.backend.db.model.User;
import org.example.backend.service.DynamicQueryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DynamicQueryServiceTests extends AbstractIntegrationTest {
    @Autowired
    private DynamicQueryService dynamicQueryService;

    @Test
    void testSimpleLibraryByName() {
        Library library = Library.builder()
                .name("Bibliothèque Nationale")
                .location("Paris")
                .openingHours("09:00-17:00")
                .establishedDate(LocalDate.of(1884, 1, 1))
                .website("http://bnf.fr")
                .email("info@bnf.fr")
                .phoneNumber("0123456789")
                .isOpen(true)
                .build();
        libraryRepository.save(library);

        List<DynamicQueryService.SearchCriterion> criteria = List.of(
                new DynamicQueryService.SearchCriterion("name", "eq", "Bibliothèque Nationale")
        );

        List<Library> result = (List<Library>) dynamicQueryService.buildDynamicQuery(criteria, "library").fetch();
        assertEquals(1, result.size());
        assertEquals("Bibliothèque Nationale", result.getFirst().getName());
    }

    @Test
    void testLibraryWithBookChild() {
        Library library = Library.builder()
                .name("Library with Books")
                .location("Somewhere")
                .openingHours("08:00-18:00")
                .establishedDate(LocalDate.now())
                .website("http://libbooks.com")
                .email("contact@libbooks.com")
                .phoneNumber("9876543210")
                .isOpen(true)
                .build();
        libraryRepository.save(library);

        Book book = Book.builder()
                .title("Java Basics")
                .isbn("123456789")
                .publishYear(2020)
                .edition("3rd")
                .language("English")
                .genre("Education")
                .library(library)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        bookRepository.save(book);

        DynamicQueryService.SearchCriterion subCriterion = new DynamicQueryService.SearchCriterion("title", "like", "%Java%");
        List<DynamicQueryService.SearchCriterion> criteria = List.of(
                new DynamicQueryService.SearchCriterion("book", "exists", subCriterion)
        );

        List<Library> result = (List<Library>) dynamicQueryService.buildDynamicQuery(criteria, "library").fetch();

        assertEquals(1, result.size());
        assertEquals("Library with Books", result.getFirst().getName());
    }

    @Test
    void testLibraryWithMultipleChildEntityConditions() {
        Library library = Library.builder()
                .name("Multi-Criteria Library")
                .location("Multi-City")
                .openingHours("10:00-20:00")
                .establishedDate(LocalDate.now())
                .website("http://multilib.com")
                .email("multi@lib.com")
                .phoneNumber("9876501234")
                .isOpen(true)
                .build();
        libraryRepository.save(library);

        User user = User.builder()
                .username("user123")
                .email("user123@library.com")
                .password("secure123")
                .fullName("User OneTwoThree")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .gender("Male")
                .build();
        userRepository.save(user);

        Membership membership = Membership.builder()
                .user(user)
                .library(library)
                .joinDate(LocalDate.now())
                .expirationDate(LocalDate.now().plusYears(1))
                .membershipStatus("Active")
                .build();
        membershipRepository.save(membership);

        DynamicQueryService.SearchCriterion subCriterion = new DynamicQueryService.SearchCriterion("membershipStatus", "eq", "Active");
        List<DynamicQueryService.SearchCriterion> criteria = List.of(
                new DynamicQueryService.SearchCriterion("name", "eq", "Multi-Criteria Library"),
                new DynamicQueryService.SearchCriterion("membership", "exists", subCriterion)
        );

        List<Library> result = (List<Library>) dynamicQueryService.buildDynamicQuery(criteria, "library").fetch();

        assertEquals(1, result.size());
        assertEquals("Multi-Criteria Library", result.getFirst().getName());
    }

    @Test
    void testLibraryNoMatch() {
        Library library = Library.builder()
                .name("Library with Books")
                .location("Somewhere")
                .openingHours("08:00-18:00")
                .establishedDate(LocalDate.now())
                .website("http://libbooks.com")
                .email("contact@libbooks.com")
                .phoneNumber("9876543210")
                .isOpen(true)
                .build();
        libraryRepository.save(library);

        Book book = Book.builder()
                .title("Java Basics")
                .isbn("123456789")
                .publishYear(2020)
                .edition("3rd")
                .language("English")
                .genre("Education")
                .library(library)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        bookRepository.save(book);

        List<DynamicQueryService.SearchCriterion> criteria = List.of(
                new DynamicQueryService.SearchCriterion("name", "eq", "Library with Books"),
                new DynamicQueryService.SearchCriterion("book", "exists", new DynamicQueryService.SearchCriterion("title", "like", "%Python%"))
        );

        List<Library> result = (List<Library>) dynamicQueryService.buildDynamicQuery(criteria, "library").fetch();

        assertEquals(0, result.size());
    }

}