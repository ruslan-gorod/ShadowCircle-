package com.game.shadowcircle.events;

import com.game.shadowcircle.model.Player;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class GameOverEvent extends GameEvent {

  private GameOverReason reason;
  private int finalScore;
  private int severity;
  private Player player;

  public GameOverEvent(GameOverReason reason, int finalScore, Player player) {
    super();
    this.setType("GAME_OVER");
    this.setMessage(String.format("Game over. Reason: %s. Final score: %d",
        reason.getDescription(), finalScore));
    this.setSeverity(3);
    this.setTimestamp(LocalDateTime.now());
    this.reason = reason;
    this.finalScore = finalScore;
    this.player = player;
  }

  public enum GameOverReason {
    DEATH("Agent Death"),
    COMPROMISED("Legend Exposed"),
    MISSION_SUCCESS("All Missions Successful"),
    ABANDONED("Player Abandoned Mission");

    private final String description;

    GameOverReason(String description) {
      this.description = description;
    }

    public String getDescription() {
      return description;
    }
  }
}