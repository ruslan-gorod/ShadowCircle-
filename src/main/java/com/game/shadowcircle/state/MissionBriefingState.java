package com.game.shadowcircle.state;

import com.game.shadowcircle.model.GameContext;
import com.game.shadowcircle.model.Mission;

public class MissionBriefingState implements GameState {

  @Override
  public void enter(GameContext context) {
    displayAvailableMissions(context);
  }

  @Override
  public void update(GameContext context) {
    // Оновлення логіки брифінгу місій
  }

  @Override
  public void exit(GameContext context) {
    context.getEventPublisher().publishEvent(
        new com.game.shadowcircle.events.GameEvent("MISSION_BRIEFING_EXIT",
            "Вихід зі стану брифінгу місій", null));
  }

  @Override
  public GameState handleInput(String input, GameContext context) {
    Mission selected = selectMission(input, context);
    if (selected != null) {
      context.setCurrentMission(selected);
      return new MissionActiveState();
    }
    return this;
  }

  private void displayAvailableMissions(GameContext context) {
    // Логіка відображення доступних місій
  }

  private Mission selectMission(String input, GameContext context) {
    // Логіка вибору місії
    return null;
  }
}