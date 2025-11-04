package com.game.shadowcircle.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GadgetItem extends Item {

  public GadgetItem(String name, String description) {
    this.setName(name);
    this.setDescription(description);
  }

  @Override
  public void use(Player player) {
    // Можна підвищити stealth або знизити suspicion
    player.setStealth(player.getStealth() + 5);
    player.setSuspicionLevel(Math.max(0, player.getSuspicionLevel() - 5));
  }
}
