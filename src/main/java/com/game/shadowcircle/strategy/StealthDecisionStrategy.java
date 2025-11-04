package com.game.shadowcircle.strategy;

import com.game.shadowcircle.model.Choice;
import com.game.shadowcircle.model.DecisionResult;
import com.game.shadowcircle.model.GameContext;
import com.game.shadowcircle.model.Player;
import java.util.Random;

public class StealthDecisionStrategy implements DecisionStrategy {

  private final Random random = new Random();

  @Override
  public DecisionResult evaluate(Choice choice, Player player, GameContext context) {
    int stealthBonus = player.getStealth();
    int successChance = 50 + (stealthBonus / 2) - choice.getRiskLevel();
    boolean success = random.nextInt(100) < successChance;

    return DecisionResult.builder()
        .success(success)
        .scoreGain(success ? choice.getRewardPoints() : 0)
        .riskPenalty(success ? 0 : choice.getRiskLevel())
        .suspicionIncrease(success ? 5 : 25)
        .healthChange(success ? 0 : -10)
        .message(success ?
            "Ви непомітно виконали завдання" :
            "Вас майже помітили!")
        .build();
  }
}