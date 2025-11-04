package com.game.shadowcircle.strategy;

import com.game.shadowcircle.model.Choice;
import com.game.shadowcircle.model.DecisionResult;
import com.game.shadowcircle.model.GameContext;
import com.game.shadowcircle.model.Player;
import java.util.Random;

public class DiplomaticDecisionStrategy implements DecisionStrategy {

  private final Random random = new Random();

  @Override
  public DecisionResult evaluate(Choice choice, Player player, GameContext context) {
    int intelligenceBonus = player.getIntelligence();
    int successChance = 40 + (intelligenceBonus / 2);
    boolean success = random.nextInt(100) < successChance;

    return DecisionResult.builder()
        .success(success)
        .scoreGain(success ? choice.getRewardPoints() : choice.getRewardPoints() / 2)
        .riskPenalty(choice.getRiskLevel() / 2)
        .suspicionIncrease(success ? 10 : 20)
        .healthChange(0)
        .message(success ?
            "Переговори пройшли успішно" :
            "Не вдалося домовитися")
        .build();
  }
}