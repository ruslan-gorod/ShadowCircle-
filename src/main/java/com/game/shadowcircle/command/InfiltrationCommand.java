package com.game.shadowcircle.command;

import com.game.shadowcircle.model.GameContext;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class InfiltrationCommand extends BaseGameCommand {

  private final String targetId;
  private final int stealthRequirement;

  @Override
  public void execute(GameContext context) {
    // Логіка виконання інфільтрації
  }

  @Override
  public void undo(GameContext context) {
    // Логіка відміни інфільтрації
  }

  @Override
  public boolean canExecute(GameContext context) {
    return super.canExecute(context) &&
        context.getPlayer().getStealth() >= stealthRequirement;
  }
}