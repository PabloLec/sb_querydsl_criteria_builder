package org.example.backend.db.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Library")
public class Library {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer libraryId;
    private String name;
    private String location;
    private String openingHours;
    private LocalDate establishedDate;
    private String website;
    private String email;
    private String phoneNumber;
    private Boolean isOpen;

    @OneToMany(mappedBy = "library")
    private Set<Book> books;

    @OneToMany(mappedBy = "library")
    private Set<Membership> memberships;

    @OneToMany(mappedBy = "library")
    private Set<LibraryEvent> events;

    @OneToMany(mappedBy = "library")
    private Set<LibraryStaff> staff;
}