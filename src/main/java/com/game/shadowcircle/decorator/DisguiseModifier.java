package com.game.shadowcircle.decorator;

import com.game.shadowcircle.strategy.DecisionResult;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor(force = true)
@AllArgsConstructor
public class DisguiseModifier implements OutcomeModifier {

  private final String disguiseType;

  @Override
  public DecisionResult modify(DecisionResult base) {
    return base.builder()
        .suspicionIncrease(base.getSuspicionIncrease() / 2)
        .message(base.getMessage() + " [Маскування допомогло]")
        .build();
  }
}