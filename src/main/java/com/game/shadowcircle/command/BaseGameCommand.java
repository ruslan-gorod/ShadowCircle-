package com.game.shadowcircle.command;

import com.game.shadowcircle.model.GameContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
abstract class BaseGameCommand implements GameCommand {

  protected final GameContext context;

  @Override
  public boolean canExecute() {
    return context.getPlayer().isAlive() &&
        context.isCoverIntact();
  }
}