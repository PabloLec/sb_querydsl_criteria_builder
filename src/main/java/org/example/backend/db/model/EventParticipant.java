package org.example.backend.db.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Event_Participant")
@IdClass(EventParticipantId.class)
public class EventParticipant {
    @Id
    @Column(name = "event_id")
    private Integer eventId;

    @Id
    @Column(name = "user_id")
    private Integer userId;

    @ManyToOne
    @JoinColumn(name = "event_id", insertable = false, updatable = false)
    private LibraryEvent event;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
}