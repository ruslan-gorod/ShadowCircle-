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
 * Результат прийняття рішення/вибору гравця
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class DecisionResult {

  // Основні результати
  private boolean success;
  private int scoreGain;
  private int riskPenalty;

  // Вплив на стан гри
  private int suspicionIncrease;
  private int coverDamage;
  private int healthChange;

  // Зміни в характеристиках
  @Builder.Default
  private Map<String, Integer> attributeChanges = new HashMap<>();

  // Отримані предмети та інформація
  @Builder.Default
  private List<Item> itemsGained = new ArrayList<>();

  @Builder.Default
  private List<String> informationGained = new ArrayList<>();

  // Наслідки
  private String message;
  private String additionalInfo;

  @Builder.Default
  private List<String> consequences = new ArrayList<>();

  // Зміни у відносинах
  @Builder.Default
  private Map<String, Integer> relationshipChanges = new HashMap<>();

  // Нова сцена (якщо є перехід)
  private String nextSceneId;

  // Критичні події
  private boolean triggersCriticalEvent;
  private String criticalEventId;

  public static DecisionResult criticalFailure(String message) {
    return DecisionResult.builder()
        .success(false)
        .message(message)
        .coverDamage(50)
        .suspicionIncrease(50)
        .healthChange(-30)
        .triggersCriticalEvent(true)
        .build();
  }

  public static DecisionResult criticalSuccess(String message, int scoreGain) {
    return DecisionResult.builder()
        .success(true)
        .message(message)
        .scoreGain(scoreGain)
        .suspicionIncrease(-10)
        .triggersCriticalEvent(true)
        .build();
  }

  public void addConsequence(String consequence) {
    this.consequences.add(consequence);
  }

  public void addItemGained(Item item) {
    this.itemsGained.add(item);
  }

  public void addInformationGained(String info) {
    this.informationGained.add(info);
  }

  public void addRelationshipChange(String npcId, int change) {
    this.relationshipChanges.put(npcId, change);
  }

  public void addAttributeChange(String attribute, int change) {
    this.attributeChanges.put(attribute, change);
  }

  public int getTotalImpact() {
    return scoreGain - riskPenalty;
  }

  public boolean isCritical() {
    return triggersCriticalEvent || coverDamage >= 50 || healthChange <= -50;
  }

  public String getFullMessage() {
    StringBuilder sb = new StringBuilder(message);
    if (additionalInfo != null && !additionalInfo.isEmpty()) {
      sb.append("\n").append(additionalInfo);
    }
    if (!consequences.isEmpty()) {
      sb.append("\nConsequences:");
      consequences.forEach(c -> sb.append("\n  - ").append(c));
    }
    return sb.toString();
  }
}