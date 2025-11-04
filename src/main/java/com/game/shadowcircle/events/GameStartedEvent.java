package com.game.shadowcircle.events;

import com.game.shadowcircle.model.Player;
import lombok.Getter;

@Getter
public class GameStartedEvent extends GameEvent {

  private final Player player;

  public GameStartedEvent(Player player) {
    super("GAME_STARTED", "Гра розпочата для гравця: " + player.getName(), player);
    this.player = player;
  }
}