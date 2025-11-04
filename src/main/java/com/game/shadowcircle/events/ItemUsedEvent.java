package com.game.shadowcircle.events;

import com.game.shadowcircle.model.Item;
import com.game.shadowcircle.model.Player;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ItemUsedEvent extends GameEvent {

  private Item item;
  private Player player;
  private String effect;

  public ItemUsedEvent(Item item, Player player, String effect) {
    super();
    this.setType("ITEM_USED");
    this.setMessage(String.format("Used: %s. Effect: %s", item.getName(), effect));
    this.setTimestamp(LocalDateTime.now());
    this.item = item;
    this.player = player;
    this.effect = effect;
  }
}
