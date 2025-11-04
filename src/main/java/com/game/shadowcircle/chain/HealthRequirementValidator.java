package com.game.shadowcircle.chain;

import com.game.shadowcircle.model.Choice;
import com.game.shadowcircle.model.GameContext;
import com.game.shadowcircle.model.ValidationResult;

class HealthRequirementValidator extends ChoiceValidator {

  @Override
  public ValidationResult validate(Choice choice, GameContext context) {
    int requiredHealth = choice.getRiskLevel();

    if (context.getPlayer().getHealth() < requiredHealth) {
      return ValidationResult.warning(
          String.format("Risky! Health: %d, Risk: %d",
              context.getPlayer().getHealth(), requiredHealth)
      );
    }

    return ValidationResult.valid();
  }
}
