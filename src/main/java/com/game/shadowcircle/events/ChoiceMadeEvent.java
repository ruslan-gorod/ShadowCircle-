package com.game.shadowcircle.events;

import com.game.shadowcircle.model.Choice;
import com.game.shadowcircle.model.DecisionResult;
import com.game.shadowcircle.model.Scene;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ChoiceMadeEvent extends GameEvent {

  private Choice choice;
  private DecisionResult result;
  private Scene scene;

  public ChoiceMadeEvent(Choice choice, DecisionResult result) {
    super();
    this.setType("CHOICE_MADE");
    this.setMessage(String.format("The decision was made: %s", choice.getDescription()));
    this.setPayload(result);
    this.setTimestamp(LocalDateTime.now());
    this.choice = choice;
    this.result = result;
  }
}