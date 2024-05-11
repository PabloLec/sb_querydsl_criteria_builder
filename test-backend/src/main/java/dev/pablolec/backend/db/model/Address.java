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
@Table(name = "Address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer addressId;

    @Column(insertable = false, updatable = false, name = "library_id")
    private Integer libraryId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "library_id")
    @JsonBackReference
    private Library library;

    private String street;
    private String city;
    private String state;
    private String country;
    private String postalCode;
}
