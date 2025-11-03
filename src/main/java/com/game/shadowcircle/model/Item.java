package com.game.shadowcircle.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Item implements Cloneable {

  protected String name;
  protected String description;

  public abstract void use(Player player);

  @Override
  public Item clone() {
    try {
      return (Item) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }
  }
}
