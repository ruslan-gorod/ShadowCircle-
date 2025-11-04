package com.game.shadowcircle.events;

import com.game.shadowcircle.model.Item;
import com.game.shadowcircle.model.Player;
import lombok.Getter;

@Getter
public class ItemAcquiredEvent extends GameEvent {

  private final Item item;
  private final Player player;

  public ItemAcquiredEvent(Item item, Player player) {
    super("ITEM_ACQUIRED", "Отримано предмет: " + item.getName(), item);
    this.item = item;
    this.player = player;
  }
}