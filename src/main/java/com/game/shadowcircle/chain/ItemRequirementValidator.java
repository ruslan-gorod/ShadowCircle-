package com.game.shadowcircle.chain;

import com.game.shadowcircle.model.Choice;
import com.game.shadowcircle.model.GameContext;
import com.game.shadowcircle.model.ValidationResult;

public class ItemRequirementValidator extends ChoiceValidator {

  @Override
  public ValidationResult validate(Choice choice, GameContext context) {
    if (choice.getItemReward() != null
        && !context.getInventory().hasItem(choice.getItemReward().getName())) {
      return ValidationResult.invalid("Required item: " + choice.getItemReward());
    }
    return ValidationResult.valid();
  }
}