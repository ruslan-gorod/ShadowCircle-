package com.game.shadowcircle.service;

import com.game.shadowcircle.model.Choice;
import com.game.shadowcircle.model.Player;

public interface ScoringService {

  int calculateScore(Choice choice, Player player);
}
