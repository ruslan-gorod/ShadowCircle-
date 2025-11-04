package com.game.shadowcircle.events;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class GameLoadedEvent extends GameEvent {

  private String saveFileName;
  private LocalDateTime originalSaveTime;

  public GameLoadedEvent(String saveFileName, LocalDateTime originalSaveTime) {
    super();
    this.setType("GAME_LOADED");
    this.setMessage("Game loaded: " + saveFileName);
    this.setTimestamp(LocalDateTime.now());
    this.saveFileName = saveFileName;
    this.originalSaveTime = originalSaveTime;
  }
}