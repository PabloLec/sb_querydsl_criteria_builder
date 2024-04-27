package org.example.backend;

import org.example.backend.db.model.Book;
import org.example.backend.db.model.Library;
import org.example.backend.db.model.Membership;
import org.example.backend.db.model.User;
import org.example.backend.service.query.DynamicQueryService;
import org.example.backend.service.query.SearchCriterion;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        List<SearchCriterion> criteria = List.of(
                new SearchCriterion("name", "eq", "Bibliothèque Nationale")
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

        SearchCriterion subCriterion = new SearchCriterion("title", "like", "%Java%");
        List<SearchCriterion> criteria = List.of(
                new SearchCriterion("book", "exists", subCriterion)
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

        SearchCriterion subCriterion = new SearchCriterion("membershipStatus", "eq", "Active");
        List<SearchCriterion> criteria = List.of(
                new SearchCriterion("name", "eq", "Multi-Criteria Library"),
                new SearchCriterion("membership", "exists", subCriterion)
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

        List<SearchCriterion> criteria = List.of(
                new SearchCriterion("name", "eq", "Library with Books"),
                new SearchCriterion("book", "exists", new SearchCriterion("title", "like", "%Python%"))
        );

        List<Library> result = (List<Library>) dynamicQueryService.buildDynamicQuery(criteria, "library").fetch();

        assertEquals(0, result.size());
    }

    @Test
    void testLibraryByLocation() {
        Library library = Library.builder()
                .name("Local Library")
                .location("Downtown")
                .openingHours("08:00-20:00")
                .establishedDate(LocalDate.of(2000, 1, 1))
                .website("http://locallibrary.com")
                .email("contact@locallibrary.com")
                .phoneNumber("9876543210")
                .isOpen(true)
                .build();
        libraryRepository.save(library);

        List<SearchCriterion> criteria = List.of(
                new SearchCriterion("location", "eq", "Downtown")
        );

        List<Library> result = (List<Library>) dynamicQueryService.buildDynamicQuery(criteria, "library").fetch();
        assertEquals(1, result.size());
        assertEquals("Local Library", result.get(0).getName());
    }

    @Test
    void testLibraryByNameNotEqual() {
        Library library1 = Library.builder()
                .name("Library A")
                .location("North Side")
                .openingHours("10:00-18:00")
                .establishedDate(LocalDate.of(1995, 5, 15))
                .website("http://librarya.com")
                .email("info@librarya.com")
                .phoneNumber("1234567890")
                .isOpen(true)
                .build();
        libraryRepository.save(library1);

        Library library2 = Library.builder()
                .name("Library B")
                .location("South Side")
                .openingHours("09:00-19:00")
                .establishedDate(LocalDate.of(1998, 8, 20))
                .website("http://libraryb.com")
                .email("info@libraryb.com")
                .phoneNumber("0987654321")
                .isOpen(true)
                .build();
        libraryRepository.save(library2);

        List<SearchCriterion> criteria = List.of(
                new SearchCriterion("name", "ne", "Library A")
        );

        List<Library> result = (List<Library>) dynamicQueryService.buildDynamicQuery(criteria, "library").fetch();
        assertEquals(1, result.size());
        assertEquals("Library B", result.get(0).getName());
    }

    @Test
    void testLibraryWithNoBooks() {
        Library library = Library.builder()
                .name("Quiet Library")
                .location("Suburb")
                .openingHours("10:00-17:00")
                .establishedDate(LocalDate.of(2010, 10, 10))
                .website("http://quietlibrary.com")
                .email("quiet@library.com")
                .phoneNumber("9876543210")
                .isOpen(true)
                .build();
        libraryRepository.save(library);

        Book book = Book.builder()
                .title("Loud Noises")
                .isbn("987654321")
                .publishYear(2011)
                .edition("First")
                .language("English")
                .genre("Science")
                .library(library)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        bookRepository.save(book);

        SearchCriterion subCriterion = new SearchCriterion("title", "like", "%Silence%");
        List<SearchCriterion> criteria = List.of(
                new SearchCriterion("book", "notExists", subCriterion)
        );

        List<Library> result = (List<Library>) dynamicQueryService.buildDynamicQuery(criteria, "library").fetch();
        assertEquals(1, result.size());
        assertEquals("Quiet Library", result.get(0).getName());
    }

    @Test
    void testLibraryWithSpecificBookGenre() {
        Library library = Library.builder()
                .name("Genre Specific Library")
                .location("City Center")
                .openingHours("09:00-21:00")
                .establishedDate(LocalDate.of(2005, 3, 3))
                .website("http://genrespecificlibrary.com")
                .email("genre@library.com")
                .phoneNumber("1231231234")
                .isOpen(true)
                .build();
        libraryRepository.save(library);

        Book book = Book.builder()
                .title("Fantasy World")
                .isbn("111222333")
                .publishYear(2006)
                .edition("Second")
                .language("Spanish")
                .genre("Fantasy")
                .library(library)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        bookRepository.save(book);

        SearchCriterion subCriterion = new SearchCriterion("genre", "eq", "Fantasy");
        List<SearchCriterion> criteria = List.of(
                new SearchCriterion("book", "exists", subCriterion)
        );

        List<Library> result = (List<Library>) dynamicQueryService.buildDynamicQuery(criteria, "library").fetch();
        assertEquals(1, result.size());
        assertEquals("Genre Specific Library", result.get(0).getName());
    }

    @Test
    void testLibraryByPublishYearGreaterThan() {
        Library library = Library.builder()
                .name("History Library")
                .location("Downtown")
                .openingHours("10:00-20:00")
                .establishedDate(LocalDate.of(1990, 1, 1))
                .website("http://historylibrary.com")
                .email("info@historylibrary.com")
                .phoneNumber("1234567890")
                .isOpen(true)
                .build();
        libraryRepository.save(library);

        Book book = Book.builder()
                .title("Historical Events")
                .isbn("222333444")
                .publishYear(1995)
                .edition("First")
                .language("English")
                .genre("History")
                .library(library)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        bookRepository.save(book);

        List<SearchCriterion> criteria = List.of(
                new SearchCriterion("publishYear", "gt", "1990")
        );

        List<Book> result = (List<Book>) dynamicQueryService.buildDynamicQuery(criteria, "book").fetch();
        assertEquals(1, result.size());
        assertEquals("Historical Events", result.get(0).getTitle());
    }

    @Test
    void testLibraryByOpeningHoursLessThan() {
        Library library = Library.builder()
                .name("Night Library")
                .location("Central City")
                .openingHours("23:00-03:00") // Stored as String, demonstration purposes only
                .establishedDate(LocalDate.of(2000, 1, 1))
                .website("http://nightlibrary.com")
                .email("night@library.com")
                .phoneNumber("9876543210")
                .isOpen(true)
                .build();
        libraryRepository.save(library);

        List<SearchCriterion> criteria = List.of(
                new SearchCriterion("establishedDate", "lte", LocalDate.of(2010, 1, 1).toString())
        );

        List<Library> result = (List<Library>) dynamicQueryService.buildDynamicQuery(criteria, "library").fetch();
        assertEquals(1, result.size());
        assertEquals("Night Library", result.get(0).getName());
    }

    @Test
    void testLibraryByEstablishedDateGreaterThanOrEqualTo() {
        Library library = Library.builder()
                .name("Modern Library")
                .location("New Town")
                .openingHours("09:00-19:00")
                .establishedDate(LocalDate.of(2010, 1, 1))
                .website("http://modernlibrary.com")
                .email("contact@modernlibrary.com")
                .phoneNumber("1234567890")
                .isOpen(true)
                .build();
        libraryRepository.save(library);

        List<SearchCriterion> criteria = List.of(
                new SearchCriterion("establishedDate", "gte", LocalDate.of(2010, 1, 1).toString())
        );

        List<Library> result = (List<Library>) dynamicQueryService.buildDynamicQuery(criteria, "library").fetch();
        assertEquals(1, result.size());
        assertEquals("Modern Library", result.get(0).getName());
    }

    @Test
    void testLibraryByIdInList() {
        Library library1 = Library.builder()
                .name("Library One")
                .build();
        libraryRepository.save(library1);

        Library library2 = Library.builder()
                .name("Library Two")
                .build();
        libraryRepository.save(library2);

        List<Integer> ids = List.of(library1.getLibraryId(), library2.getLibraryId());
        List<SearchCriterion> criteria = List.of(
                new SearchCriterion("libraryId", "in", ids.toString())
        );

        List<Library> result = (List<Library>) dynamicQueryService.buildDynamicQuery(criteria, "library").fetch();
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(lib -> lib.getName().equals("Library One")));
        assertTrue(result.stream().anyMatch(lib -> lib.getName().equals("Library Two")));
    }

    @Test
    void testLibraryByIdNotInList() {
        Library library1 = Library.builder()
                .name("Library One")
                .build();
        libraryRepository.save(library1);

        Library library2 = Library.builder()
                .name("Library Two")
                .build();
        libraryRepository.save(library2);

        List<Integer> ids = List.of(library1.getLibraryId());
        List<SearchCriterion> criteria = List.of(
                new SearchCriterion("libraryId", "notIn", ids.toString())
        );

        List<Library> result = (List<Library>) dynamicQueryService.buildDynamicQuery(criteria, "library").fetch();
        assertEquals(1, result.size());
        assertEquals("Library Two", result.get(0).getName());
    }
}