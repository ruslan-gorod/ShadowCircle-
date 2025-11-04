package com.game.shadowcircle.events;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SuspicionChangedEvent extends GameEvent {

  private int previousSuspicion;
  private int currentSuspicion;
  private int change;
  private int severity;
  private String cause;

  public SuspicionChangedEvent(int previousSuspicion, int currentSuspicion, String cause) {
    super();
    this.setType("SUSPICION_CHANGED");
    this.change = currentSuspicion - previousSuspicion;
    this.setMessage(String.format("Suspicion level: %+d (reason: %s)", change, cause));
    this.setSeverity(currentSuspicion > 80 ? 2 : 1);
    this.setTimestamp(LocalDateTime.now());
    this.previousSuspicion = previousSuspicion;
    this.currentSuspicion = currentSuspicion;
    this.cause = cause;
  }
}