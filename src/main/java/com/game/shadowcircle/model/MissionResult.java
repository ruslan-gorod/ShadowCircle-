package com.game.shadowcircle.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Результат виконання місії
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MissionResult {

  private Status status;
  private String message;
  private int totalScore;
  private int baseReward;
  private int bonusReward;
  // Виконання цілей
  @Builder.Default
  private Map<String, Boolean> objectivesCompleted = new HashMap<>();
  private int objectivesCompletedCount;
  private int totalObjectivesCount;
  // Стан після місії
  private int finalSuspicionLevel;
  private int finalCoverIntegrity;
  private int finalHealth;
  // Статистика
  private int turnsCompleted;
  private int enemiesNeutralized;
  private int informationGathered;
  private int sabotageOperations;
  // Нагороди та досягнення
  @Builder.Default
  private List<Item> rewards = new ArrayList<>();
  @Builder.Default
  private List<String> achievementsUnlocked = new ArrayList<>();
  @Builder.Default
  private List<String> newMissionsUnlocked = new ArrayList<>();
  // Репутація
  @Builder.Default
  private Map<String, Integer> reputationChanges = new HashMap<>();
  // Довгострокові наслідки
  @Builder.Default
  private List<String> longTermConsequences = new ArrayList<>();
  private boolean allowsContinuation;
  private String nextMissionId;
  // Детальний звіт
  @Builder.Default
  private List<String> missionLog = new ArrayList<>();

  public static MissionResult success(String message, int reward) {
    return MissionResult.builder()
        .status(Status.SUCCESS)
        .message(message)
        .baseReward(reward)
        .allowsContinuation(true)
        .build();
  }

  public static MissionResult failure(String message) {
    return MissionResult.builder()
        .status(Status.FAILURE)
        .message(message)
        .baseReward(0)
        .allowsContinuation(false)
        .build();
  }

  public static MissionResult compromised(String message) {
    return MissionResult.builder()
        .status(Status.COMPROMISED)
        .message(message)
        .baseReward(0)
        .allowsContinuation(false)
        .build();
  }

  public int getTotalReward() {
    return baseReward + bonusReward;
  }

  public double getCompletionPercentage() {
    if (totalObjectivesCount == 0) {
      return 0.0;
    }
    return (double) objectivesCompletedCount / totalObjectivesCount * 100.0;
  }

  public boolean isSuccess() {
    return status == Status.SUCCESS || status == Status.PARTIAL_SUCCESS;
  }

  public void addObjectiveCompletion(String objectiveId, boolean completed) {
    objectivesCompleted.put(objectiveId, completed);
    if (completed) {
      objectivesCompletedCount++;
    }
  }

  public void addReward(Item item) {
    rewards.add(item);
  }

  public void addAchievement(String achievement) {
    achievementsUnlocked.add(achievement);
  }

  public void addLogEntry(String entry) {
    missionLog.add(entry);
  }

  public void addLongTermConsequence(String consequence) {
    longTermConsequences.add(consequence);
  }

  public void unlockMission(String missionId) {
    newMissionsUnlocked.add(missionId);
  }

  public void addReputationChange(String faction, int change) {
    reputationChanges.put(faction, change);
  }

  public String getDetailedReport() {
    StringBuilder sb = new StringBuilder();
    sb.append("=== MISSION REPORT ===\n");
    sb.append("Status: ").append(status).append("\n");
    sb.append("Message: ").append(message).append("\n");
    sb.append("Total Score: ").append(totalScore).append("\n");
    sb.append("Reward: ").append(getTotalReward()).append("\n");
    sb.append("Objectives Completed: ").append(objectivesCompletedCount)
        .append("/").append(totalObjectivesCount)
        .append(" (").append(String.format("%.1f", getCompletionPercentage())).append("%)\n");

    if (!achievementsUnlocked.isEmpty()) {
      sb.append("\nAchievements:\n");
      achievementsUnlocked.forEach(a -> sb.append("  🏆 ").append(a).append("\n"));
    }

    if (!rewards.isEmpty()) {
      sb.append("\nAwards:\n");
      rewards.forEach(r -> sb.append("  📦 ").append(r.getName()).append("\n"));
    }

    if (!longTermConsequences.isEmpty()) {
      sb.append("\nLong-term consequences:\n");
      longTermConsequences.forEach(c -> sb.append("  ⚠ ").append(c).append("\n"));
    }

    return sb.toString();
  }

  public enum Status {
    SUCCESS,
    PARTIAL_SUCCESS,
    FAILURE,
    ABORTED,
    COMPROMISED
  }
}