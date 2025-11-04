package com.game.shadowcircle.application;

import com.game.shadowcircle.engine.GameEngine;
import com.game.shadowcircle.events.DefaultGameEventPublisher;
import com.game.shadowcircle.events.GameEvent;
import com.game.shadowcircle.model.Choice;
import com.game.shadowcircle.model.GameState;
import com.game.shadowcircle.model.Scene;
import com.game.shadowcircle.service.DialogueService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsoleAppRunner implements ApplicationRunner {

  private final GameEngine gameEngine;
  private final DialogueService dialogueService;
  private final DefaultGameEventPublisher eventPublisher;

  @Override
  public void run(ApplicationArguments args) {
    dialogueService.print("=== Ласкаво просимо в SHADOW CIRCLE ===");

    // 1. Створюємо нового гравця
    String playerName = dialogueService.prompt("Введіть ім'я агента:");
    gameEngine.startNewGame(playerName);

    // 2. Підписуємо слухача подій
    eventPublisher.registerListener(
        event -> log.info("Подія гри: {} - {}", event.getType(), event.getMessage()));

    // 3. Основний ігровий цикл
    while (true) {
      GameState state = gameEngine.getCurrentState();

      if (state.isGameOver()) {
        dialogueService.print("Гру завершено. Ви не вижили...");
        break;
      }

      Scene currentScene = state.getCurrentMission() != null
          ? state.getCurrentMission().getScenes().get(0)
          : null;

      if (currentScene == null) {
        dialogueService.print("Немає доступних місій. Гру завершено.");
        break;
      }

      gameEngine.playScene(currentScene);

      List<Choice> choices = currentScene.getChoices();
      if (choices == null || choices.isEmpty()) {
        dialogueService.print("Сцена завершена.");
        break;
      }

      String input = dialogueService.prompt("Оберіть варіант (1-" + choices.size() + "):");
      int choiceIndex;
      try {
        choiceIndex = Integer.parseInt(input) - 1;
      } catch (NumberFormatException e) {
        dialogueService.print("Невірне введення, спробуйте ще раз.");
        continue;
      }

      if (choiceIndex < 0 || choiceIndex >= choices.size()) {
        dialogueService.print("Невірний номер варіанту.");
        continue;
      }

      Choice selected = choices.get(choiceIndex);
      gameEngine.applyChoice(selected);

      // Публікуємо подію про вибір
      GameEvent event = GameEvent.builder()
          .type("CHOICE_MADE")
          .message("Гравець обрав варіант: " + selected.getDescription())
          .payload(selected)
          .build();
      eventPublisher.publishEvent(event);

      // Запит на збереження прогресу
      String saveInput = dialogueService.prompt("Зберегти гру? (y/n):");
      if (saveInput.equalsIgnoreCase("y")) {
        gameEngine.saveGame();
      }
    }

    dialogueService.print("Дякуємо за гру у SHADOW CIRCLE!");
  }
}
