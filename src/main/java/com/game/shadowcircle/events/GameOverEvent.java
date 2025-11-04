package com.game.shadowcircle.events;

import com.game.shadowcircle.model.Player;
import lombok.Getter;

@Getter
public class GameOverEvent extends GameEvent {

  private final GameOverReason reason;
  private final int finalScore;
  private final Player player;
  public GameOverEvent(GameOverReason reason, int finalScore, Player player) {
    super("GAME_OVER", "Гра завершена: " + reason, null);
    this.reason = reason;
    this.finalScore = finalScore;
    this.player = player;
  }

  public enum GameOverReason {
    DEATH, COMPROMISED, VICTORY
  }
}