package com.game.shadowcircle.state;

import com.game.shadowcircle.command.GameCommand;
import com.game.shadowcircle.command.MakeChoiceCommand;
import com.game.shadowcircle.model.Choice;
import com.game.shadowcircle.model.GameContext;
import com.game.shadowcircle.model.Scene;

public class MissionActiveState implements GameState {

  private Scene currentScene;

  @Override
  public void enter(GameContext context) {
    currentScene = context.getCurrentMission().getScenes().getFirst();
    displayScene(currentScene, context);
  }

  @Override
  public void update(GameContext context) {
    // Оновлення логіки активного завдання
  }

  @Override
  public void exit(GameContext context) {
    context.getEventPublisher().publishEvent(
        new com.game.shadowcircle.events.GameEvent("MISSION_ACTIVE_EXIT",
            "Вихід зі стану активного завдання", null));
  }

  @Override
  public GameState handleInput(String input, GameContext context) {
    Choice choice = parseChoice(input, currentScene);
    if (choice == null) {
      return this; // Залишаємось у тому ж стані
    }

    GameCommand cmd = new MakeChoiceCommand(choice);
    cmd.execute(context);

    if (context.getPlayer().getHealth() <= 0) {
      return new GameOverState();
    }
    if (!context.isCoverIntact()) {
      return new GameOverState();
    }
    if (currentScene.isEndingScene()) {
      return new MissionCompleteState();
    }

    return this;
  }

  private void displayScene(Scene scene, GameContext context) {
    // Логіка відображення сцени
  }

  private Choice parseChoice(String input, Scene scene) {
    // Логіка парсингу вибору
    return null;
  }
}