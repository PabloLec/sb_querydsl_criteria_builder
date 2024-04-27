package org.example.backend.db.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer addressId;

    @Column(insertable = false, updatable = false, name = "library_id")
    private Integer libraryId;

    @ManyToOne
    @JoinColumn(name = "library_id")
    private Library library;

    private String street;
    private String city;
    private String state;
    private String country;
    private String postalCode;
}