package dev.pablolec.backend.db.model;

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
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    private String username;
    private String email;
    private String password;
    private String fullName;
    private LocalDate dateOfBirth;
    private String gender;

    @OneToMany(mappedBy = "user")
    private Set<BorrowedBook> borrowedBooks;

    @OneToMany(mappedBy = "user")
    private Set<Review> reviews;

    @OneToMany(mappedBy = "user")
    private Set<Membership> memberships;

    @OneToMany(mappedBy = "user")
    private Set<EventParticipant> eventParticipants;

    @OneToMany(mappedBy = "user")
    private Set<LibraryStaff> libraryStaff;
}