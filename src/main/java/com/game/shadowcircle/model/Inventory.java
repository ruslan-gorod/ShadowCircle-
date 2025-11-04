package com.game.shadowcircle.model;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

  @Builder.Default
  private List<Item> items = new ArrayList<>();
  @Builder.Default
  private int maxCapacity = 20;

  public void addItem(Item item) {
    if (items.size() < maxCapacity) {
      items.add(item);
    } else {
      throw new IllegalStateException("Inventory is full!");
    }
  }

  public boolean removeItem(Item item) {
    return items.remove(item);
  }

  public boolean hasItem(String itemName) {
    return items.stream()
        .anyMatch(item -> item.getName().equalsIgnoreCase(itemName));
  }

  public Item getItem(String itemName) {
    return items.stream()
        .filter(item -> item.getName().equalsIgnoreCase(itemName))
        .findFirst()
        .orElse(null);
  }

  public int getItemCount() {
    return items.size();
  }

  public boolean isFull() {
    return items.size() >= maxCapacity;
  }

  public Inventory copy() {
    Inventory copy = new Inventory();

    if (this.items != null) {
      List<Item> itemsCopy = new ArrayList<>();
      for (Item item : this.items) {
        itemsCopy.add(item.copy());
      }
      copy.setItems(itemsCopy);
    }

    return copy;
  }
}