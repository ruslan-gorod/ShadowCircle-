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
  private int maxCapacity = 20;

  public void addItem(Item item) {
    if (items.size() < maxCapacity) {
      items.add(item);
    } else {
      throw new IllegalStateException("Інвентар повний!");
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
    return Inventory.builder()
        .items(new ArrayList<>(this.items))
        .maxCapacity(this.maxCapacity)
        .build();
  }
}