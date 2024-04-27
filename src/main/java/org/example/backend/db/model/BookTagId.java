package org.example.backend.db.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookTagId implements Serializable {
    private Integer bookId;
    private Integer tagId;
}
