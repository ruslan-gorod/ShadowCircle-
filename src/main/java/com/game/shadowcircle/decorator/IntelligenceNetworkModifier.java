package com.game.shadowcircle.decorator;

import com.game.shadowcircle.model.DecisionResult;

public class IntelligenceNetworkModifier implements OutcomeModifier {

  @Override
  public DecisionResult modify(DecisionResult base) {
    return base.builder()
        .scoreGain((int) (base.getScoreGain() * 1.5))
        .message("The network of informants provided valuable data")
        .build();
  }
}
