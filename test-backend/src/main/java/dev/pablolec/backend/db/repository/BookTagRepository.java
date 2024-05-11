package dev.pablolec.backend.db.repository;

import dev.pablolec.backend.db.model.BookTag;
import dev.pablolec.backend.db.model.BookTagId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookTagRepository extends JpaRepository<BookTag, BookTagId> {}
