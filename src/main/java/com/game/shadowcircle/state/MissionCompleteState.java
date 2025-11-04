package com.game.shadowcircle.state;

import com.game.shadowcircle.model.GameContext;
import java.time.LocalDateTime;

public class MissionCompleteState implements State {

  @Override
  public void enter(GameContext context) {
    context.getEventPublisher().publishEvent(
        new com.game.shadowcircle.events.GameEvent("MISSION_COMPLETE_ENTER",
            "Mission completed successfully!", null, LocalDateTime.now(), 0));
  }

  @Override
  public void update(GameContext context) {
    // Оновлення логіки завершеної місії
  }

  @Override
  public void exit(GameContext context) {
    context.getEventPublisher().publishEvent(
        new com.game.shadowcircle.events.GameEvent("MISSION_COMPLETE_EXIT",
            "Exiting mission completed state", null, LocalDateTime.now(), 0));
  }

  @Override
  public State handleInput(String input, GameContext context) {
    if ("continue".equalsIgnoreCase(input)) {
      return new MissionBriefingState();
    } else if ("menu".equalsIgnoreCase(input)) {
      return new MainMenuState();
    }
    return this;
  }
}