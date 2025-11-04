package com.game.shadowcircle.chain;

import com.game.shadowcircle.model.Choice;
import com.game.shadowcircle.model.GameContext;

abstract class ChoiceValidator {

  public abstract ValidationResult validate(Choice choice, GameContext context);
}