package org.example.backend.db.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Book")
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

    @ManyToOne
    @JoinColumn(name = "library_id")
    private Library library;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "book")
    private Set<BorrowedBook> borrowedBooks;

    @OneToMany(mappedBy = "book")
    private Set<Review> reviews;

    @OneToMany(mappedBy = "book")
    private Set<BookTag> bookTags;

    @OneToMany(mappedBy = "book")
    private Set<BookPublisher> publishers;
}
