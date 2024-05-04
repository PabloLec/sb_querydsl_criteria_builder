package dev.pablolec.backend.db.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventParticipantId implements Serializable {
    private Integer eventId;
    private Integer userId;
}