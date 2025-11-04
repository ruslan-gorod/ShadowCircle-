package com.game.shadowcircle.events;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class GameSavedEvent extends GameEvent {

  private String saveFileName;
  private LocalDateTime saveTime;

  public GameSavedEvent(String saveFileName) {
    super();
    this.setType("GAME_SAVED");
    this.setMessage("Game saved: " + saveFileName);
    this.setTimestamp(LocalDateTime.now());
    this.saveFileName = saveFileName;
    this.saveTime = LocalDateTime.now();
  }
}
