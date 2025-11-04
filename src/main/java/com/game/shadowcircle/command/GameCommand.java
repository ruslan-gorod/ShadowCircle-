package com.game.shadowcircle.command;

import com.game.shadowcircle.model.GameContext;

public interface GameCommand {

  void execute(GameContext context);

  void undo(GameContext context);

  boolean canExecute(GameContext context);
}