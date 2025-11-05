package com.game.shadowcircle.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
  private boolean gameOver;

  public boolean isGameOver() {
    return player != null && !player.isAlive();
  }

  public GameState copy() {
    GameState copy = new GameState();

    // Копіюємо гравця
    if (this.player != null) {
      copy.setPlayer(this.player.copy());
    }

    // Місія копіюється за посиланням (не потребує глибокого копіювання)
    copy.setCurrentMission(this.currentMission);

    // Копіюємо список завершених місій
    if (this.completedMissions != null) {
      copy.setCompletedMissions(new ArrayList<>(this.completedMissions));
    }

    copy.setTotalScore(this.totalScore);
    copy.setLastSaved(this.lastSaved);
    copy.setGameOver(this.gameOver);

    return copy;
  }

  public boolean isActive() {
    return !gameOver && player != null && player.isAlive();
  }

  /**
   * Перевіряє чи є активна місія
   */
  public boolean hasActiveMission() {
    return currentMission != null && !currentMission.isCompleted();
  }

  /**
   * Отримує відсоток прогресу місії
   */
  public double getMissionProgress() {
    if (currentMission == null || currentMission.getScenes() == null) {
      return 0.0;
    }

    // TODO Додати більш складну логіку підрахунку прогресу
    return 0.0;
  }
}
