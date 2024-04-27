package org.example.backend;

import org.example.backend.db.model.*;
import org.example.backend.db.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RepositoryTests {

    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private BookTagRepository bookTagRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BorrowedBookRepository borrowedBookRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private BookPublisherRepository bookPublisherRepository;

    @Autowired
    private LibraryStaffRepository libraryStaffRepository;

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private LibraryEventRepository libraryEventRepository;

    @Autowired
    private EventParticipantRepository eventParticipantRepository;


    @Test
    void testLibraryInsertAndGet() {
        Library library = new Library();
        library.setName("Central Library");
        library.setLocation("Main St");
        library.setOpeningHours("09:00 - 18:00");
        library.setEstablishedDate(LocalDate.of(1950, 1, 1));
        library.setWebsite("http://www.centrallibrary.com");
        library.setEmail("info@centrallibrary.com");
        library.setPhoneNumber("1234567890");
        library.setIsOpen(true);
        library = libraryRepository.save(library);
        Library found = libraryRepository.findById(library.getLibraryId()).orElse(null);
        assertNotNull(found);
        assertEquals("Central Library", found.getName());
    }

    @Test
    void testAuthorAndBookInsertAndGet() {
        Author author = new Author();
        author.setName("John Doe");
        author.setBio("Some bio");
        author.setNationality("American");
        author.setBirthDate(LocalDate.of(1970, 5, 20));
        author.setDeathDate(null);
        author.setWebsite("http://www.johndoe.com");
        author = authorRepository.save(author);

        Book book = new Book();
        book.setTitle("Java Programming");
        book.setIsbn("123-4567890123");
        book.setPublishYear(2020);
        book.setEdition("Second Edition");
        book.setLanguage("English");
        book.setGenre("Education");
        book.setAuthorId(author.getAuthorId());
        book.setCreatedAt(LocalDateTime.now());
        book.setUpdatedAt(LocalDateTime.now());
        book = bookRepository.save(book);

        Author foundAuthor = authorRepository.findById(author.getAuthorId()).orElse(null);
        Book foundBook = bookRepository.findById(book.getBookId()).orElse(null);
        assertNotNull(foundAuthor);
        assertNotNull(foundBook);
        assertEquals("John Doe", foundAuthor.getName());
        assertEquals("Java Programming", foundBook.getTitle());
    }

    @Test
    void testTagAndBookTagInsertAndGet() {
        Tag tag = new Tag();
        tag.setName("Programming");
        tag.setDescription("All programming books");
        tag = tagRepository.save(tag);

        Book book = new Book();
        book.setTitle("More Java");
        book.setIsbn("789-0123456789");
        book.setPublishYear(2021);
        book.setEdition("First Edition");
        book.setLanguage("French");
        book.setGenre("Technology");
        book.setCreatedAt(LocalDateTime.now());
        book.setUpdatedAt(LocalDateTime.now());
        book = bookRepository.save(book);

        BookTag bookTag = new BookTag();
        bookTag.setBookId(book.getBookId());
        bookTag.setTagId(tag.getTagId());
        bookTag = bookTagRepository.save(bookTag);

        Tag foundTag = tagRepository.findById(tag.getTagId()).orElse(null);
        BookTag foundBookTag = bookTagRepository.findById(new BookTagId(book.getBookId(), tag.getTagId())).orElse(null);
        assertNotNull(foundTag);
        assertNotNull(foundBookTag);
        assertEquals("Programming", foundTag.getName());
    }

    @Test
    void testUserAndBorrowedBookInsertAndGet() {
        User user = new User();
        user.setUsername("user1");
        user.setEmail("user1@email.com");
        user.setPassword("password");
        user.setFullName("User One");
        user.setDateOfBirth(LocalDate.of(1990, 4, 5));
        user.setGender("Male");
        user = userRepository.save(user);

        Book book = new Book();
        book.setTitle("Advanced Java");
        book.setIsbn("234-5678901234");
        book.setPublishYear(2022);
        book.setEdition("Third Edition");
        book.setLanguage("English");
        book.setGenre("Programming");
        book.setCreatedAt(LocalDateTime.now());
        book.setUpdatedAt(LocalDateTime.now());
        book = bookRepository.save(book);

        BorrowedBook borrowedBook = new BorrowedBook();
        borrowedBook.setBookId(book.getBookId());
        borrowedBook.setUserId(user.getUserId());
        borrowedBook.setBorrowDate(LocalDate.now());
        borrowedBook.setReturnDate(LocalDate.now().plusDays(14));
        borrowedBook = borrowedBookRepository.save(borrowedBook);

        User foundUser = userRepository.findById(user.getUserId()).orElse(null);
        BorrowedBook foundBorrowedBook = borrowedBookRepository.findById(borrowedBook.getBorrowedId()).orElse(null);
        assertNotNull(foundUser);
        assertNotNull(foundBorrowedBook);
        assertEquals("user1", foundUser.getUsername());
        assertEquals(book.getBookId(), foundBorrowedBook.getBookId());
    }

    @Test
    void testPublisherAndBookPublisherInsertAndGet() {
        Publisher publisher = new Publisher();
        publisher.setName("Pearson");
        publisher.setWebsite("http://www.pearson.com");
        publisher = publisherRepository.save(publisher);

        Book book = new Book();
        book.setTitle("Effective Java");
        book.setIsbn("987-6543210987");
        book.setPublishYear(2018);
        book.setEdition("Third Edition");
        book.setLanguage("English");
        book.setGenre("Programming");
        book.setCreatedAt(LocalDateTime.now());
        book.setUpdatedAt(LocalDateTime.now());
        book = bookRepository.save(book);

        BookPublisher bookPublisher = new BookPublisher();
        bookPublisher.setBookId(book.getBookId());
        bookPublisher.setPublisherId(publisher.getPublisherId());
        bookPublisher = bookPublisherRepository.save(bookPublisher);

        Publisher foundPublisher = publisherRepository.findById(publisher.getPublisherId()).orElse(null);
        BookPublisher foundBookPublisher = bookPublisherRepository.findById(new BookPublisherId(book.getBookId(), publisher.getPublisherId())).orElse(null);
        assertNotNull(foundPublisher);
        assertNotNull(foundBookPublisher);
        assertEquals("Pearson", foundPublisher.getName());
    }

    @Test
    void testLibraryStaffInsertAndGet() {
        Library library = new Library();
        library.setName("Downtown Library");
        library.setLocation("Central Blvd");
        library.setOpeningHours("08:00 - 19:00");
        library.setEstablishedDate(LocalDate.of(1985, 3, 15));
        library.setWebsite("http://www.downtownlibrary.com");
        library.setEmail("contact@downtownlibrary.com");
        library.setPhoneNumber("9876543210");
        library.setIsOpen(true);
        library = libraryRepository.save(library);

        User user = new User();
        user.setUsername("staffmember");
        user.setEmail("staff@downtownlibrary.com");
        user.setPassword("securepassword");
        user.setFullName("Staff Member");
        user.setDateOfBirth(LocalDate.of(1980, 10, 10));
        user.setGender("Female");
        user = userRepository.save(user);

        LibraryStaff libraryStaff = new LibraryStaff();
        libraryStaff.setLibraryId(library.getLibraryId());
        libraryStaff.setUserId(user.getUserId());
        libraryStaff.setRole("Librarian");
        libraryStaff = libraryStaffRepository.save(libraryStaff);

        LibraryStaff foundLibraryStaff = libraryStaffRepository.findById(libraryStaff.getStaffId()).orElse(null);
        assertNotNull(foundLibraryStaff);
        assertEquals("Librarian", foundLibraryStaff.getRole());
    }

    @Test
    void testMembershipInsertAndGet() {
        User user = new User();
        user.setUsername("member");
        user.setEmail("member@library.com");
        user.setPassword("memberpass");
        user.setFullName("Regular Member");
        user.setDateOfBirth(LocalDate.of(1995, 8, 25));
        user.setGender("Male");
        user = userRepository.save(user);

        Library library = new Library();
        library.setName("Community Library");
        library.setLocation("Suburb St");
        library.setOpeningHours("10:00 - 17:00");
        library.setEstablishedDate(LocalDate.of(2000, 12, 1));
        library.setWebsite("http://www.communitylibrary.com");
        library.setEmail("info@communitylibrary.com");
        library.setPhoneNumber("1231231234");
        library.setIsOpen(true);
        library = libraryRepository.save(library);

        Membership membership = new Membership();
        membership.setUserId(user.getUserId());
        membership.setLibraryId(library.getLibraryId());
        membership.setJoinDate(LocalDate.now());
        membership.setExpirationDate(LocalDate.now().plusYears(1));
        membership.setMembershipStatus("Active");
        membership = membershipRepository.save(membership);

        Membership foundMembership = membershipRepository.findById(membership.getMembershipId()).orElse(null);
        assertNotNull(foundMembership);
        assertEquals("Active", foundMembership.getMembershipStatus());
    }

    @Test
    void testLibraryEventAndEventParticipantInsertAndGet() {
        Library library = new Library();
        library.setName("Metro Library");
        library.setLocation("East Side");
        library.setOpeningHours("07:00 - 20:00");
        library.setEstablishedDate(LocalDate.of(1990, 7, 23));
        library.setWebsite("http://www.metrolibrary.com");
        library.setEmail("service@metrolibrary.com");
        library.setPhoneNumber("3213214321");
        library.setIsOpen(true);
        library = libraryRepository.save(library);

        LibraryEvent libraryEvent = new LibraryEvent();
        libraryEvent.setLibraryId(library.getLibraryId());
        libraryEvent.setEventName("Book Fair");
        libraryEvent.setEventDate(LocalDate.now().plusMonths(1));
        libraryEvent.setDescription("Annual book fair with guest authors and signings.");
        libraryEvent.setCreatedAt(LocalDateTime.now());
        libraryEvent.setUpdatedAt(LocalDateTime.now());
        libraryEvent = libraryEventRepository.save(libraryEvent);

        User user = new User();
        user.setUsername("eventgoer");
        user.setEmail("goer@events.com");
        user.setPassword("goerpass");
        user.setFullName("Event Goer");
        user.setDateOfBirth(LocalDate.of(1985, 12, 15));
        user.setGender("Non-binary");
        user = userRepository.save(user);

        EventParticipant eventParticipant = new EventParticipant();
        eventParticipant.setEventId(libraryEvent.getEventId());
        eventParticipant.setUserId(user.getUserId());
        eventParticipant = eventParticipantRepository.save(eventParticipant);

        EventParticipant foundEventParticipant = eventParticipantRepository.findById(new EventParticipantId(libraryEvent.getEventId(), user.getUserId())).orElse(null);
        assertNotNull(foundEventParticipant);
    }

}
