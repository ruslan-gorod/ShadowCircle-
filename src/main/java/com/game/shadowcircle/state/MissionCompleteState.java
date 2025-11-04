package com.game.shadowcircle.state;

import com.game.shadowcircle.model.GameContext;

public class MissionCompleteState implements GameState {

  @Override
  public void enter(GameContext context) {
    context.getEventPublisher().publishEvent(
        new com.game.shadowcircle.events.GameEvent("MISSION_COMPLETE_ENTER",
            "Місію завершено успішно!", null));
  }

  @Override
  public void update(GameContext context) {
    // Оновлення логіки завершеної місії
  }

  @Override
  public void exit(GameContext context) {
    context.getEventPublisher().publishEvent(
        new com.game.shadowcircle.events.GameEvent("MISSION_COMPLETE_EXIT",
            "Вихід зі стану завершеної місії", null));
  }

  @Override
  public GameState handleInput(String input, GameContext context) {
    if ("continue".equalsIgnoreCase(input)) {
      return new MissionBriefingState();
    } else if ("menu".equalsIgnoreCase(input)) {
      return new MainMenuState();
    }
    return this;
  }
}