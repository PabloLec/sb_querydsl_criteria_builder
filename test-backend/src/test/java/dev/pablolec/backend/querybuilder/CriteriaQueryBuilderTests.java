package dev.pablolec.backend.querybuilder;

import dev.pablolec.backend.AbstractIntegrationTest;
import dev.pablolec.backend.db.model.*;
import dev.pablolec.querybuilder.CriteriaQueryBuilder;
import dev.pablolec.querybuilder.model.SearchCriterion;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CriteriaQueryBuilderTests extends AbstractIntegrationTest {
    @Autowired
    private CriteriaQueryBuilder criteriaQueryBuilder;

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

        List<Library> result = criteriaQueryBuilder.buildQuery(criteria, Library.class).fetch();
        assertEquals(1, result.size());
        assertEquals("Bibliothèque Nationale", result.getFirst().getName());
    }

    @Test
    void testLibraryByBookChild() {
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
                new SearchCriterion("book", "exists", List.of(subCriterion))
        );

        List<Library> result = criteriaQueryBuilder.buildQuery(criteria, Library.class).fetch();

        assertEquals(1, result.size());
        assertEquals("Library with Books", result.getFirst().getName());
    }

    @Test
    void testLibraryWitRootAndChildEntityConditions() {
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

        Membership membership = Membership.builder()
                .library(library)
                .joinDate(LocalDate.now())
                .expirationDate(LocalDate.now().plusYears(1))
                .membershipStatus("Active")
                .build();
        membershipRepository.save(membership);

        SearchCriterion subCriterion = new SearchCriterion("membershipStatus", "eq", "Active");
        List<SearchCriterion> criteria = List.of(
                new SearchCriterion("name", "eq", "Multi-Criteria Library"),
                new SearchCriterion("membership", "exists", List.of(subCriterion))
        );

        List<Library> result = criteriaQueryBuilder.buildQuery(criteria, Library.class).fetch();

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
                new SearchCriterion("book", "exists", List.of(new SearchCriterion("title", "like", "%Python%")))
        );

        List<Library> result = criteriaQueryBuilder.buildQuery(criteria, Library.class).fetch();

        assertEquals(0, result.size());
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

        List<Library> result = criteriaQueryBuilder.buildQuery(criteria, Library.class).fetch();
        assertEquals(1, result.size());
        assertEquals("Library B", result.getFirst().getName());
    }

    @Test
    void testLibraryByNonExistingBook() {
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
                new SearchCriterion("book", "notExists", List.of(subCriterion))
        );

        List<Library> result = criteriaQueryBuilder.buildQuery(criteria, Library.class).fetch();
        assertEquals(1, result.size());
        assertEquals("Quiet Library", result.getFirst().getName());
    }

    @Test
    void testBookByPublishYearGreaterThan() {
        Book book = Book.builder()
                .title("Historical Events")
                .isbn("222333444")
                .publishYear(1995)
                .edition("First")
                .language("English")
                .genre("History")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        bookRepository.save(book);

        List<SearchCriterion> criteria = List.of(
                new SearchCriterion("publishYear", "gt", "1990")
        );

        List<Book> result = criteriaQueryBuilder.buildQuery(criteria, Book.class).fetch();
        assertEquals(1, result.size());
        assertEquals("Historical Events", result.getFirst().getTitle());
    }

    @Test
    void testLibraryByEstablishedDateLowerThanOrEqualTo() {
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

        List<Library> result = criteriaQueryBuilder.buildQuery(criteria, Library.class).fetch();
        assertEquals(1, result.size());
        assertEquals("Night Library", result.getFirst().getName());
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

        List<Library> result = criteriaQueryBuilder.buildQuery(criteria, Library.class).fetch();
        assertEquals(1, result.size());
        assertEquals("Modern Library", result.getFirst().getName());
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

        List<Library> result = criteriaQueryBuilder.buildQuery(criteria, Library.class).fetch();
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

        List<Library> result = criteriaQueryBuilder.buildQuery(criteria, Library.class).fetch();
        assertEquals(1, result.size());
        assertEquals("Library Two", result.getFirst().getName());
    }

    @Test
    void testLibraryByMultipleChildEntities() {
        Library library = Library.builder()
                .name("Complex Research Library")
                .location("Research Blvd")
                .openingHours("24/7")
                .establishedDate(LocalDate.of(1950, 1, 1))
                .website("http://www.complexresearchlib.com")
                .email("info@complexlibrary.com")
                .phoneNumber("9876543210")
                .isOpen(true)
                .build();
        library = libraryRepository.save(library);

        for (int i = 1; i <= 5; i++) {
            Author author = Author.builder()
                    .name("Author " + i)
                    .bio("Bio of Author " + i)
                    .nationality("Country " + i)
                    .birthDate(LocalDate.of(1960 + i, 1, 1))
                    .website("http://author" + i + ".com")
                    .build();
            author = authorRepository.save(author);

            for (int j = 1; j <= 3; j++) {
                Book book = Book.builder()
                        .title("Deep Learning " + j)
                        .isbn("978-3-" + i + "0000-" + j)
                        .publishYear(2000 + j)
                        .edition(j + "th")
                        .language("English")
                        .genre("Science")
                        .library(library)
                        .author(author)
                        .createdAt(LocalDateTime.now().minusDays(j))
                        .updatedAt(LocalDateTime.now().minusHours(j))
                        .build();
                bookRepository.save(book);
            }
        }

        SearchCriterion deepLearningCriterion = new SearchCriterion("title", "like", "%Deep Learning%");
        SearchCriterion bookSubQuery = new SearchCriterion("book", "exists", List.of(deepLearningCriterion));

        SearchCriterion authorCriterion = new SearchCriterion("author.name", "like", "%Author 3%");
        SearchCriterion authorSubQuery = new SearchCriterion("book", "exists", List.of(authorCriterion));

        SearchCriterion languageCriterion = new SearchCriterion("language", "eq", "English");
        SearchCriterion bookLanguageSubQuery = new SearchCriterion("book", "exists", List.of(languageCriterion));

        SearchCriterion genreCriterion = new SearchCriterion("genre", "eq", "Science");
        SearchCriterion bookGenreSubQuery = new SearchCriterion("book", "exists", List.of(genreCriterion));

        List<SearchCriterion> criteria = Arrays.asList(
                new SearchCriterion("name", "like", "%research Library%"),
                new SearchCriterion("location", "eq", "Research Blvd"),
                new SearchCriterion("establishedDate", "gte", LocalDate.of(1950, 1, 1).toString()),
                bookSubQuery,
                authorSubQuery,
                bookLanguageSubQuery,
                bookGenreSubQuery
        );

        List<Library> result = criteriaQueryBuilder.buildQuery(criteria, Library.class).fetch();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.stream().anyMatch(lib -> "Complex Research Library".equals(lib.getName())));
        assertTrue(result.stream().anyMatch(lib -> lib.getBooks().stream().anyMatch(b -> b.getTitle().contains("Deep Learning"))));
        assertTrue(result.stream().anyMatch(lib -> lib.getBooks().stream().anyMatch(b -> "Author 3".equals(b.getAuthor().getName()))));
    }

    @Test
    void testMultiLevelNestedSubQueryWithSingleSubCriterion() {
        Library library = Library.builder()
                .name("Nested Query Library")
                .location("Nested City")
                .openingHours("09:00-18:00")
                .establishedDate(LocalDate.of(2000, 1, 1))
                .website("http://nestedlibrary.com")
                .build();

        libraryRepository.save(library);

        User user = User.builder()
                .username("nesteduser")
                .email("user@library.org")
                .password("secure123")
                .fullName("Nested User")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build();

        userRepository.save(user);

        LibraryEvent event = LibraryEvent.builder()
                .eventName("Nested Event")
                .eventDate(LocalDate.now())
                .description("Nested Event Description")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .library(library)
                .build();

        libraryEventRepository.save(event);

        EventParticipant participant = EventParticipant.builder()
                .eventId(event.getEventId())
                .userId(user.getUserId())
                .build();

        eventParticipantRepository.save(participant);


        SearchCriterion userCriterion = new SearchCriterion("username", "eq", "nesteduser");
        SearchCriterion participantSubQuery = new SearchCriterion("user", "exists", List.of(userCriterion));
        SearchCriterion eventCriterion = new SearchCriterion("participant", "exists", List.of(participantSubQuery));
        SearchCriterion librarySubQuery = new SearchCriterion("event", "exists", List.of(eventCriterion));
        List<SearchCriterion> criteria = List.of(
                new SearchCriterion("name", "eq", "Nested Query Library"),
                librarySubQuery
        );

        List<Library> result = criteriaQueryBuilder.buildQuery(criteria, Library.class).fetch();
        assertEquals(1, result.size());
        assertEquals("Nested Query Library", result.getFirst().getName());
    }

    @Test
    void testLibraryByBookChildWithMultipleCriteria() {
        Library library = Library.builder()
                .name("Central City Library")
                .location("Downtown Metropolis")
                .openingHours("09:00-19:00")
                .establishedDate(LocalDate.of(1950, 1, 1))
                .website("http://centralcitylib.org")
                .email("info@centralcitylib.org")
                .phoneNumber("1234567890")
                .isOpen(true)
                .build();
        libraryRepository.save(library);

        Book book = Book.builder()
                .title("Advanced C++ Programming")
                .isbn("987654321")
                .publishYear(2018)
                .edition("2nd")
                .language("French")
                .genre("Technology")
                .library(library)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        bookRepository.save(book);

        SearchCriterion titleCriterion = new SearchCriterion("title", "like", "%C++%");
        SearchCriterion yearCriterion = new SearchCriterion("publishYear", "gt", "2010");
        SearchCriterion languageCriterion = new SearchCriterion("language", "eq", "French");
        SearchCriterion genreCriterion = new SearchCriterion("genre", "notIn", List.of("Science", "Education").toString());
        SearchCriterion editionCriterion = new SearchCriterion("edition", "ne", "1st");
        SearchCriterion bookSubQuery = new SearchCriterion("book", "exists", List.of(titleCriterion, yearCriterion, languageCriterion, genreCriterion, editionCriterion));

        List<SearchCriterion> criteria = List.of(
                new SearchCriterion("name", "eq", "Central City Library"),
                bookSubQuery
        );

        List<Library> result = criteriaQueryBuilder.buildQuery(criteria, Library.class).fetch();
        assertEquals(1, result.size());
        assertEquals("Central City Library", result.getFirst().getName());
    }

    @Test
    void testMultiLevelNestedSubQueryWithMultipleSubCriteria() {
        Library library = Library.builder()
                .name("Expansive Research Library")
                .location("Capital Metropolis")
                .openingHours("08:00-22:00")
                .establishedDate(LocalDate.of(1995, 5, 15))
                .website("http://researchlibrary.com")
                .email("contact@researchlibrary.com")
                .phoneNumber("1231231234")
                .isOpen(false)
                .build();
        libraryRepository.save(library);

        User user = User.builder()
                .username("researchbuff")
                .email("buff@research.org")
                .password("password987")
                .fullName("Research Buff")
                .dateOfBirth(LocalDate.of(1985, 12, 25))
                .gender("Male")
                .build();
        userRepository.save(user);

        LibraryStaff staff = LibraryStaff.builder()
                .user(user)
                .library(library)
                .staffId(1)
                .build();

        libraryStaffRepository.save(staff);

        Book book = Book.builder()
                .title("Research Methods")
                .isbn("123456789")
                .publishYear(2010)
                .edition("3rd")
                .language("English")
                .genre("Research")
                .library(library)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        bookRepository.save(book);

        BorrowedBook borrowedBook = BorrowedBook.builder()
                .book(book)
                .user(user)
                .borrowDate(LocalDate.now())
                .returnDate(LocalDate.now().plusDays(30))
                .build();

        borrowedBookRepository.save(borrowedBook);

        Book bookWithoutUser = Book.builder()
                .title("Medicine Essentials")
                .isbn("987654321")
                .publishYear(2015)
                .edition("2nd")
                .language("Spanish")
                .genre("Research")
                .library(library)
                .createdAt(LocalDateTime.now().minusDays(30))
                .updatedAt(LocalDateTime.now())
                .build();

        bookRepository.save(bookWithoutUser);

        BorrowedBook borrowedBookWithoutUser = BorrowedBook.builder()
                .book(bookWithoutUser)
                .borrowDate(LocalDate.now())
                .returnDate(LocalDate.now().plusDays(30))
                .build();

        borrowedBookRepository.save(borrowedBookWithoutUser);

        SearchCriterion bookTitleCriterion = new SearchCriterion("book.title", "like", "%Research%");
        SearchCriterion bookYearCriterion = new SearchCriterion("book.publishYear", "gte", "2010");
        SearchCriterion bookCreationDateCriterion = new SearchCriterion("book.createdAt", "lte", LocalDateTime.now().toString());
        SearchCriterion bookLanguageCriterion = new SearchCriterion("book.language", "in", List.of("English", "Spanish").toString());
        List<SearchCriterion> borrowedBookCriteria = List.of(bookTitleCriterion, bookYearCriterion, bookLanguageCriterion);

        List<SearchCriterion> nonBorrowedBookCriteria = List.of(new SearchCriterion("book.title", "like", "%Medicine%"));

        SearchCriterion staffIdCriterion = new SearchCriterion("staffId", "eq", "1");
        SearchCriterion staffUserFullNameCriterion = new SearchCriterion("user.fullName", "like", "%Buff%");
        SearchCriterion staffUserDobCriterion = new SearchCriterion("user.dateOfBirth", "lte", LocalDate.of(1990, 1, 1).toString());
        SearchCriterion staffUserBorrowedBookCriterion = new SearchCriterion("user.borrowedBook", "exists", borrowedBookCriteria);
        SearchCriterion staffUserNonBorrowedBookCriterion = new SearchCriterion("user.borrowedBook", "notExists", nonBorrowedBookCriteria);
        List<SearchCriterion> staffCriteria = List.of(staffIdCriterion, staffUserFullNameCriterion, staffUserDobCriterion, staffUserBorrowedBookCriterion, staffUserNonBorrowedBookCriterion);

        SearchCriterion libraryNameCriterion = new SearchCriterion("name", "notLike", "%Test%");
        SearchCriterion libraryStaffCriterion = new SearchCriterion("staff", "exists", staffCriteria);

        List<SearchCriterion> criteria = List.of(
                libraryNameCriterion,
                libraryStaffCriterion
        );

        List<Library> result = criteriaQueryBuilder.buildQuery(criteria, Library.class).fetch();

        assertEquals(1, result.size());
    }
}