package dev.pablolec.backend.service;

import dev.pablolec.backend.db.model.*;
import dev.pablolec.backend.db.repository.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MockDataService {
    private final LibraryRepository libraryRepository;
    private final AddressRepository addressRepository;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final TagRepository tagRepository;
    private final BookTagRepository bookTagRepository;
    private final UserRepository userRepository;
    private final BorrowedBookRepository borrowedBookRepository;
    private final ReviewRepository reviewRepository;
    private final LibraryStaffRepository libraryStaffRepository;
    private final MembershipRepository membershipRepository;
    private final LibraryEventRepository libraryEventRepository;
    private final EventParticipantRepository eventParticipantRepository;

    private final Random random = new Random();

    public void createMockLibraries(int count) {
        log.info("Creating {} mock libraries", count);
        for (int i = 0; i < count; i++) {
            Library library = createLibrary();
            createAddressForLibrary(library);
            createLibraryStaff(library);
            createLibraryEvents(library);
            createMembership(library);
            createBooksAndAssociatedData(library);
        }
    }

    private Library createLibrary() {
        Library library = Library.builder()
                .name("Library " + random.nextInt(1, 1000))
                .location("Location " + random.nextInt(1, 1000))
                .openingHours("09:00 - 17:00")
                .establishedDate(LocalDate.now().minusYears(random.nextInt(1, 50) + 1))
                .website("http://www.librarywebsite.com/" + random.nextInt(1, 1000))
                .email("contact@library" + random.nextInt(1, 1000) + ".com")
                .phoneNumber("123-456-" + random.nextInt(1, 9999))
                .isOpen(random.nextBoolean())
                .build();

        log.info("Creating library: {}", library);

        return libraryRepository.save(library);
    }

    private void createAddressForLibrary(Library library) {
        Address address = Address.builder()
                .libraryId(library.getLibraryId())
                .street("Street " + random.nextInt(1, 1000))
                .city("City " + random.nextInt(1, 1000))
                .state("State " + random.nextInt(1, 50))
                .country("Country " + random.nextInt(1, 50))
                .postalCode(String.format("%05d", random.nextInt(1, 99999)))
                .build();

        log.info("Creating address: {} for library: {}", address, library.getLibraryId());

        addressRepository.save(address);
    }

    private void createLibraryStaff(Library library) {
        int staffCount = 1 + random.nextInt(1, 10);
        for (int i = 0; i < staffCount; i++) {
            User user = createUser();
            LibraryStaff libraryStaff = LibraryStaff.builder()
                    .library(library)
                    .user(user)
                    .role("Role " + random.nextInt(1, 5))
                    .build();

            log.info("Creating library staff: {} for library: {}", libraryStaff, library.getLibraryId());

            libraryStaffRepository.save(libraryStaff);
        }
    }

    private void createLibraryEvents(Library library) {
        int eventCount = random.nextInt(1, 5);
        for (int i = 0; i < eventCount; i++) {
            LibraryEvent libraryEvent = LibraryEvent.builder()
                    .library(library)
                    .eventName("Event " + random.nextInt(1, 1000))
                    .eventDate(LocalDate.now().plusDays(random.nextInt(1, 365)))
                    .description("Description " + random.nextInt(1, 1000))
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            log.info("Creating library event: {} for library: {}", libraryEvent, library.getLibraryId());

            libraryEvent = libraryEventRepository.save(libraryEvent);
            createEventParticipants(libraryEvent);
        }
    }

    private void createEventParticipants(LibraryEvent libraryEvent) {
        int participantCount = random.nextInt(1, 10);
        for (int i = 0; i < participantCount; i++) {
            User user = createUser();
            EventParticipant eventParticipant = EventParticipant.builder()
                    .eventId(libraryEvent.getEventId())
                    .userId(user.getUserId())
                    .build();

            log.info("Creating event participant: {} for event: {}", eventParticipant, libraryEvent.getEventId());

            eventParticipantRepository.save(eventParticipant);
        }
    }

    private void createMembership(Library library) {
        int memberCount = random.nextInt(1, 50);
        for (int i = 0; i < memberCount; i++) {
            User user = createUser();
            Membership membership = Membership.builder()
                    .user(user)
                    .library(library)
                    .joinDate(LocalDate.now().minusDays(random.nextInt(1, 365)))
                    .expirationDate(LocalDate.now().plusDays(random.nextInt(1, 365)))
                    .membershipStatus(random.nextBoolean() ? "Active" : "Inactive")
                    .build();

            log.info("Creating membership: {} for library: {}", membership, library.getLibraryId());

            membershipRepository.save(membership);
        }
    }

    private void createBooksAndAssociatedData(Library library) {
        int bookCount = random.nextInt(1, 50);
        for (int i = 0; i < bookCount; i++) {
            Author author = createAuthor();
            Book book = createBook(library, author);
            createBookTags(book);
            createBorrowedBooks(book);
            createReviews(book);
        }
    }

    private Author createAuthor() {
        Author author = Author.builder()
                .name("Author " + random.nextInt(1, 1000))
                .bio("Bio " + random.nextInt(1, 1000))
                .nationality("Nationality " + random.nextInt(1, 1000))
                .birthDate(LocalDate.now().minusYears(random.nextInt(1, 100) + 18))
                .deathDate(random.nextBoolean() ? LocalDate.now().minusYears(random.nextInt(1, 100) + 18) : null)
                .website("http://www.authorwebsite.com/" + random.nextInt(1, 1000))
                .build();

        log.info("Creating author: {}", author);

        return authorRepository.save(author);
    }

    private Book createBook(Library library, Author author) {
        Book book = Book.builder()
                .title("Book Title " + random.nextInt(1, 1000))
                .isbn("ISBN " + random.nextInt(1, 1000000))
                .publishYear(LocalDate.now().getYear() - random.nextInt(1, 100))
                .edition("Edition " + random.nextInt(1, 10))
                .language("Language " + random.nextInt(1, 10))
                .genre("Genre " + random.nextInt(1, 10))
                .library(library)
                .author(author)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        log.info("Creating book: {} for library: {}", book, library.getLibraryId());

        return bookRepository.save(book);
    }

    private void createBookTags(Book book) {
        int tagCount = random.nextInt(1, 5);
        for (int i = 0; i < tagCount; i++) {
            Tag tag = Tag.builder()
                    .name("Tag " + random.nextInt(1, 1000))
                    .description("Description " + random.nextInt(1, 1000))
                    .build();

            log.info("Creating tag: {} for book: {}", tag, book.getBookId());

            tag = tagRepository.save(tag);

            BookTag bookTag = BookTag.builder()
                    .bookId(book.getBookId())
                    .tagId(tag.getTagId())
                    .build();

            log.info("Creating book tag: {} for book: {}", bookTag, book.getBookId());

            bookTagRepository.save(bookTag);
        }
    }

    private void createBorrowedBooks(Book book) {
        int borrowCount = random.nextInt(1, 10);
        for (int i = 0; i < borrowCount; i++) {
            User user = createUser();
            BorrowedBook borrowedBook = BorrowedBook.builder()
                    .book(book)
                    .user(user)
                    .borrowDate(LocalDate.now().minusDays(random.nextInt(1, 30)))
                    .returnDate(LocalDate.now().plusDays(random.nextInt(1, 30)))
                    .build();

            log.info("Creating borrowed book: {} for book: {}", borrowedBook, book.getBookId());

            borrowedBookRepository.save(borrowedBook);
        }
    }

    private void createReviews(Book book) {
        int reviewCount = random.nextInt(1, 5);
        for (int i = 0; i < reviewCount; i++) {
            User user = createUser();
            Review review = Review.builder()
                    .book(book)
                    .user(user)
                    .rating(BigDecimal.valueOf(random.nextDouble() * 5).setScale(2, RoundingMode.HALF_UP))
                    .comment("Comment " + random.nextInt(1, 1000))
                    .reviewDate(LocalDate.now().minusDays(random.nextInt(1, 100)))
                    .build();

            log.info("Creating review: {} for book: {}", review, book.getBookId());

            reviewRepository.save(review);
        }
    }

    private User createUser() {
        User user = User.builder()
                .username("User" + random.nextInt(1, 1000))
                .email("user" + random.nextInt(1, 1000) + "@mail.com")
                .password("password" + random.nextInt(1, 1000))
                .fullName("Full Name " + random.nextInt(1, 1000))
                .dateOfBirth(LocalDate.now().minusYears(random.nextInt(1, 50) + 18))
                .gender(random.nextBoolean() ? "Male" : "Female")
                .build();

        log.info("Creating user: {}", user);

        return userRepository.save(user);
    }
}
