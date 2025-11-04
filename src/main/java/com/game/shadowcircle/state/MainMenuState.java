package com.game.shadowcircle.state;

import com.game.shadowcircle.model.GameContext;

public class MainMenuState implements GameState {

  @Override
  public void enter(GameContext context) {
    context.getEventPublisher().publishEvent(
        new com.game.shadowcircle.events.GameEvent("MAIN_MENU_ENTER", "Вхід у головне меню", null));
  }

  @Override
  public void update(GameContext context) {
    // Оновлення логіки головного меню
  }

  @Override
  public void exit(GameContext context) {
    context.getEventPublisher().publishEvent(
        new com.game.shadowcircle.events.GameEvent("MAIN_MENU_EXIT", "Вихід з головного меню",
            null));
  }

  @Override
  public GameState handleInput(String input, GameContext context) {
    if ("start".equalsIgnoreCase(input)) {
      return new com.game.shadowcircle.state.PlayingState();
    } else if ("exit".equalsIgnoreCase(input)) {
      return new com.game.shadowcircle.state.GameOverState();
    }
    return this;
  }
}