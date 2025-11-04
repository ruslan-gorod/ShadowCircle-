package com.game.shadowcircle.events;

import lombok.Getter;

@Getter
public class PlayerHealthChangedEvent extends GameEvent {

  private final int oldHealth;
  private final int newHealth;
  private final String reason;

  public PlayerHealthChangedEvent(int oldHealth, int newHealth, String reason) {
    super("PLAYER_HEALTH_CHANGED", "Зміна здоров'я: " + oldHealth + " -> " + newHealth, null);
    this.oldHealth = oldHealth;
    this.newHealth = newHealth;
    this.reason = reason;
  }
}