package org.example.backend.db.repository;

import org.example.backend.db.model.EventParticipant;
import org.example.backend.db.model.EventParticipantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventParticipantRepository extends JpaRepository<EventParticipant, EventParticipantId> {
}