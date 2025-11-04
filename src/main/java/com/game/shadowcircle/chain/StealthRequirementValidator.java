package com.game.shadowcircle.chain;

import com.game.shadowcircle.model.Choice;
import com.game.shadowcircle.model.GameContext;
import com.game.shadowcircle.model.ValidationResult;

public class StealthRequirementValidator extends ChoiceValidator {

  @Override
  public ValidationResult validate(Choice choice, GameContext context) {
    if (choice.getStealthRequirement() > context.getPlayer().getStealth()) {
      return ValidationResult.invalid("Insufficient secrecy");
    }
    return ValidationResult.valid();
  }
}