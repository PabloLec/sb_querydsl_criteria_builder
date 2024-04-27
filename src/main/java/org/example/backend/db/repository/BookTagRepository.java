package org.example.backend.db.repository;

import org.example.backend.db.model.BookTag;
import org.example.backend.db.model.BookTagId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookTagRepository extends JpaRepository<BookTag, BookTagId> {
}