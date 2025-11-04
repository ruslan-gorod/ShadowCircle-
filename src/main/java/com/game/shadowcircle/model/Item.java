package com.game.shadowcircle.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public abstract class Item implements Cloneable {

  private String id;
  private String name;
  private String description;
  private ItemType type;
  private int value;
  private boolean consumable;

  public Item copy() {
    return Item.builder()
        .id(this.id)
        .name(this.name)
        .description(this.description)
        .type(this.type)
        .value(this.value)
        .consumable(this.consumable)
        .build();
  }

  public void use(Player player) {
    switch (this.type) {
      case MEDICAL -> player.setHealth(Math.min(100, player.getHealth() + 30));
      case GADGET -> player.setStealth(Math.min(100, player.getStealth() + 10));
      case DOCUMENT -> player.setIntelligence(Math.min(100, player.getIntelligence() + 5));
      default -> System.out.println("Цей предмет не можна використати");
    }
  }

  public enum ItemType {
    WEAPON,
    TOOL,
    DOCUMENT,
    GADGET,
    MEDICAL,
    KEY,
    EVIDENCE
  }
}
