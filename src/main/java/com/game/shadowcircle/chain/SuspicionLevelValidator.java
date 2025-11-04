package com.game.shadowcircle.chain;

import com.game.shadowcircle.model.Choice;
import com.game.shadowcircle.model.GameContext;

public class SuspicionLevelValidator extends ChoiceValidator {

  @Override
  public ValidationResult validate(Choice choice, GameContext context) {
    if (context.getSuspicionLevel() > 80 && choice.isHighProfile()) {
      return ValidationResult.invalid("Занадто високий рівень підозри");
    }
    return ValidationResult.valid();
  }
}