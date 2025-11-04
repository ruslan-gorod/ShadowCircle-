package com.game.shadowcircle.chain;

import com.game.shadowcircle.model.Choice;
import com.game.shadowcircle.model.GameContext;
import com.game.shadowcircle.model.ValidationResult;

public class SuspicionLevelValidator extends ChoiceValidator {

  @Override
  public ValidationResult validate(Choice choice, GameContext context) {
    if (context.getSuspicionLevel() > 80 && choice.isHighProfile()) {
      return ValidationResult.invalid("Suspicion level too high");
    }
    return ValidationResult.valid();
  }
}