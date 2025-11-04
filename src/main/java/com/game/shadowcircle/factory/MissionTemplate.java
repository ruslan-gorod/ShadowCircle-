package com.game.shadowcircle.factory;

import com.game.shadowcircle.model.Item;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Шаблон місії для генерації
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MissionTemplate {

  private String id;
  private String name;
  private MissionType type;
  private String description;
  // Параметри генерації
  private int minScenes;
  private int maxScenes;
  private int baseReward;
  private int baseSuspicionThreshold;
  // Цілі місії
  @Builder.Default
  private List<ObjectiveTemplate> primaryObjectives = new ArrayList<>();
  @Builder.Default
  private List<ObjectiveTemplate> secondaryObjectives = new ArrayList<>();
  @Builder.Default
  private List<ObjectiveTemplate> hiddenObjectives = new ArrayList<>();
  // Доступні локації
  @Builder.Default
  private List<String> availableLocations = new ArrayList<>();
  // Можливі NPC
  @Builder.Default
  private List<NPCTemplate> npcs = new ArrayList<>();
  // Можливі предмети
  @Builder.Default
  private List<Item> possibleItems = new ArrayList<>();
  // Умови та обмеження
  @Builder.Default
  private Map<String, Object> requirements = new HashMap<>();
  private int recommendedLevel;
  private int timeLimit; // у годинах
  // Наративні елементи
  @Builder.Default
  private List<String> narrativeHooks = new ArrayList<>();
  @Builder.Default
  private List<String> plotTwists = new ArrayList<>();
  // Складність та ризики
  private int difficultyRating; // 1-10
  @Builder.Default
  private List<String> keyRisks = new ArrayList<>();
  // Нагороди
  @Builder.Default
  private List<String> possibleAchievements = new ArrayList<>();

  public static MissionTemplate infiltration() {
    return MissionTemplate.builder()
        .id("infiltration_template")
        .name("Operation Infiltration")
        .type(MissionType.INFILTRATION)
        .description("Infiltrate the target silently")
        .minScenes(5)
        .maxScenes(8)
        .baseReward(500)
        .baseSuspicionThreshold(80)
        .recommendedLevel(1)
        .difficultyRating(5)
        .build();
  }

  public static MissionTemplate sabotage() {
    return MissionTemplate.builder()
        .id("sabotage_template")
        .name("Operation Destruction")
        .type(MissionType.SABOTAGE)
        .description("Sabotage key enemy targets")
        .minScenes(4)
        .maxScenes(7)
        .baseReward(600)
        .baseSuspicionThreshold(60)
        .recommendedLevel(2)
        .difficultyRating(7)
        .build();
  }

  public static MissionTemplate intelligence() {
    return MissionTemplate.builder()
        .id("intel_template")
        .name("Operation Intelligence")
        .type(MissionType.INTELLIGENCE)
        .description("Gather critical information")
        .minScenes(6)
        .maxScenes(10)
        .baseReward(450)
        .baseSuspicionThreshold(90)
        .recommendedLevel(1)
        .difficultyRating(4)
        .build();
  }

  public String getPrimaryObjective() {
    if (primaryObjectives.isEmpty()) {
      return "Complete mission";
    }
    return primaryObjectives.get(0).getDescription();
  }

  public enum MissionType {
    INFILTRATION("Infiltration"),
    SABOTAGE("Sabotage"),
    INTELLIGENCE("Information Collection"),
    EXTRACTION("Evacuation"),
    ASSASSINATION("Liquidation"),
    COUNTER_INTEL("Counterintelligence"),
    DIPLOMATIC("Diplomacy"),
    HYBRID("Combined");

    private final String displayName;

    MissionType(String displayName) {
      this.displayName = displayName;
    }

    public String getDisplayName() {
      return displayName;
    }
  }
}