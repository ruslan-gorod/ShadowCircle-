package com.game.shadowcircle.service;

import com.game.shadowcircle.model.Item;
import com.game.shadowcircle.model.Player;

public interface InventoryService {
  void addItem(Player player, Item item);
  void removeItem(Player player, Item item);
  boolean hasItem(Player player, String itemName);
}
