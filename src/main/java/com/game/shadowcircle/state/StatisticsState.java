package com.game.shadowcircle.state;

import com.game.shadowcircle.model.GameContext;
import com.game.shadowcircle.model.Player;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Стан відображення статистики
 */
@Slf4j
@Component
public class StatisticsState implements State {

  @Override
  public void enter(GameContext context) {
    System.out.println("\n╔════════════════════════════════════════╗");
    System.out.println("║         AGENT STATISTICS               ║");
    System.out.println("╚════════════════════════════════════════╝");

    Player player = context.getPlayer();

    // Основна інформація
    System.out.println("\nBASIC INFORMATION:");
    System.out.println("─────────────────────────────────────────");
    System.out.printf(" Agent Name: %s\n", player.getName());
    System.out.printf(" Score: %d\n", player.getScore());
    System.out.printf(" Health: %d/100 %s\n",
        player.getHealth(),
        getHealthBar(player.getHealth()));

    // Характеристики
    System.out.println("\nCHARACTERISTICS:");
    System.out.println("─────────────────────────────────────────");
    System.out.printf(" Stealth: %d/100 %s\n",
        player.getStealth(),
        getStatBar(player.getStealth()));
    System.out.printf(" Intelligence: %d/100 %s\n",
        player.getIntelligence(),
        getStatBar(player.getIntelligence()));

    // Додаткові характеристики якщо є
    if (player.getCharisma() > 0) {
      System.out.printf(" Charisma: %d/100 %s\n",
          player.getCharisma(),
          getStatBar(player.getCharisma()));
    }

    // Стан прикриття
    System.out.println("\nOPERATION STATUS:");
    System.out.println("─────────────────────────────────────────");
    System.out.printf(" Suspicion: %d/100 %s\n",
        context.getSuspicionLevel(),
        getSuspicionBar(context.getSuspicionLevel()));
    System.out.printf(" Legend Integrity: %d/100 %s\n",
        context.getCoverIntegrity(),
        getStatBar(context.getCoverIntegrity()));

    // Оцінка статусу
    String status = getAgentStatus(player, context);
    System.out.printf(" Status: %s\n", status);

    // Місії
    System.out.println("\nMISSIONS:");
    System.out.println("─────────────────────────────────────────");

    List<String> completedMissions = context.getCompletedMissions();
    if (completedMissions != null && !completedMissions.isEmpty()) {
      System.out.printf(" Missions completed: %d\n", completedMissions.size());
      System.out.println(" List of completed missions:");
      for (String mission : completedMissions) {
        System.out.printf("    ✓ %s\n", mission);
      }
    } else {
      System.out.println(" Missions Completed: 0");
    }

    if (context.getCurrentMission() != null) {
      System.out.printf(" Current Mission: %s\n", context.getCurrentMission().getTitle());
    }

    // Інвентар
    System.out.println("\nINVENTORY:");
    System.out.println("─────────────────────────────────────────");
    if (player.getInventory() != null && player.getInventory().getItems() != null) {
      System.out.printf(" Items: %d\n", player.getInventory().getItems().size());
    } else {
      System.out.println(" Items: 0");
    }

    // Прогрес гри
    System.out.println("\nPROGRESS:");
    System.out.println("─────────────────────────────────────────");
    System.out.printf(" Choices made: %d\n", context.getChoiceHistory().size());
    System.out.printf(" Turns made: %d\n", context.getTurnNumber());

    // Відносини (якщо є)
    if (!context.getRelationshipLevels().isEmpty()) {
      System.out.println("\nRELATIONSHIP:");
      System.out.println("─────────────────────────────────────────");
      context.getRelationshipLevels().forEach((npc, level) -> {
        String relation = getRelationshipStatus(level);
        System.out.printf("  %s: %d (%s)\n", npc, level, relation);
      });
    }

    System.out.println("\n═════════════════════════════════════════");
    System.out.println("\n0. Return to main menu");
    System.out.print("\nYour choice: ");
  }

  @Override
  public void update(GameContext context) {
    // Можна додати live-оновлення статистики
  }

  @Override
  public void exit(GameContext context) {
    log.debug("Exiting statistics state");
  }

  @Override
  public State handleInput(String input, GameContext context) {
    if ("0".equals(input.trim())) {
      return new MainMenuState();
    }

    // Будь-яке інше введення повертає до меню
    return new MainMenuState();
  }

  /**
   * Генерує візуальну смугу здоров'я
   */
  private String getHealthBar(int health) {
    int bars = health / 10;
    StringBuilder bar = new StringBuilder("[");

    for (int i = 0; i < 10; i++) {
      if (i < bars) {
        bar.append(health > 70 ? "█" : health > 30 ? "▓" : "░");
      } else {
        bar.append(" ");
      }
    }
    bar.append("]");

    return bar.toString();
  }

  /**
   * Генерує візуальну смугу характеристики
   */
  private String getStatBar(int stat) {
    int bars = stat / 10;
    StringBuilder bar = new StringBuilder("[");

    for (int i = 0; i < 10; i++) {
      bar.append(i < bars ? "█" : "░");
    }
    bar.append("]");

    return bar.toString();
  }

  /**
   * Генерує візуальну смугу підозри
   */
  private String getSuspicionBar(int suspicion) {
    int bars = suspicion / 10;
    StringBuilder bar = new StringBuilder("[");

    for (int i = 0; i < 10; i++) {
      if (i < bars) {
        bar.append(suspicion > 70 ? "⚠" : suspicion > 40 ? "▓" : "░");
      } else {
        bar.append(" ");
      }
    }
    bar.append("]");

    return bar.toString();
  }

  /**
   * Визначає статус агента
   */
  private String getAgentStatus(Player player, GameContext context) {
    if (!player.isAlive()) {
      return "Dead";
    }

    if (!context.isCoverIntact()) {
      return "Legend Exposed";
    }

    if (context.getSuspicionLevel() > 80) {
      return "⚠Under High Suspicion";
    }

    if (player.getHealth() < 30) {
      return "Seriously Injured";
    }

    if (context.getSuspicionLevel() > 50) {
      return "Suspicious";
    }

    if (player.getScore() > 500) {
      return "Experienced Agent";
    }

    return "Normal";
  }

  /**
   * Визначає статус відносин
   */
  private String getRelationshipStatus(int level) {
    if (level >= 80) {
      return "Very friendly";
    }
    if (level >= 50) {
      return "Friendly";
    }
    if (level >= 20) {
      return "Neutral";
    }
    if (level >= -20) {
      return "Cool";
    }
    if (level >= -50) {
      return "Hostile";
    }
    return "Very hostile";
  }
}