package com.game.shadowcircle.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Player {

  private String name;
  private int health = 100;
  private int suspicionLevel = 0;
  private int stealth = 10;
  private int score = 0;
  private int intelligence = 10;
  private Inventory inventory = new Inventory();
  private boolean alive = true;

  public void increaseSuspicion(int amount) {
    suspicionLevel = Math.min(100, suspicionLevel + amount);
  }

  public void takeDamage(int dmg) {
    health = Math.max(0, health - dmg);
    if (health == 0) {
      alive = false;
    }
  }

  public void gainScore(int points) {
    score += points;
    if (inventory != null) {
      inventory.addItem(new RewardItem("Reputation +" + points, "Відзнака успіху"));
    }
  }
}
