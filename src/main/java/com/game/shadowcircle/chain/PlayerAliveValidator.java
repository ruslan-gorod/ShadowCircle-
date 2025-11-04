package com.game.shadowcircle.chain;

import com.game.shadowcircle.model.Choice;
import com.game.shadowcircle.model.GameContext;
import com.game.shadowcircle.model.ValidationResult;

public class PlayerAliveValidator extends ChoiceValidator {

  @Override
  public ValidationResult validate(Choice choice, GameContext context) {
    if (!context.getPlayer().isAlive()) {
      return ValidationResult.invalid("Player cannot perform actions (health = 0)");
    }
    return ValidationResult.valid();
  }
}