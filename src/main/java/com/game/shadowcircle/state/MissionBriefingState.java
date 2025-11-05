package com.game.shadowcircle.state;

import com.game.shadowcircle.events.GameEventPublisher;
import com.game.shadowcircle.model.GameContext;
import com.game.shadowcircle.model.Mission;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MissionBriefingState implements State {

  private final GameEventPublisher eventPublisher;

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
            "Exit from mission briefing state", null, LocalDateTime.now(), 0));
  }

  @Override
  public State handleInput(String input, GameContext context) {
    Mission selected = selectMission(input, context);
    if (selected != null) {
      context.setCurrentMission(selected);
      return new MissionActiveState(eventPublisher, context.getCurrentScene());
    }
    return this;
  }

  private void displayAvailableMissions(GameContext context) {
    // TODO Логіка відображення доступних місій
  }

  private Mission selectMission(String input, GameContext context) {
    // TODO Логіка вибору місії
    return null;
  }
}