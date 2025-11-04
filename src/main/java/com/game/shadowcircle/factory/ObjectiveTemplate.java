package com.game.shadowcircle.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Шаблон цілі місії
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ObjectiveTemplate {

  private String id;
  private String description;
  private ObjectiveType type;
  private int rewardPoints;
  private boolean isOptional;
  private boolean isHidden;
  @Builder.Default
  private Map<String, Object> parameters = new HashMap<>();
  @Builder.Default
  private List<String> completionConditions = new ArrayList<>();

  public static ObjectiveTemplate reachLocation(String locationId, String description, int reward) {
    return ObjectiveTemplate.builder()
        .id("reach_" + locationId)
        .description(description)
        .type(ObjectiveType.REACH_LOCATION)
        .rewardPoints(reward)
        .isOptional(false)
        .parameters(Map.of("locationId", locationId))
        .build();
  }

  public static ObjectiveTemplate gatherIntel(String intelId, String description, int reward) {
    return ObjectiveTemplate.builder()
        .id("intel_" + intelId)
        .description(description)
        .type(ObjectiveType.GATHER_INTEL)
        .rewardPoints(reward)
        .isOptional(false)
        .parameters(Map.of("intelId", intelId))
        .build();
  }

  public static ObjectiveTemplate sabotageTarget(String targetId, String description, int reward) {
    return ObjectiveTemplate.builder()
        .id("sabotage_" + targetId)
        .description(description)
        .type(ObjectiveType.SABOTAGE_OBJECT)
        .rewardPoints(reward)
        .isOptional(false)
        .parameters(Map.of("targetId", targetId))
        .build();
  }

  public static ObjectiveTemplate meetNPC(String npcId, String description, int reward) {
    return ObjectiveTemplate.builder()
        .id("meet_" + npcId)
        .description(description)
        .type(ObjectiveType.MEET_NPC)
        .rewardPoints(reward)
        .isOptional(false)
        .parameters(Map.of("npcId", npcId))
        .build();
  }

  public static ObjectiveTemplate eliminateTarget(String targetId, String description, int reward) {
    return ObjectiveTemplate.builder()
        .id("eliminate_" + targetId)
        .description(description)
        .type(ObjectiveType.ELIMINATE_TARGET)
        .rewardPoints(reward)
        .isOptional(true)
        .parameters(Map.of("targetId", targetId))
        .build();
  }

  public enum ObjectiveType {
    REACH_LOCATION,
    GATHER_ITEM,
    MEET_NPC,
    ELIMINATE_TARGET,
    PROTECT_ASSET,
    SABOTAGE_OBJECT,
    GATHER_INTEL,
    MAINTAIN_COVER,
    TIME_LIMIT
  }
}