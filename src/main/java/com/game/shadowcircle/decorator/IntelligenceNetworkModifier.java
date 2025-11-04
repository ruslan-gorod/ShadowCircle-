package com.game.shadowcircle.decorator;

import com.game.shadowcircle.strategy.DecisionResult;

public class IntelligenceNetworkModifier implements OutcomeModifier {

  @Override
  public DecisionResult modify(DecisionResult base) {
    return base.builder()
        .scoreGain((int) (base.getScoreGain() * 1.5))
        .message("Мережа інформаторів надала цінні дані")
        .build();
  }
}
