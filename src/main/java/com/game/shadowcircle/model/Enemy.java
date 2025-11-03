package com.game.shadowcircle.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Enemy {

  private String name;
  private int alertness;
  private int strength;
  private boolean neutralized;

  public void increaseAlertness(int amount) {
    alertness = Math.min(100, alertness + amount);
  }
}
