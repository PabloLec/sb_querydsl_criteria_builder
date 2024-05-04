package dev.pablolec.backend.db.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookTagId implements Serializable {
    private Integer bookId;
    private Integer tagId;
}