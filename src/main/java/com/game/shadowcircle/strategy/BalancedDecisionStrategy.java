package com.game.shadowcircle.strategy;

import com.game.shadowcircle.model.Choice;
import com.game.shadowcircle.model.GameContext;
import com.game.shadowcircle.model.Player;
import java.util.Random;

public class BalancedDecisionStrategy implements DecisionStrategy {

  private final Random random = new Random();

  @Override
  public DecisionResult evaluate(Choice choice, Player player, GameContext context) {
    int avgStat = (player.getStealth() + player.getIntelligence()) / 2;
    int successChance = 45 + (avgStat / 3);
    boolean success = random.nextInt(100) < successChance;

    return DecisionResult.builder()
        .success(success)
        .scoreGain(success ? choice.getRewardPoints() : 0)
        .riskPenalty(success ? choice.getRiskLevel() / 2 : choice.getRiskLevel())
        .suspicionIncrease(success ? 15 : 30)
        .healthChange(success ? 0 : -15)
        .message(success ?
            "Збалансований підхід спрацював" :
            "Результат міг бути кращим")
        .build();
  }
}
