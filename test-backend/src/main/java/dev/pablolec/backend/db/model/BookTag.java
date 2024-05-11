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
@Table(name = "Book_Tag")
@IdClass(BookTagId.class)
public class BookTag {
    @Id
    @Column(name = "book_id")
    private Integer bookId;

    @Id
    @Column(name = "tag_id")
    private Integer tagId;

    @ManyToOne
    @JoinColumn(name = "book_id", insertable = false, updatable = false)
    @JsonBackReference
    private Book book;

    @ManyToOne
    @JoinColumn(name = "tag_id", insertable = false, updatable = false)
    @JsonBackReference
    private Tag tag;
}
