package com.game.shadowcircle.state;

import com.game.shadowcircle.model.GameContext;

public class GameOverState implements GameState {

  @Override
  public void enter(GameContext context) {
    context.getEventPublisher().publishEvent(
        new com.game.shadowcircle.events.GameEvent("GAME_OVER_ENTER", "Вхід у стан завершення гри",
            null));
  }

  @Override
  public void update(GameContext context) {
    // Оновлення логіки екрану завершення гри
  }

  @Override
  public void exit(GameContext context) {
    context.getEventPublisher().publishEvent(
        new com.game.shadowcircle.events.GameEvent("GAME_OVER_EXIT",
            "Вихід зі стану завершення гри", null));
  }

  @Override
  public GameState handleInput(String input, GameContext context) {
    if ("restart".equalsIgnoreCase(input)) {
      return new MainMenuState();
    } else if ("exit".equalsIgnoreCase(input)) {
      System.exit(0);
    }
    return this;
  }
}