package dev.pablolec.backend.db.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Author")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer authorId;
    private String name;
    private String bio;
    private String nationality;
    private LocalDate birthDate;
    private LocalDate deathDate;
    private String website;

    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Book> books;
}