package com.game.shadowcircle.service.impl;

import com.game.shadowcircle.model.Choice;
import com.game.shadowcircle.model.Player;
import com.game.shadowcircle.service.ScoringService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SimpleScoringService implements ScoringService {

  @Override
  public int calculateScore(Choice choice, Player player) {
    int base = choice.getRewardPoints() - choice.getRiskLevel();
    int modifier = (player.getStealth() + player.getIntelligence()) / 20;
    int result = Math.max(0, base + modifier);

    log.debug("Обчислення очок: base={}, modifier={}, total={}", base, modifier, result);
    return result;
  }
}

