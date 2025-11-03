package com.game.shadowcircle.model;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameState {

  private Player player;
  private Mission currentMission;
  private List<String> completedMissions;
  private int totalScore;
  private LocalDateTime lastSaved;

  public boolean isGameOver() {
    return player != null && !player.isAlive();
  }
}
