package com.game.shadowcircle.strategy;

import com.game.shadowcircle.model.Choice;
import com.game.shadowcircle.model.GameContext;
import com.game.shadowcircle.model.Player;
import java.util.Random;

public class AggressiveDecisionStrategy implements DecisionStrategy {

  private final Random random = new Random();

  @Override
  public DecisionResult evaluate(Choice choice, Player player, GameContext context) {
    boolean success = random.nextInt(100) < 60;

    return DecisionResult.builder()
        .success(success)
        .scoreGain(success ? choice.getRewardPoints() + 20 : 0)
        .riskPenalty(choice.getRiskLevel())
        .suspicionIncrease(success ? 30 : 50)
        .healthChange(success ? -20 : -40)
        .message(success ?
            "Швидке й рішуче виконання!" :
            "Агресивний підхід призвів до проблем")
        .build();
  }
}