package com.game.shadowcircle.model;

import com.game.shadowcircle.factory.Difficulty;
import com.game.shadowcircle.factory.MissionTemplate;
import com.game.shadowcircle.factory.NPCTemplate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Параметри для генерації сцен
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SceneGenerationParams {

  private MissionTemplate missionTemplate;
  private Difficulty difficulty;
  private int sceneIndex;
  private int totalScenes;

  // Контекст попередніх сцен
  @Builder.Default
  private List<String> previousSceneIds = new ArrayList<>();

  @Builder.Default
  private Map<String, Boolean> objectivesCompleted = new HashMap<>();

  // Поточний стан
  private int currentSuspicion;
  private int currentHealth;

  // Налаштування генерації
  private boolean allowCombat;
  private boolean allowDialogue;
  private boolean allowStealth;

  @Builder.Default
  private List<String> availableLocations = new ArrayList<>();

  @Builder.Default
  private List<NPCTemplate> availableNPCs = new ArrayList<>();

  public boolean isFirstScene() {
    return sceneIndex == 0;
  }

  public boolean isLastScene() {
    return sceneIndex == totalScenes - 1;
  }

  public boolean isMidGame() {
    return sceneIndex > 0 && sceneIndex < totalScenes - 1;
  }

  public double getProgressPercentage() {
    if (totalScenes == 0) {
      return 0.0;
    }
    return (double) sceneIndex / totalScenes * 100.0;
  }

  public int getRemainingScenes() {
    return totalScenes - sceneIndex - 1;
  }
}