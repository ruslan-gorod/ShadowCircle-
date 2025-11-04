package com.game.shadowcircle.state;

import com.game.shadowcircle.events.GameEventPublisher;
import com.game.shadowcircle.model.GameContext;
import com.game.shadowcircle.service.InventoryService;
import com.game.shadowcircle.service.MissionService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor(force = true)
@AllArgsConstructor
public class MainMenuState implements State {

  private final MissionService missionService;
  private final InventoryService inventoryService;
  private final GameEventPublisher eventPublisher;

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
    return switch (input) {
      case "1" -> new MissionSelectionState(missionService);
      case "2" -> new InventoryState(inventoryService);
      case "3" -> new StatisticsState();
      case "4" -> // TODO Збереження
          this;
      case "5" -> new ExitState(eventPublisher);
      default -> this;
    };
  }
}
