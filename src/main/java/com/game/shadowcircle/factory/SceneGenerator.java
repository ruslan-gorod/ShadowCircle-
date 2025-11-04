package com.game.shadowcircle.factory;

import com.game.shadowcircle.model.Scene;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Component;

@Component
public class SceneGenerator {

  private final Random random = new Random();

  public List<Scene> generateScenes(MissionTemplate template, Difficulty difficulty) {
    List<Scene> scenes = new ArrayList<>();

    for (String sceneTemplate : template.getSceneTemplates()) {
      Scene scene = generateScene(sceneTemplate, template.getType(), difficulty);
      scenes.add(scene);
    }

    return scenes;
  }

  private Scene generateScene(String template, String missionType, Difficulty difficulty) {
    return Scene.builder()
        .id(generateSceneId(missionType))
        .title(generateSceneTitle(template, missionType))
        .narrativeText(generateSceneDescription(template, missionType, difficulty))
        .choices(generateSceneChoices(template, missionType, difficulty))
        .build();
  }

  private String generateSceneId(String missionType) {
    return missionType + "_scene_" + System.currentTimeMillis() + "_" + random.nextInt(1000);
  }

  private String generateSceneTitle(String template, String missionType) {
    // Генерація назви сцени на основі шаблону та типу місії
    return switch (template) {
      case "infiltration" -> "Тайне проникнення";
      case "sabotage" -> "Операція з підриву";
      case "extraction" -> "Евакуація цілі";
      default -> "Оперативна дія";
    };
  }

  private String generateSceneDescription(String template, String missionType,
      Difficulty difficulty) {
    // Генерація опису сцени
    String baseDescription = getBaseDescription(template);
    return baseDescription + " (Складність: " + difficulty.getDisplayName() + ")";
  }

  private String getBaseDescription(String template) {
    return switch (template) {
      case "infiltration" ->
          "Ви знаходитесь біля штаб-квартири ворога. Потрібно непомітно проникнути всередину.";
      case "sabotage" -> "Ви в приміщенні з критичною інфраструктурою. Час діяти.";
      case "extraction" -> "Ціль знаходиться під охороною. Потрібно вивести її безпечно.";
      default -> "Ви в небезпечній ситуації. Прийміть рішення.";
    };
  }

  private List<com.game.shadowcircle.model.Choice> generateSceneChoices(String template,
      String missionType, Difficulty difficulty) {
    // Генерація варіантів вибору для сцени
    List<com.game.shadowcircle.model.Choice> choices = new ArrayList<>();

    // Додаємо базові варіанти вибору в залежності від типу сцени
    switch (template) {
      case "infiltration":
        choices.add(createStealthChoice(difficulty));
        choices.add(createDisguiseChoice(difficulty));
        choices.add(createTechnicalChoice(difficulty));
        break;
      case "sabotage":
        choices.add(createSabotageChoice(difficulty));
        choices.add(createDiversionChoice(difficulty));
        break;
      default:
        choices.add(createDefaultChoice(difficulty));
    }

    return choices;
  }

  private com.game.shadowcircle.model.Choice createStealthChoice(Difficulty difficulty) {
    return com.game.shadowcircle.model.Choice.builder()
        .description("Спробувати пройти непомітно")
        .riskLevel((int) (20 * difficulty.getMultiplier()))
        .rewardPoints((int) (50 / difficulty.getMultiplier()))
        .stealthRequirement((int) (40 * difficulty.getMultiplier()))
        .highProfile(false)
        .build();
  }

  private com.game.shadowcircle.model.Choice createDisguiseChoice(Difficulty difficulty) {
    return com.game.shadowcircle.model.Choice.builder()
        .description("Використати маскування")
        .riskLevel((int) (15 * difficulty.getMultiplier()))
        .rewardPoints((int) (40 / difficulty.getMultiplier()))
        .stealthRequirement((int) (30 * difficulty.getMultiplier()))
        .highProfile(false)
        .build();
  }

  private com.game.shadowcircle.model.Choice createTechnicalChoice(Difficulty difficulty) {
    return com.game.shadowcircle.model.Choice.builder()
        .description("Зламати систему безпеки")
        .riskLevel((int) (25 * difficulty.getMultiplier()))
        .rewardPoints((int) (60 / difficulty.getMultiplier()))
        .stealthRequirement((int) (50 * difficulty.getMultiplier()))
        .highProfile(true)
        .build();
  }

  private com.game.shadowcircle.model.Choice createSabotageChoice(Difficulty difficulty) {
    return com.game.shadowcircle.model.Choice.builder()
        .description("Підірвати об'єкт")
        .riskLevel((int) (40 * difficulty.getMultiplier()))
        .rewardPoints((int) (80 / difficulty.getMultiplier()))
        .stealthRequirement((int) (20 * difficulty.getMultiplier()))
        .highProfile(true)
        .build();
  }

  private com.game.shadowcircle.model.Choice createDiversionChoice(Difficulty difficulty) {
    return com.game.shadowcircle.model.Choice.builder()
        .description("Створити диверсію")
        .riskLevel((int) (30 * difficulty.getMultiplier()))
        .rewardPoints((int) (60 / difficulty.getMultiplier()))
        .stealthRequirement((int) (35 * difficulty.getMultiplier()))
        .highProfile(true)
        .build();
  }

  private com.game.shadowcircle.model.Choice createDefaultChoice(Difficulty difficulty) {
    return com.game.shadowcircle.model.Choice.builder()
        .description("Продовжити операцію")
        .riskLevel((int) (10 * difficulty.getMultiplier()))
        .rewardPoints((int) (30 / difficulty.getMultiplier()))
        .stealthRequirement((int) (25 * difficulty.getMultiplier()))
        .highProfile(false)
        .build();
  }
}