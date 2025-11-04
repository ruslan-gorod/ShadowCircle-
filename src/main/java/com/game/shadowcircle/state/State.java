package com.game.shadowcircle.state;

import com.game.shadowcircle.model.GameContext;

public interface State {

  void enter(GameContext context);

  void update(GameContext context);

  void exit(GameContext context);

  State handleInput(String input, GameContext context);
}