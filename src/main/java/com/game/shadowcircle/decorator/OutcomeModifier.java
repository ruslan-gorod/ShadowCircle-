package com.game.shadowcircle.decorator;

import com.game.shadowcircle.model.DecisionResult;

public interface OutcomeModifier {

  DecisionResult modify(DecisionResult base);
}
