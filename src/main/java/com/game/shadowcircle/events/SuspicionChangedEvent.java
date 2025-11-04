package com.game.shadowcircle.events;

import lombok.Getter;

@Getter
public class SuspicionChangedEvent extends GameEvent {

  private final int oldSuspicion;
  private final int newSuspicion;
  private final String reason;

  public SuspicionChangedEvent(int oldSuspicion, int newSuspicion, String reason) {
    super("SUSPICION_CHANGED", "Зміна підозри: " + oldSuspicion + " -> " + newSuspicion, null);
    this.oldSuspicion = oldSuspicion;
    this.newSuspicion = newSuspicion;
    this.reason = reason;
  }
}