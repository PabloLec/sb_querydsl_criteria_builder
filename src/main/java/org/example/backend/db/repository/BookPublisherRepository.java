package org.example.backend.db.repository;

import org.example.backend.db.model.BookPublisher;
import org.example.backend.db.model.BookPublisherId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookPublisherRepository extends JpaRepository<BookPublisher, BookPublisherId> {
}