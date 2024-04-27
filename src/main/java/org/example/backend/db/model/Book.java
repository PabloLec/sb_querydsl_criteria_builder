package org.example.backend.db.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Book")
@Getter
@Setter
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookId;
    private String title;
    private String isbn;
    private Integer publishYear;
    private String edition;
    private String language;
    private String genre;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "library_id")
    private Library library;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private Author author;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "book", fetch = FetchType.EAGER)
    private Set<BorrowedBook> borrowedBooks;

    @OneToMany(mappedBy = "book", fetch = FetchType.EAGER)
    private Set<Review> reviews;

    @OneToMany(mappedBy = "book", fetch = FetchType.EAGER)
    private Set<BookTag> bookTags;

    @OneToMany(mappedBy = "book", fetch = FetchType.EAGER)
    private Set<BookPublisher> publishers;
}
