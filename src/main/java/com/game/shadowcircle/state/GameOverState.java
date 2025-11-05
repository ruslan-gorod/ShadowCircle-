package com.game.shadowcircle.state;

import com.game.shadowcircle.events.GameEventPublisher;
import com.game.shadowcircle.model.GameContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Стан завершення гри (Game Over)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GameOverState implements State {

  private final GameEventPublisher eventPublisher;

  @Override
  public void enter(GameContext context) {
    log.info("Entering Game Over state");

    System.out.println("\n╔════════════════════════════════════════╗");
    System.out.println("║           GAME COMPLETED               ║");
    System.out.println("╚════════════════════════════════════════╝");

    if (context == null || context.getPlayer() == null) {
      System.out.println("\nError: Game context missing");
      return;
    }

    // Визначаємо причину завершення
    String reason;

    if (!context.getPlayer().isAlive()) {
      reason = "You died in battle";
    } else if (!context.isCoverIntact()) {
      reason = "Your legend has been exposed";
    } else {
      reason = "Mission completed";
    }

    System.out.println("\n" + reason);
    System.out.println("\n─────────────────────────────────────────");

    // Статистика
    System.out.println("\nSUMMARY STATISTICS:");
    System.out.printf(" Agent: %s\n", context.getPlayer().getName());
    System.out.printf(" Final Score: %d\n", context.getPlayer().getScore());
    System.out.printf(" Remaining Health: %d/100\n", context.getPlayer().getHealth());

    if (context.getCompletedMissions() != null && !context.getCompletedMissions().isEmpty()) {
      System.out.printf(" Missions Completed: %d\n", context.getCompletedMissions().size());
      System.out.println("\n List of Missions Completed:");
      for (String mission : context.getCompletedMissions()) {
        System.out.println("     ✓ " + mission);
      }
    } else {
      System.out.println(" Missions Completed: 0");
    }

    if (context.getChoiceHistory() != null) {
      System.out.printf(" Choices Made: %d\n", context.getChoiceHistory().size());
    }

    // Оцінка продуктивності
    System.out.println("\n🏆 PERFORMANCE EVALUATION:");
    String performance = evaluatePerformance(context.getPlayer().getScore());
    System.out.println(" " + performance);

    System.out.println("\n─────────────────────────────────────────");
    System.out.println("\nPress Enter to return to the main menu...");
  }

  @Override
  public void update(GameContext context) {
    // Немає необхідності оновлювати стан Game Over
  }

  @Override
  public void exit(GameContext context) {
    log.debug("Exiting Game Over");
  }

  @Override
  public State handleInput(String input, GameContext context) {
    // Будь-яке введення повертає до головного меню
    log.debug("Return to main menu after Game Over");

    // Можна скинути деякі параметри контексту
    if (context != null) {
      context.setSuspicionLevel(0);
      context.setCoverIntegrity(100);
      context.setCurrentMission(null);
      context.setCurrentScene(null);
    }

    return new MainMenuState();
  }

  /**
   * Оцінює продуктивність гравця
   */
  private String evaluatePerformance(int score) {
    if (score >= 2000) {
      return " LEGENDARY! You are a true master of espionage!";
    } else if (score >= 1500) {
      return " EXCELLENT! Your skills are impressive!";
    } else if (score >= 1000) {
      return " GOOD! You are an experienced agent!";
    } else if (score >= 500) {
      return " SATISFACTORY. There is room for improvement.";
    } else if (score >= 100) {
      return " BEGINNER LEVEL. Keep practicing!";
    } else {
      return "Need more practice. Don't give up!";
    }
  }
}