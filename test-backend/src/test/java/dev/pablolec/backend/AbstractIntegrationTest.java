package dev.pablolec.backend;

import dev.pablolec.backend.db.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class AbstractIntegrationTest {
    @Autowired
    protected LibraryRepository libraryRepository;

    @Autowired
    protected AddressRepository addressRepository;

    @Autowired
    protected AuthorRepository authorRepository;

    @Autowired
    protected BookRepository bookRepository;

    @Autowired
    protected TagRepository tagRepository;

    @Autowired
    protected BookTagRepository bookTagRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected BorrowedBookRepository borrowedBookRepository;

    @Autowired
    protected ReviewRepository reviewRepository;

    @Autowired
    protected LibraryStaffRepository libraryStaffRepository;

    @Autowired
    protected MembershipRepository membershipRepository;

    @Autowired
    protected LibraryEventRepository libraryEventRepository;

    @Autowired
    protected EventParticipantRepository eventParticipantRepository;

    @Autowired
    protected PublisherRepository publisherRepository;

    @Autowired
    protected BookPublisherRepository bookPublisherRepository;

    @AfterEach
    void tearDown() {
        eventParticipantRepository.deleteAll();
        libraryEventRepository.deleteAll();
        membershipRepository.deleteAll();
        libraryStaffRepository.deleteAll();
        borrowedBookRepository.deleteAll();
        reviewRepository.deleteAll();
        bookTagRepository.deleteAll();
        tagRepository.deleteAll();
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        userRepository.deleteAll();
        addressRepository.deleteAll();
        libraryRepository.deleteAll();
        bookPublisherRepository.deleteAll();
        publisherRepository.deleteAll();
    }
}
