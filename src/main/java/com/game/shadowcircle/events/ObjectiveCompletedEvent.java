package com.game.shadowcircle.events;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ObjectiveCompletedEvent extends GameEvent {

  private String objectiveId;
  private String description;
  private int reward;

  public ObjectiveCompletedEvent(String objectiveId, String description, int reward) {
    super();
    this.setType("OBJECTIVE_COMPLETED");
    this.setMessage(String.format("Goal completed: %s (+%d points)", description, reward));
    this.setTimestamp(LocalDateTime.now());
    this.objectiveId = objectiveId;
    this.description = description;
    this.reward = reward;
  }
}
