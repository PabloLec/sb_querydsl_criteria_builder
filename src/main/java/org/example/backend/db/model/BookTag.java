package org.example.backend.db.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Book_Tag")
@IdClass(BookTagId.class)
public class BookTag {
    @Id
    private Integer bookId;
    @Id
    private Integer tagId;
}