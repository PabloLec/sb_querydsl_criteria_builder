package dev.pablolec.backend.db.repository;

import dev.pablolec.backend.db.model.EventParticipant;
import dev.pablolec.backend.db.model.EventParticipantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventParticipantRepository extends JpaRepository<EventParticipant, EventParticipantId> {}
