package com.game.shadowcircle.events;

import lombok.Getter;

@Getter
public class InvalidChoiceEvent extends GameEvent {

  private final String validationError;

  public InvalidChoiceEvent(String validationError) {
    super("INVALID_CHOICE", "Недійсний вибір: " + validationError, null);
    this.validationError = validationError;
  }
}