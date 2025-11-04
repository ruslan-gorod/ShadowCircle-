package com.game.shadowcircle.events;

import com.game.shadowcircle.model.Item;
import com.game.shadowcircle.model.Player;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ItemAcquiredEvent extends GameEvent {

  private Item item;
  private Player player;

  public ItemAcquiredEvent(Item item, Player player) {
    super();
    this.setType("ITEM_ACQUIRED");
    this.setMessage(String.format("Item received: %s", item.getName()));
    this.setPayload(item);
    this.setTimestamp(LocalDateTime.now());
    this.item = item;
    this.player = player;
  }
}