package dev.pablolec.backend.db.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Library_Staff")
public class LibraryStaff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer staffId;

    @ManyToOne
    @JoinColumn(name = "library_id")
    private Library library;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String role;
}
