package com.game.shadowcircle.state;

import com.game.shadowcircle.model.GameContext;
import java.time.LocalDateTime;

public class GameOverState implements State {

  @Override
  public void enter(GameContext context) {
    System.out.println("\n=== GAME COMPLETED ===");
    System.out.println("Final score: " + context.getPlayer().getScore());

    if (!context.getPlayer().isAlive()) {
      System.out.println("You died...");
    } else if (!context.isCoverIntact()) {
      System.out.println("Your legend has been exposed!");
    }
  }

  @Override
  public void update(GameContext context) {
    // TODO Оновлення логіки екрану завершення гри
  }

  @Override
  public void exit(GameContext context) {
    context.getEventPublisher().publishEvent(
        new com.game.shadowcircle.events.GameEvent("GAME_OVER_EXIT",
            "Вихід зі стану завершення гри", null, LocalDateTime.now(), 0));
  }

  @Override
  public State handleInput(String input, GameContext context) {
    return new MainMenuState();
  }
}