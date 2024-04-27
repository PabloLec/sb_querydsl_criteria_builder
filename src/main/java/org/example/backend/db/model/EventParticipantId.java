package org.example.backend.db.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventParticipantId implements Serializable {
    private Integer eventId;
    private Integer userId;
}