package com.game.shadowcircle.service;

import com.game.shadowcircle.model.Player;

public interface PlayerService {

  Player createPlayer(String name);

  Player getCurrentPlayer();

  void updatePlayer(Player player);

  void addScore(int points);

  void applyRisk(int risk);
}
