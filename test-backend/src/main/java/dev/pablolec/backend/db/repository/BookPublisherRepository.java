package dev.pablolec.backend.db.repository;

import dev.pablolec.backend.db.model.BookPublisher;
import dev.pablolec.backend.db.model.BookPublisherId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookPublisherRepository extends JpaRepository<BookPublisher, BookPublisherId> {}
