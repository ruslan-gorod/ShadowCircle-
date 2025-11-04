package com.game.shadowcircle.strategy;

import com.game.shadowcircle.model.Choice;
import com.game.shadowcircle.model.GameContext;
import com.game.shadowcircle.model.Player;
import java.util.Random;

public class TechnicalDecisionStrategy implements DecisionStrategy {

  private final Random random = new Random();

  @Override
  public DecisionResult evaluate(Choice choice, Player player, GameContext context) {
    boolean hasTools = context.getInventory().hasItem("tech_tools");
    int successChance = hasTools ? 70 : 45;
    boolean success = random.nextInt(100) < successChance;

    return DecisionResult.builder()
        .success(success)
        .scoreGain(success ? choice.getRewardPoints() + 10 : 0)
        .riskPenalty(choice.getRiskLevel())
        .suspicionIncrease(success ? 15 : 35)
        .healthChange(0)
        .message(success ?
            "Технічні засоби допомогли" :
            "Обладнання підвело")
        .build();
  }
}
