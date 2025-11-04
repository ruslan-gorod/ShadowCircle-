package com.game.shadowcircle.strategy;

import com.game.shadowcircle.model.Choice;
import com.game.shadowcircle.model.GameContext;
import com.game.shadowcircle.model.Player;

public interface DecisionStrategy {

  DecisionResult evaluate(Choice choice, Player player, GameContext context);
}