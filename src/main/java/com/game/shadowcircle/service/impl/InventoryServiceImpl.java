package com.game.shadowcircle.service.impl;

import com.game.shadowcircle.model.Item;
import com.game.shadowcircle.model.Player;
import com.game.shadowcircle.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InventoryServiceImpl implements InventoryService {

  @Override
  public void addItem(Player player, Item item) {
    if (player.getInventory() == null) {
      player.setInventory(new com.game.shadowcircle.model.Inventory());
    }
    player.getInventory().addItem(item);
    log.info("Додано предмет '{}' гравцю '{}'", item.getName(), player.getName());
  }

  @Override
  public void removeItem(Player player, Item item) {
    if (player.getInventory() != null && player.getInventory().getItems().contains(item)) {
      player.getInventory().getItems().remove(item);
      log.info("Видалено предмет '{}' у гравця '{}'", item.getName(), player.getName());
    }
  }

  @Override
  public boolean hasItem(Player player, String itemName) {
    return player.getInventory() != null && player.getInventory().hasItem(itemName);
  }

  public void useItem(Player player, Item item) {
    if (player.getInventory() != null && player.getInventory().getItems().contains(item)) {
      item.use(player);
      player.getInventory().getItems().remove(item);
      log.info("Використано предмет '{}' гравцем '{}'", item.getName(), player.getName());
    }
  }
}
