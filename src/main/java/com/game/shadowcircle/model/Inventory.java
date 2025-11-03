package com.game.shadowcircle.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Inventory {

  private List<Item> items = new ArrayList<>();

  public void addItem(Item item) {
    if (item != null) {
      items.add(item);
    }
  }

  public void useItem(Item item, Player player) {
    if (items.contains(item)) {
      item.use(player);
      items.remove(item);
    }
  }

  public boolean hasItem(String itemName) {
    return items.stream().anyMatch(i -> i.getName().equalsIgnoreCase(itemName));
  }
}
