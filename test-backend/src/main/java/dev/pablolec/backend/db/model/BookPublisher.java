package dev.pablolec.backend.db.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Book_Publisher")
@IdClass(BookPublisherId.class)
public class BookPublisher {
    @Id
    @Column(name = "book_id")
    private Integer bookId;

    @Id
    @Column(name = "publisher_id")
    private Integer publisherId;

    @ManyToOne
    @JoinColumn(name = "book_id", insertable = false, updatable = false)
    @JsonBackReference
    private Book book;

    @ManyToOne
    @JoinColumn(name = "publisher_id", insertable = false, updatable = false)
    @JsonBackReference
    private Publisher publisher;
}
