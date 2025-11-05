package com.game.shadowcircle.events;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CriticalSituationEvent extends GameEvent {

  private SituationType situationType;
  private String details;
  private int severity;

  public CriticalSituationEvent(SituationType situationType, String details) {
    super();
    this.setType("CRITICAL_SITUATION");
    this.setMessage(situationType.getDescription() + " " + details);
    this.setSeverity(3);
    this.setTimestamp(LocalDateTime.now());
    this.situationType = situationType;
    this.details = details;
  }

  public enum SituationType {
    COVER_BLOWN("Legend exposed!"),
    LOW_HEALTH("Critical low health!"),
    HIGH_SUSPICION("Very high suspicion!"),
    MISSION_CRITICAL("Mission critical!"),
    ENEMY_ALERT("Enemy alert!");

    private final String description;

    SituationType(String description) {
      this.description = description;
    }

    public String getDescription() {
      return description;
    }
  }
}