package dev.pablolec.backend.db.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.*;

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
    @JsonBackReference
    private Library library;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    @JsonBackReference
    private Author author;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "book", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<BorrowedBook> borrowedBooks;

    @OneToMany(mappedBy = "book", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Review> reviews;

    @OneToMany(mappedBy = "book", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<BookTag> bookTags;

    @OneToMany(mappedBy = "book", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<BookPublisher> publishers;
}
