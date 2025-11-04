package com.game.shadowcircle.chain;

import com.game.shadowcircle.model.Choice;
import com.game.shadowcircle.model.GameContext;

class PlayerAliveValidator extends ChoiceValidator {

  @Override
  public ValidationResult validate(Choice choice, GameContext context) {
    if (!context.getPlayer().isAlive()) {
      return ValidationResult.invalid("Гравець не може виконувати дії (здоров'я = 0)");
    }
    return ValidationResult.valid();
  }
}