package com.game.shadowcircle.state;

import com.game.shadowcircle.model.GameContext;
import com.game.shadowcircle.model.Inventory;
import com.game.shadowcircle.model.Item;
import com.game.shadowcircle.model.Player;
import com.game.shadowcircle.service.InventoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Стан перегляду інвентаря
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryState implements State {

  private final InventoryService inventoryService;

  @Override
  public void enter(GameContext context) {
    System.out.println("\n=== INVENTORY ===");

    Player player = context.getPlayer();
    Inventory inventory = player.getInventory();

    if (inventory == null || inventory.getItems() == null || inventory.getItems().isEmpty()) {
      System.out.println(" Inventory is empty");
      System.out.println("\n0. Return to main menu");
      return;
    }

    List<Item> items = inventory.getItems();

    System.out.printf("Items in inventory: %d\n\n", items.size());

    // Відображаємо предмети
    for (int i = 0; i < items.size(); i++) {
      Item item = items.get(i);
      System.out.printf("%d. %s\n", i + 1, item.getName());

      if (item.getDescription() != null) {
        System.out.printf("   Description: %s\n", item.getDescription());
      }

      // Показуємо тип предмету якщо є
      if (item.getType() != null) {
        System.out.printf("   Type: %s\n", item.getType());
      }

      System.out.println();
    }

    System.out.println("─────────────────────────");
    System.out.println("Enter the item number to use");
    System.out.println("0. Return to main menu");
    System.out.print("\nYour choice: ");
  }

  @Override
  public void update(GameContext context) {
    // TODO Додати автоматичне оновлення інвентаря
  }

  @Override
  public void exit(GameContext context) {
    log.debug("Exiting inventory state");
  }

  @Override
  public State handleInput(String input, GameContext context) {
    try {
      int choice = Integer.parseInt(input.trim());

      // Повернення до меню
      if (choice == 0) {
        return new MainMenuState();
      }

      Player player = context.getPlayer();
      Inventory inventory = player.getInventory();

      if (inventory == null || inventory.getItems() == null) {
        System.out.println("Inventory is empty.");
        return new MainMenuState();
      }

      List<Item> items = inventory.getItems();

      // Перевірка валідності вибору
      if (choice < 1 || choice > items.size()) {
        System.out.println(" Invalid item number. Try again.");
        return this;
      }

      // Використовуємо предмет
      Item selectedItem = items.get(choice - 1);

      System.out.printf("\nUsing: %s\n", selectedItem.getName());

      // Застосовуємо ефект предмету
      try {
        inventoryService.useItem(player, selectedItem);
        System.out.println("Item used successfully!");

        // Показуємо зміни статусу
        displayPlayerStatus(player);

      } catch (Exception e) {
        System.out.println("Failed to use item: " + e.getMessage());
        log.error("Error using item", e);
      }

      // Залишаємось в інвентарі для подальших дій
      System.out.println("\nPress Enter to continue...");

      return this;

    } catch (NumberFormatException e) {
      System.out.println("Enter a number. Try again.");
      return this;
    }
  }

  /**
   * Відображає поточний статус гравця
   */
  private void displayPlayerStatus(Player player) {
    System.out.println("\nCurrent status:");
    System.out.printf(" Health: %d/100\n", player.getHealth());
    System.out.printf(" Stealth: %d\n", player.getStealth());
    System.out.printf(" Intelligence: %d\n", player.getIntelligence());
    System.out.printf(" Score: %d\n", player.getScore());
  }
}