package com.game.shadowcircle.events;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class InvalidChoiceEvent extends GameEvent {

  private String reason;
  private int severity;

  public InvalidChoiceEvent(String reason) {
    super();
    this.setType("INVALID_CHOICE");
    this.setMessage("Unable to perform action: " + reason);
    this.setSeverity(1);
    this.setTimestamp(LocalDateTime.now());
    this.reason = reason;
  }
}