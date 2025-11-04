package com.game.shadowcircle.chain;

import com.game.shadowcircle.model.Choice;
import com.game.shadowcircle.model.GameContext;
import com.game.shadowcircle.model.ValidationResult;

class CoverIntactValidator extends ChoiceValidator {

  @Override
  public ValidationResult validate(Choice choice, GameContext context) {
    if (!context.isCoverIntact()) {
      return ValidationResult.invalid(
          "Your legend has been exposed! Unable to continue the mission");
    }

    if (context.getSuspicionLevel() > 80) {
      return ValidationResult.warning("WARNING: Very high level of suspicion!");
    }

    return ValidationResult.valid();
  }
}
