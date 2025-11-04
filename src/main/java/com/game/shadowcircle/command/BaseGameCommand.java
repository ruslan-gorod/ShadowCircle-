package com.game.shadowcircle.command;

import com.game.shadowcircle.model.GameContext;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@RequiredArgsConstructor
public abstract class BaseGameCommand implements GameCommand {

  private final GameContext context;

  @Override
  public boolean canExecute(GameContext context) {
    return context.getPlayer().isAlive() &&
        context.isCoverIntact();
  }
}