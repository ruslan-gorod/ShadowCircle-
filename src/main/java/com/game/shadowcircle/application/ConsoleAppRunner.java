package com.game.shadowcircle.application;

import com.game.shadowcircle.engine.GameEngine;
import com.game.shadowcircle.events.DefaultGameEventPublisher;
import com.game.shadowcircle.service.DialogueService;
import java.util.Scanner;
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
    dialogueService.print("=== Welcome to SHADOW CIRCLE ===");

    String playerName = dialogueService.prompt("Enter agent name:");
    gameEngine.startNewGame(playerName);

    // Реєструємо слухачів подій
    eventPublisher.registerListener(event -> {
      if (event.getSeverity() >= 2) {
        log.warn("IMPORTANT EVENT: {} - {}", event.getType(), event.getMessage());
      } else {
        log.info("{}: {}", event.getType(), event.getMessage());
      }
    });

    // Основний ігровий цикл
    Scanner scanner = new Scanner(System.in);
    while (true) {
      String input = scanner.nextLine();

      if (input.equalsIgnoreCase("exit")) {
        break;
      }

      // Обробка через State Machine
      gameEngine.processInput(input);

      // Перевірка стану гри
      if (gameEngine.isGameOver()) {
        dialogueService.print("Thanks for playing!");
        break;
      }
    }
  }
}
