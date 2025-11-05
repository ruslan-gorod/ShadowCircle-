package com.game.shadowcircle.state;

import com.game.shadowcircle.events.GameEventPublisher;
import com.game.shadowcircle.model.Choice;
import com.game.shadowcircle.model.GameContext;
import com.game.shadowcircle.model.Scene;
import java.util.List;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MissionActiveState implements State {

  private final GameEventPublisher eventPublisher;
  private Scene currentScene;

  @Override
  public void enter(GameContext context) {
    currentScene = context.getCurrentMission().getScenes().get(0);
    displayScene(currentScene, context);
  }

  @Override
  public void update(GameContext context) {
    // Перевірка таймера місії, тощо
  }

  @Override
  public void exit(GameContext context) {
    // Очищення
  }

  @Override
  public State handleInput(String input, GameContext context) {
    try {
      int choiceIndex = Integer.parseInt(input) - 1;
      List<Choice> choices = currentScene.getChoices();

      if (choiceIndex >= 0 && choiceIndex < choices.size()) {
        Choice choice = choices.get(choiceIndex);
        // Обробка вибору через GameEngine

        if (context.getPlayer().getHealth() <= 0) {
          return new GameOverState(eventPublisher);
        }

        // Перехід до наступної сцени або завершення місії
      }
    } catch (NumberFormatException e) {
      System.out.println("Невірне введення");
    }

    return this;
  }

  private void displayScene(Scene scene, GameContext context) {
    System.out.println("\n" + scene.getNarrativeText());

    List<Choice> choices = scene.getChoices();
    for (int i = 0; i < choices.size(); i++) {
      System.out.println((i + 1) + ". " + choices.get(i).getDescription());
    }
  }
}