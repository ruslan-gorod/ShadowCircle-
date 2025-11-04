package com.game.shadowcircle.chain;

import com.game.shadowcircle.model.Choice;
import com.game.shadowcircle.model.GameContext;

class CoverIntactValidator extends ChoiceValidator {

  @Override
  public ValidationResult validate(Choice choice, GameContext context) {
    if (!context.isCoverIntact()) {
      return ValidationResult.invalid("Вашу легенду викрито! Неможливо продовжувати місію");
    }

    if (context.getSuspicionLevel() > 80) {
      return ValidationResult.warning("УВАГА: Дуже високий рівень підозри!");
    }

    return ValidationResult.valid();
  }
}
