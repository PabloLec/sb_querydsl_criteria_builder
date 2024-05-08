package dev.pablolec.backend.db.model;

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
    @JsonManagedReference
    private Set<BorrowedBook> borrowedBooks;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private Set<Review> reviews;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private Set<Membership> memberships;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private Set<EventParticipant> eventParticipants;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private Set<LibraryStaff> libraryStaff;
}