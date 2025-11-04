package com.game.shadowcircle.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RewardItem extends Item {

  public RewardItem(String name, String description) {
    this.setName(name);
    this.setDescription(description);
  }

  @Override
  public void use(Player player) {
    // Додаємо очки гравцю
    player.setScore(player.getScore() + 10);
  }
}
