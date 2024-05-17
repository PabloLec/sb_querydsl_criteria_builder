package dev.pablolec.backend.service;

import dev.pablolec.backend.db.model.*;
import dev.pablolec.backend.db.repository.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
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

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        if (libraryRepository.count() == 0) {
            log.info("Database is empty. Creating mock libraries.");
            createMockLibraries(10);
        } else {
            log.info("Database already contains data. No mock data created.");
        }
    }

    public void createMockLibraries(int count) {
        log.info("Creating {} mock libraries", count);
        long currentLibraryCount = libraryRepository.count();
        for (int i = 0; i < count; i++) {
            Library library = createLibrary(i + currentLibraryCount);
            createAddressForLibrary(library, i + currentLibraryCount);
            createLibraryStaff(library, i + currentLibraryCount);
            createLibraryEvents(library, i + currentLibraryCount);
            createMembership(library, i + currentLibraryCount);
            createBooksAndAssociatedData(library, i + currentLibraryCount);
        }
    }

    private Library createLibrary(long index) {
        Library library = Library.builder()
                .name("Library " + index)
                .location("Location " + index)
                .openingHours("09:00 - 17:00")
                .establishedDate(LocalDate.now().minusYears(index % 50 + 1))
                .website("http://www.librarywebsite.com/" + index)
                .email("contact@library" + index + ".com")
                .phoneNumber("123-456-" + String.format("%04d", index))
                .isOpen(index % 2 == 0)
                .build();

        log.info("Creating library: {}", library);

        return libraryRepository.save(library);
    }

    private void createAddressForLibrary(Library library, long index) {
        Address address = Address.builder()
                .libraryId(library.getLibraryId())
                .street("Street " + index)
                .city("City " + index)
                .state("State " + index % 50)
                .country("Country " + index % 50)
                .postalCode(String.format("%05d", index))
                .build();

        log.info("Creating address: {} for library: {}", address, library.getLibraryId());

        addressRepository.save(address);
    }

    private void createLibraryStaff(Library library, long index) {
        int staffCount = (int) (index % 10 + 1);
        for (int i = 0; i < staffCount; i++) {
            User user = createUser(index * 10 + i);
            LibraryStaff libraryStaff = LibraryStaff.builder()
                    .library(library)
                    .user(user)
                    .role("Role " + (index % 5 + 1))
                    .build();

            log.info("Creating library staff: {} for library: {}", libraryStaff, library.getLibraryId());

            libraryStaffRepository.save(libraryStaff);
        }
    }

    private void createLibraryEvents(Library library, long index) {
        int eventCount = (int) (index % 5 + 1);
        for (int i = 0; i < eventCount; i++) {
            LibraryEvent libraryEvent = LibraryEvent.builder()
                    .library(library)
                    .eventName("Event " + (index * 10 + i))
                    .eventDate(LocalDate.now().plusDays(index % 365 + 1))
                    .description("Description " + (index * 10 + i))
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            log.info("Creating library event: {} for library: {}", libraryEvent, library.getLibraryId());

            libraryEvent = libraryEventRepository.save(libraryEvent);
            createEventParticipants(libraryEvent, index * 10 + i);
        }
    }

    private void createEventParticipants(LibraryEvent libraryEvent, long index) {
        int participantCount = (int) (index % 10 + 1);
        for (int i = 0; i < participantCount; i++) {
            User user = createUser(index * 10 + i);
            EventParticipant eventParticipant = EventParticipant.builder()
                    .eventId(libraryEvent.getEventId())
                    .userId(user.getUserId())
                    .build();

            log.info("Creating event participant: {} for event: {}", eventParticipant, libraryEvent.getEventId());

            eventParticipantRepository.save(eventParticipant);
        }
    }

    private void createMembership(Library library, long index) {
        int memberCount = (int) (index % 50 + 1);
        for (int i = 0; i < memberCount; i++) {
            User user = createUser(index * 10 + i);
            Membership membership = Membership.builder()
                    .user(user)
                    .library(library)
                    .joinDate(LocalDate.now().minusDays(index % 365 + 1))
                    .expirationDate(LocalDate.now().plusDays(index % 365 + 1))
                    .membershipStatus(index % 2 == 0 ? "Active" : "Inactive")
                    .build();

            log.info("Creating membership: {} for library: {}", membership, library.getLibraryId());

            membershipRepository.save(membership);
        }
    }

    private void createBooksAndAssociatedData(Library library, long index) {
        int bookCount = (int) (index % 50 + 1);
        for (int i = 0; i < bookCount; i++) {
            Author author = createAuthor(index * 10 + i);
            Book book = createBook(library, author, index * 10 + i);
            createBookTags(book, index * 10 + i);
            createBorrowedBooks(book, index * 10 + i);
            createReviews(book, index * 10 + i);
        }
    }

    private Author createAuthor(long index) {
        Author author = Author.builder()
                .name("Author " + index)
                .bio("Bio " + index)
                .nationality("Nationality " + index)
                .birthDate(LocalDate.now().minusYears(index % 100 + 18))
                .deathDate(index % 2 == 0 ? LocalDate.now().minusYears(index % 100 + 18) : null)
                .website("http://www.authorwebsite.com/" + index)
                .build();

        log.info("Creating author: {}", author);

        return authorRepository.save(author);
    }

    private Book createBook(Library library, Author author, long index) {
        Book book = Book.builder()
                .title("Book Title " + index)
                .isbn("ISBN " + index)
                .publishYear(LocalDate.now().getYear() - (int) (index % 100))
                .edition("Edition " + (index % 10 + 1))
                .language("Language " + (index % 10 + 1))
                .genre("Genre " + (index % 10 + 1))
                .library(library)
                .author(author)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        log.info("Creating book: {} for library: {}", book, library.getLibraryId());

        return bookRepository.save(book);
    }

    private void createBookTags(Book book, long index) {
        int tagCount = (int) (index % 5 + 1);
        for (int i = 0; i < tagCount; i++) {
            Tag tag = Tag.builder()
                    .name("Tag " + (index * 10 + i))
                    .description("Description " + (index * 10 + i))
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

    private void createBorrowedBooks(Book book, long index) {
        int borrowCount = (int) (index % 10 + 1);
        for (int i = 0; i < borrowCount; i++) {
            User user = createUser(index * 10 + i);
            BorrowedBook borrowedBook = BorrowedBook.builder()
                    .book(book)
                    .user(user)
                    .borrowDate(LocalDate.now().minusDays(index % 30 + 1))
                    .returnDate(LocalDate.now().plusDays(index % 30 + 1))
                    .build();

            log.info("Creating borrowed book: {} for book: {}", borrowedBook, book.getBookId());

            borrowedBookRepository.save(borrowedBook);
        }
    }

    private void createReviews(Book book, long index) {
        int reviewCount = (int) (index % 5 + 1);
        for (int i = 0; i < reviewCount; i++) {
            User user = createUser(index * 10 + i);
            Review review = Review.builder()
                    .book(book)
                    .user(user)
                    .rating(BigDecimal.valueOf(index % 5 + 1).setScale(2, RoundingMode.HALF_UP))
                    .comment("Comment " + (index * 10 + i))
                    .reviewDate(LocalDate.now().minusDays(index % 100 + 1))
                    .build();

            log.info("Creating review: {} for book: {}", review, book.getBookId());

            reviewRepository.save(review);
        }
    }

    private User createUser(long index) {
        User user = User.builder()
                .username("User" + index)
                .email("user" + index + "@mail.com")
                .password("password" + index)
                .fullName("Full Name " + index)
                .dateOfBirth(LocalDate.now().minusYears(index % 50 + 18))
                .gender(index % 2 == 0 ? "Male" : "Female")
                .build();

        log.info("Creating user: {}", user);

        return userRepository.save(user);
    }
}
