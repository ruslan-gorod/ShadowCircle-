package com.game.shadowcircle.state;

import com.game.shadowcircle.events.GameEvent;
import com.game.shadowcircle.events.GameEventPublisher;
import com.game.shadowcircle.model.GameContext;
import com.game.shadowcircle.model.Player;
import com.game.shadowcircle.service.SaveGameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Стан виходу з гри
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ExitState implements State {

  private final GameEventPublisher eventPublisher;
  private final SaveGameService saveGameService;
  private boolean confirmationShown = false;

  @Override
  public void enter(GameContext context) {
    if (!confirmationShown) {
      System.out.println("\n╔════════════════════════════════════════╗");
      System.out.println("║           EXIT GAME                    ║");
      System.out.println("╚════════════════════════════════════════╝");

      Player player = context.getPlayer();

      // Показуємо короткий звіт
      System.out.println("\nSession results:");
      System.out.println("─────────────────────────────────────────");
      System.out.printf("  Agent: %s\n", player.getName());
      System.out.printf("  Final score: %d\n", player.getScore());
      System.out.printf("  Missions completed: %d\n",
          context.getCompletedMissions() != null ? context.getCompletedMissions().size() : 0);
      System.out.printf("  Elections made: %d\n",
          context.getChoiceHistory() != null ? context.getChoiceHistory().size() : 0);

      // Перевіряємо чи є незбережений прогрес
      if (context.getTurnNumber() > 0 && !isGameSaved()) {
        System.out.println("\nWARNING: Unsaved progress will be lost!");
      }

      System.out.println("\n─────────────────────────────────────────");
      System.out.println("Are you sure you want to exit?");
      System.out.println("\n1. Yes, exit");
      System.out.println("2. Save and exit");
      System.out.println("0. Return to the game");
      System.out.print("\nYour choice: ");

      confirmationShown = true;
    }
  }

  @Override
  public void update(GameContext context) {
    // Не потрібно оновлення
  }

  @Override
  public void exit(GameContext context) {
    log.debug("Exiting the exit state");
  }

  @Override
  public State handleInput(String input, GameContext context) {
    return switch (input.trim()) {
      case "1" -> {
        // Вихід без збереження
        performExit(context, false);
        yield null; // null означає завершення гри
      }
      case "2" -> {
        // Збереження та вихід
        performExit(context, true);
        yield null;
      }
      case "0" -> {
        // Повернення до меню
        System.out.println("\n Back to the game...");
        confirmationShown = false;
        yield new MainMenuState();
      }
      default -> {
        System.out.println("Incorrect selection. Please try again.");
        confirmationShown = false;
        yield this;
      }
    };
  }

  /**
   * Виконує вихід з гри
   */
  private void performExit(GameContext context, boolean saveFirst) {
    System.out.println();

    if (saveFirst) {
      System.out.println("Saving the game...");
      try {
        // TODO Тут має бути виклик сервісу збереження
        saveGameService.save(context);
        System.out.println("Game saved!");

        // Публікуємо подію збереження
        eventPublisher.publishEvent(
            GameEvent.of("GAME_SAVED", "Game saved before exit")
        );

      } catch (Exception e) {
        System.out.println("Saving error: " + e.getMessage());
        log.error("Error saving game", e);
      }
    }

    // Показуємо прощальне повідомлення
    displayFarewellMessage(context);

    // Публікуємо подію завершення гри
    eventPublisher.publishEvent(
        GameEvent.of("GAME_ENDED",
            String.format("Player %s ended the session with a score %d",
                context.getPlayer().getName(),
                context.getPlayer().getScore()))
    );

    log.info("Player {} finished the game. Score: {}",
        context.getPlayer().getName(),
        context.getPlayer().getScore());
  }

  /**
   * Відображає прощальне повідомлення
   */
  private void displayFarewellMessage(GameContext context) {
    System.out.println("\n╔════════════════════════════════════════╗");
    System.out.println("║         THANK YOU FOR PLAYING!         ║");
    System.out.println("╚════════════════════════════════════════╝");

    Player player = context.getPlayer();

    // Персоналізоване повідомлення залежно від результатів
    if (player.getScore() > 1000) {
      System.out.println("\nIncredible, Agent " + player.getName() + "!");
      System.out.println(" Your achievements are worthy of legend.");
    } else if (player.getScore() > 500) {
      System.out.println("\nGreat job, Agent " + player.getName() + "!");
      System.out.println(" Keep up the good work.");
    } else if (player.getScore() > 100) {
      System.out.println("\nWell done, Agent " + player.getName() + "!");
      System.out.println(" There's work to be done.");
    } else {
      System.out.println("\nEvery great agent started small.");
      System.out.println(" Keep practicing, " + player.getName() + "!");
    }

    System.out.println("\n   Final score: " + player.getScore());

    if (context.getCompletedMissions() != null && !context.getCompletedMissions().isEmpty()) {
      System.out.println("   Missions completed: " + context.getCompletedMissions().size());
    }

    System.out.println("\n─────────────────────────────────────────");
    System.out.println("  Shadow Circle: Interactive Spy Fiction");
    System.out.println("─────────────────────────────────────────");
    System.out.println();
  }

  /**
   * Перевіряє чи була гра збережена
   */
  private boolean isGameSaved() {
    // Перевіряємо чи існує автозбереження
    try {
      return saveGameService.exists("autosave");
    } catch (Exception e) {
      log.error("Failed to check if game is saved", e);
      return false;
    }
  }
}