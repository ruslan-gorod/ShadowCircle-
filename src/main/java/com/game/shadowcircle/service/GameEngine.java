package com.game.shadowcircle.service;

import com.game.shadowcircle.model.Choice;
import com.game.shadowcircle.model.GameState;
import com.game.shadowcircle.model.Scene;

public interface GameEngine {

  void startNewGame(String playerName);

  void loadGame();

  void saveGame();

  void playScene(Scene scene);

  void applyChoice(Choice choice);

  GameState getCurrentState();
}
