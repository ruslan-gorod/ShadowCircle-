package com.game.shadowcircle.template;

import com.game.shadowcircle.model.GameContext;
import java.util.Random;

public class InfiltrationMission extends AbstractMission {

  private final Random random = new Random();

  @Override
  protected MissionResult executeMissionLogic(GameContext context) {
    // Специфічна логіка інфільтрації
    int suspicionGain = calculateBaseSuspicion(context);
    context.setSuspicionLevel(context.getSuspicionLevel() + suspicionGain);

    if (context.getSuspicionLevel() > getSuspicionThreshold()) {
      return MissionResult.failure("Вас викрили!");
    }

    return MissionResult.success(calculateReward(context));
  }

  private int calculateBaseSuspicion(GameContext context) {
    int baseSuspicion = 10;
    int stealthModifier = Math.max(0, 50 - context.getPlayer().getStealth()) / 10;
    int randomFactor = random.nextInt(11); // 0-10

    return baseSuspicion + stealthModifier + randomFactor;
  }

  private int getSuspicionThreshold() {
    return 80;
  }

  private int calculateReward(GameContext context) {
    int baseReward = 100;
    int stealthBonus = context.getPlayer().getStealth() / 5;
    return baseReward + stealthBonus;
  }
}