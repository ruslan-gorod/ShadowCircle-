package com.game.shadowcircle.state;

import com.game.shadowcircle.events.GameEventPublisher;
import com.game.shadowcircle.model.GameContext;
import com.game.shadowcircle.service.InventoryService;
import com.game.shadowcircle.service.MissionService;
import com.game.shadowcircle.service.SaveGameService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class MainMenuState implements State {

  private final MissionService missionService;
  private final InventoryService inventoryService;
  private final GameEventPublisher eventPublisher;
  private final SaveGameService saveGameService;

  @Override
  public void enter(GameContext context) {
    System.out.println("\n=== MAIN MENU ===");
    System.out.println("1. New Mission");
    System.out.println("2. Inventory");
    System.out.println("3. Statistics");
    System.out.println("4. Save Game");
    System.out.println("5. Exit");
  }

  @Override
  public void update(GameContext context) {
    // TODO Оновлення стану
  }

  @Override
  public void exit(GameContext context) {
    // TODO Очищення при виході
  }

  @Override
  public State handleInput(String input, GameContext context) {
    return switch (input.trim()) {
      case "1" -> {
        log.debug("Navigating to MissionSelectionState");
        yield new MissionSelectionState(missionService, eventPublisher);
      }
      case "2" -> {
        log.debug("Navigating to InventoryState");
        yield new InventoryState(inventoryService);
      }
      case "3" -> {
        log.debug("Navigating to StatisticsState");
        yield new StatisticsState();
      }
      case "4" -> {
        // Збереження гри
        handleSaveGame(context);
        yield this;
      }
      case "5" -> {
        // Завантаження гри
        handleLoadGame(context);
        yield this;
      }
      case "6" -> {
        // Список збережень
        handleListSaves();
        yield this;
      }
      case "0" -> {
        log.debug("Navigating to ExitState");
        yield new ExitState(eventPublisher, saveGameService);
      }
      default -> {
        System.out.println("Invalid menu choice. Try again.");
        log.debug("Invalid menu choice: {}", input);
        yield this;
      }
    };
  }

  /**
   * Обробка збереження гри
   */
  private void handleSaveGame(GameContext context) {
    System.out.println("\n💾 Saving the game...");

    try {
      saveGameService.save(context);
      System.out.println("Game saved successfully!");
      log.info("Game saved for player: {}", context.getPlayer().getName());

    } catch (Exception e) {
      System.out.println("Failed to save: " + e.getMessage());
      log.error("Failed to save game", e);
    }

    System.out.println("\nPress Enter to continue...");
  }

  /**
   * Обробка завантаження гри
   */
  private void handleLoadGame(GameContext context) {
    System.out.println("\nLoading game...");

    try {
      GameContext loadedContext = saveGameService.load();

      if (loadedContext == null) {
        System.out.println("No save file found.");
        log.warn("Attempted to load non-existent save");
      } else {
        // Копіюємо дані з завантаженого контексту
        context.setPlayer(loadedContext.getPlayer());
        context.setCurrentMission(loadedContext.getCurrentMission());
        context.setCompletedMissions(loadedContext.getCompletedMissions());
        context.setSuspicionLevel(loadedContext.getSuspicionLevel());
        context.setCoverIntegrity(loadedContext.getCoverIntegrity());

        System.out.println("Game loaded successfully!");
        System.out.printf("   Player: %s\n", context.getPlayer().getName());
        System.out.printf("   Score: %d\n", context.getPlayer().getScore());

        log.info("Game loaded for player: {}", context.getPlayer().getName());
      }

    } catch (Exception e) {
      System.out.println("Failed to load: " + e.getMessage());
      log.error("Failed to load game", e);
    }

    System.out.println("\nPress Enter to continue...");
  }

  /**
   * Відображення списку збережень
   */
  private void handleListSaves() {
    System.out.println("\nAvailable saves:");
    System.out.println("─────────────────────────────────────────");

    try {
      var saves = saveGameService.listSaves();

      if (saves.isEmpty()) {
        System.out.println("No saves found.");
        log.debug("No save files found");
      } else {
        for (int i = 0; i < saves.size(); i++) {
          System.out.printf("%d. %s\n", i + 1, saves.get(i));
        }
        log.debug("Listed {} save file(s)", saves.size());
      }

    } catch (Exception e) {
      System.out.println("Failed to list saves: " + e.getMessage());
      log.error("Failed to list saves", e);
    }

    System.out.println("\nPress Enter to continue...");
  }
}
