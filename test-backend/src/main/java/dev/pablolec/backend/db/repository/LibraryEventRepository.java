package dev.pablolec.backend.db.repository;

import dev.pablolec.backend.db.model.LibraryEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraryEventRepository extends JpaRepository<LibraryEvent, Integer> {
}