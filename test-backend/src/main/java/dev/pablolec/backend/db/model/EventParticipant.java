package dev.pablolec.backend.db.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
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
    @JsonBackReference
    private LibraryEvent event;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonBackReference
    private User user;
}