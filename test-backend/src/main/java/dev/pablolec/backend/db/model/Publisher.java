package dev.pablolec.backend.db.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Publisher")
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer publisherId;
    private String name;
    private String website;

    @OneToMany(mappedBy = "publisher")
    @JsonManagedReference
    private Set<BookPublisher> publishedBooks;
}