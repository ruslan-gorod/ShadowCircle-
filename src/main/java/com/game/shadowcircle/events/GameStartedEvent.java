package com.game.shadowcircle.events;

import com.game.shadowcircle.factory.Difficulty;
import com.game.shadowcircle.model.Player;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class GameStartedEvent extends GameEvent {

  private Player player;
  private Difficulty difficulty;

  public GameStartedEvent(Player player) {
    super();
    this.setType("GAME_STARTED");
    this.setMessage(
        String.format("Congratulations, Agent %s! The game has started.", player.getName()));
    this.setTimestamp(LocalDateTime.now());
    this.player = player;
  }
}