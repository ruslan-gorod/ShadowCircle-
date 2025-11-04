package com.game.shadowcircle.chain;

import com.game.shadowcircle.model.Choice;
import com.game.shadowcircle.model.GameContext;

class HealthRequirementValidator extends ChoiceValidator {

  @Override
  public ValidationResult validate(Choice choice, GameContext context) {
    int requiredHealth = choice.getRiskLevel();

    if (context.getPlayer().getHealth() < requiredHealth) {
      return ValidationResult.warning(
          String.format("Ризиковано! Здоров'я: %d, ризик: %d",
              context.getPlayer().getHealth(), requiredHealth)
      );
    }

    return ValidationResult.valid();
  }
}
