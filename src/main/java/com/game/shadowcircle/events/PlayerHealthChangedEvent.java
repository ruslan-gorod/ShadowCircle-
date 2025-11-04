package com.game.shadowcircle.events;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PlayerHealthChangedEvent extends GameEvent {

  private int previousHealth;
  private int currentHealth;
  private int change;
  private int severity;
  private String cause;

  public PlayerHealthChangedEvent(int previousHealth, int currentHealth, String cause) {
    super();
    this.setType("PLAYER_HEALTH_CHANGED");
    this.change = currentHealth - previousHealth;
    this.setMessage(String.format("Health changed: %+d (reason: %s)", change, cause));
    this.setSeverity(change < -20 ? 2 : 1);
    this.setTimestamp(LocalDateTime.now());
    this.previousHealth = previousHealth;
    this.currentHealth = currentHealth;
    this.cause = cause;
  }
}