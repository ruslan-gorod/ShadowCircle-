package com.game.shadowcircle.chain;

import com.game.shadowcircle.model.Choice;
import com.game.shadowcircle.model.GameContext;

public class ItemRequirementValidator extends ChoiceValidator {

  @Override
  public ValidationResult validate(Choice choice, GameContext context) {
    if (choice.getItemReward() != null
        && !context.getInventory().hasItem(choice.getItemReward().getName())) {
      return ValidationResult.invalid("Потрібен предмет: " + choice.getItemReward());
    }
    return ValidationResult.valid();
  }
}