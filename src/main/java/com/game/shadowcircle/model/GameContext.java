package com.game.shadowcircle.model;

import com.game.shadowcircle.events.GameEventPublisher;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;

/**
 * Центральний контекст гри, що містить всю поточну інформацію про стан гри. Використовується як
 * параметр для Command, Strategy, State патернів.
 */
@Data
@Builder
public class GameContext {

  // Основні компоненти
  private Player player;
  private Mission currentMission;
  private Scene currentScene;
  private GameEventPublisher eventPublisher;

  // Стан гри
  private int suspicionLevel;  // 0-100, рівень підозри
  private int coverIntegrity;  // 0-100, цілісність легенди
  private String currentLocation;
  private Map<String, Boolean> flags;  // Прапорці подій (наприклад, "met_contact", "alarm_triggered")

  // Історія
  @Builder.Default
  private List<String> completedMissions = new ArrayList<>();
  @Builder.Default
  private List<Choice> choiceHistory = new ArrayList<>();
  @Builder.Default
  private Map<String, Integer> relationshipLevels = new HashMap<>();  // Відносини з NPC

  // Інвентар та ресурси
  private Inventory inventory;
  @Builder.Default
  private Map<String, Object> gameData = new HashMap<>();  // Додаткові дані

  // Час та обмеження
  private int turnNumber;
  private int timeRemaining;  // Залишок часу на місію (якщо обмежена)

  public GameContext(Player player, GameEventPublisher eventPublisher) {
    this.player = player;
    this.eventPublisher = eventPublisher;
    this.suspicionLevel = 0;
    this.coverIntegrity = 100;
    this.flags = new HashMap<>();
    this.completedMissions = new ArrayList<>();
    this.choiceHistory = new ArrayList<>();
    this.relationshipLevels = new HashMap<>();
    this.gameData = new HashMap<>();
    this.turnNumber = 0;
    this.inventory = player.getInventory() != null ? player.getInventory() : new Inventory();
  }

  // === Методи роботи з підозрою ===

  public void addSuspicion(int amount) {
    this.suspicionLevel = Math.min(100, this.suspicionLevel + amount);
    if (this.suspicionLevel >= 100) {
      setFlag("cover_blown", true);
    }
  }

  public void reduceSuspicion(int amount) {
    this.suspicionLevel = Math.max(0, this.suspicionLevel - amount);
  }

  public boolean isCoverIntact() {
    return coverIntegrity > 0 && suspicionLevel < 100;
  }

  // === Методи роботи з прапорцями ===

  public void setFlag(String key, boolean value) {
    flags.put(key, value);
  }

  public boolean getFlag(String key) {
    return flags.getOrDefault(key, false);
  }

  public boolean hasFlag(String key) {
    return flags.containsKey(key);
  }

  // === Методи роботи з відносинами ===

  public void setRelationship(String npcId, int level) {
    relationshipLevels.put(npcId, Math.max(-100, Math.min(100, level)));
  }

  public int getRelationship(String npcId) {
    return relationshipLevels.getOrDefault(npcId, 0);
  }

  public void improveRelationship(String npcId, int amount) {
    int current = getRelationship(npcId);
    setRelationship(npcId, current + amount);
  }

  public void deteriorateRelationship(String npcId, int amount) {
    int current = getRelationship(npcId);
    setRelationship(npcId, current - amount);
  }

  // === Методи роботи з документами та інформацією ===

  public boolean hasDocument(String documentId) {
    return inventory.hasItem(documentId);
  }

  public boolean hasInformation(String infoKey) {
    return gameData.containsKey("info_" + infoKey);
  }

  public void gatherInformation(String infoKey, Object data) {
    gameData.put("info_" + infoKey, data);
  }

  // === Методи роботи зі станом місії ===

  public void transitionToScene(String sceneId) {
    if (currentMission != null) {
      Optional<Scene> nextScene = currentMission.getScenes().stream()
          .filter(s -> s.getId().equals(sceneId))
          .findFirst();
      nextScene.ifPresent(scene -> this.currentScene = scene);
    }
  }

  public boolean isMissionCompleted(String missionTitle) {
    return completedMissions.contains(missionTitle);
  }

  public void completeMission(String missionTitle) {
    if (!completedMissions.contains(missionTitle)) {
      completedMissions.add(missionTitle);
    }
  }

  // === Методи для перевірки саботажу ===

  public boolean isSabotaged(String targetId) {
    return getFlag("sabotaged_" + targetId);
  }

  public void markAsSabotaged(String targetId) {
    setFlag("sabotaged_" + targetId, true);
  }

  // === Методи для збереження історії ===

  public void recordChoice(Choice choice) {
    choiceHistory.add(choice);
    turnNumber++;
  }

  public Choice getLastChoice() {
    return choiceHistory.isEmpty() ? null : choiceHistory.get(choiceHistory.size() - 1);
  }

  // === Методи для роботи з часом ===

  public void advanceTime(int hours) {
    timeRemaining = Math.max(0, timeRemaining - hours);
  }

  public boolean isTimeExpired() {
    return timeRemaining <= 0;
  }

  // === Корисні методи перевірки ===

  public boolean canAffordRisk(int risk) {
    return player.getHealth() > risk;
  }

  public boolean isStealthyEnough(int requirement) {
    return player.getStealth() >= requirement;
  }

  public boolean isSmartEnough(int requirement) {
    return player.getIntelligence() >= requirement;
  }

  // === Створення копії контексту (для Memento) ===

  public GameContext deepCopy() {
    return GameContext.builder()
        .player(player.copy())
        .currentMission(currentMission)
        .currentScene(currentScene)
        .eventPublisher(eventPublisher)
        .suspicionLevel(suspicionLevel)
        .coverIntegrity(coverIntegrity)
        .currentLocation(currentLocation)
        .flags(new HashMap<>(flags))
        .completedMissions(new ArrayList<>(completedMissions))
        .choiceHistory(new ArrayList<>(choiceHistory))
        .relationshipLevels(new HashMap<>(relationshipLevels))
        .inventory(inventory.copy())
        .gameData(new HashMap<>(gameData))
        .turnNumber(turnNumber)
        .timeRemaining(timeRemaining)
        .build();
  }

  @Override
  public String toString() {
    return String.format(
        "GameContext{player=%s, mission=%s, suspicion=%d, cover=%d, location=%s}",
        player.getName(),
        currentMission != null ? currentMission.getTitle() : "none",
        suspicionLevel,
        coverIntegrity,
        currentLocation
    );
  }
}
