package com.game.shadowcircle.chain;

import com.game.shadowcircle.model.Choice;
import com.game.shadowcircle.model.GameContext;

public class StealthRequirementValidator extends ChoiceValidator {

  @Override
  public ValidationResult validate(Choice choice, GameContext context) {
    if (choice.getStealthRequirement() > context.getPlayer().getStealth()) {
      return ValidationResult.invalid("Недостатньо скритності");
    }
    return ValidationResult.valid();
  }
}