package com.game.shadowcircle.events;

import com.game.shadowcircle.model.Choice;
import com.game.shadowcircle.strategy.DecisionResult;
import lombok.Getter;

@Getter
public class ChoiceMadeEvent extends GameEvent {

  private final Choice choice;
  private final DecisionResult result;

  public ChoiceMadeEvent(Choice choice, DecisionResult result) {
    super("CHOICE_MADE", "Зроблено вибір: " + choice.getDescription(), result);
    this.choice = choice;
    this.result = result;
  }
}