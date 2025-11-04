package com.game.shadowcircle.factory;

import com.game.shadowcircle.model.Mission;
import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class DynamicMissionFactory implements MissionFactory {

  private final Map<String, MissionTemplate> templates = new HashMap<>();
  private final SceneGenerator sceneGenerator;

  public DynamicMissionFactory(SceneGenerator sceneGenerator) {
    this.sceneGenerator = sceneGenerator;
  }

  @PostConstruct
  public void init() {
    // Ініціалізація шаблонів місій
    templates.put("INFILTRATION", createInfiltrationTemplate());
    templates.put("SABOTAGE", createSabotageTemplate());
    templates.put("EXTRACTION", createExtractionTemplate());
    templates.put("INTEL_GATHERING", createIntelGatheringTemplate());
  }

  @Override
  public Mission createMission(String type, Difficulty difficulty) {
    MissionTemplate template = templates.get(type.toUpperCase());

    if (template == null) {
      throw new IllegalArgumentException("Невідомий тип місії: " + type);
    }

    return Mission.builder()
        .title(generateTitle(type, difficulty))
        .description(generateDescription(type, difficulty))
        .scenes(sceneGenerator.generateScenes(template, difficulty))
        .completed(false)
        .build();
  }

  private MissionTemplate createInfiltrationTemplate() {
    return MissionTemplate.builder()
        .type("INFILTRATION")
        .baseTitle("Операція 'Тінь'")
        .description("Таємне проникнення до штаб-квартири ворога для збору інформації")
        .baseTimeLimit(24)
        .baseSuspicionThreshold(80)
        .sceneTemplates(List.of("infiltration", "security_evasion", "data_extraction"))
        .build();
  }

  private MissionTemplate createSabotageTemplate() {
    return MissionTemplate.builder()
        .type("SABOTAGE")
        .baseTitle("Операція 'Підрив'")
        .description("Диверсійна операція з підриву комунікацій ворога")
        .baseTimeLimit(12)
        .baseSuspicionThreshold(60)
        .sceneTemplates(List.of("sabotage", "infiltration", "escape"))
        .build();
  }

  private MissionTemplate createExtractionTemplate() {
    return MissionTemplate.builder()
        .type("EXTRACTION")
        .baseTitle("Операція 'Спасіння'")
        .description("Евакуація цінного агента з території ворога")
        .baseTimeLimit(18)
        .baseSuspicionThreshold(70)
        .sceneTemplates(List.of("extraction", "stealth", "extraction_point"))
        .build();
  }

  private MissionTemplate createIntelGatheringTemplate() {
    return MissionTemplate.builder()
        .type("INTEL_GATHERING")
        .baseTitle("Операція 'Всевидяще око'")
        .description("Збір критичної розвідувальної інформації")
        .baseTimeLimit(36)
        .baseSuspicionThreshold(50)
        .sceneTemplates(List.of("recon", "infiltration", "data_steal", "escape"))
        .build();
  }

  private String generateTitle(String type, Difficulty difficulty) {
    String baseTitle = templates.get(type.toUpperCase()).getBaseTitle();
    String difficultySuffix = getDifficultySuffix(difficulty);
    return baseTitle + " - " + difficultySuffix;
  }

  private String generateDescription(String type, Difficulty difficulty) {
    String baseDescription = templates.get(type.toUpperCase()).getDescription();
    return baseDescription + " Складність: " + difficulty.getDisplayName() + ".";
  }

  private String getDifficultySuffix(Difficulty difficulty) {
    return switch (difficulty) {
      case EASY -> "Просте завдання";
      case NORMAL -> "Стандартна операція";
      case HARD -> "Складне завдання";
      case EXPERT -> "Елітна операція";
    };
  }
}