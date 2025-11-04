package com.game.shadowcircle.state;

import com.game.shadowcircle.model.GameContext;

public interface GameState {

  void enter(GameContext context);

  void update(GameContext context);

  void exit(GameContext context);

  GameState handleInput(String input, GameContext context);
}