package dev.pablolec.backend.db.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookTagId implements Serializable {
    private Integer bookId;
    private Integer tagId;
}
