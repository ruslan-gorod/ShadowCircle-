package com.game.shadowcircle.decorator;

import com.game.shadowcircle.strategy.DecisionResult;

public interface OutcomeModifier {

  DecisionResult modify(DecisionResult base);
}
