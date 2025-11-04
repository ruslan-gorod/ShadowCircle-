package com.game.shadowcircle.factory;

import com.game.shadowcircle.model.Choice;
import com.game.shadowcircle.model.Item;
import com.game.shadowcircle.model.Scene;
import com.game.shadowcircle.model.SceneGenerationParams;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Генератор сцен для динамічного створення місій
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SceneGenerator {

  private final Random random = new Random();

  /**
   * Генерує список сцен на основі шаблону місії та складності
   */
  public List<Scene> generateScenes(MissionTemplate template, Difficulty difficulty) {
    int sceneCount = random.nextInt(
        template.getMaxScenes() - template.getMinScenes() + 1
    ) + template.getMinScenes();

    log.info("Generating {} scenes for mission '{}'", sceneCount, template.getName());

    List<Scene> scenes = new ArrayList<>();

    for (int i = 0; i < sceneCount; i++) {
      SceneGenerationParams params = SceneGenerationParams.builder()
          .missionTemplate(template)
          .difficulty(difficulty)
          .sceneIndex(i)
          .totalScenes(sceneCount)
          .availableLocations(template.getAvailableLocations())
          .availableNPCs(template.getNpcs())
          .build();

      Scene scene = generateScene(params);
      scenes.add(scene);
    }

    // Зв'язуємо сцени між собою
    linkScenes(scenes);

    return scenes;
  }

  /**
   * Генерує одну сцену
   */
  private Scene generateScene(SceneGenerationParams params) {
    Scene scene = new Scene();
    scene.setId(generateSceneId(params));

    if (params.isFirstScene()) {
      scene.setNarrativeText(generateIntroText(params));
    } else if (params.isLastScene()) {
      scene.setNarrativeText(generateOutroText(params));
      scene.setEndingScene(true);
    } else {
      scene.setNarrativeText(generateMidGameText(params));
    }

    // Генеруємо вибори
    List<Choice> choices = generateChoices(scene, params);
    scene.setChoices(choices);

    return scene;
  }

  /**
   * Генерує унікальний ID сцени
   */
  private String generateSceneId(SceneGenerationParams params) {
    return String.format("%s_scene_%d_%s",
        params.getMissionTemplate().getId(),
        params.getSceneIndex(),
        UUID.randomUUID().toString().substring(0, 8)
    );
  }

  /**
   * Генерує вступний текст сцени
   */
  private String generateIntroText(SceneGenerationParams params) {
    MissionTemplate template = params.getMissionTemplate();

    List<String> intros = Arrays.asList(
        "You have received the task: %s. Time to proceed with caution.",
        "Mission '%s' begins. Your task is %s.",
        "Operation begins. Objective: %s. Be vigilant.",
        "Briefing complete. %s. Remember to be stealthy."
    );

    String intro = intros.get(random.nextInt(intros.size()));
    return String.format(intro, template.getName(), template.getDescription());
  }

  /**
   * Генерує текст для середніх сцен
   */
  private String generateMidGameText(SceneGenerationParams params) {
    MissionTemplate.MissionType type = params.getMissionTemplate().getType();

    return switch (type) {
      case INFILTRATION -> generateInfiltrationText();
      case SABOTAGE -> generateSabotageText();
      case INTELLIGENCE -> generateIntelligenceText();
      case EXTRACTION -> generateExtractionText();
      case ASSASSINATION -> generateAssassinationText();
      default -> generateGenericText();
    };
  }

  private String generateInfiltrationText() {
    List<String> texts = Arrays.asList(
        "You are approaching a guarded perimeter. Guards are patrolling the area.",
        "There is a checkpoint in front of you. You need to pass unnoticed.",
        "You are in a corridor. You can hear the voices of guards ahead.",
        "Surveillance cameras are scanning the area. You need to act quickly."
    );
    return texts.get(random.nextInt(texts.size()));
  }

  private String generateSabotageText() {
    List<String> texts = Arrays.asList(
        "You are near the target. Time to act.",
        "Security system is active. Sabotage plan required.",
        "Your target is in front of you, but time is limited.",
        "The enemy is unaware of your intentions. Not yet."
    );
    return texts.get(random.nextInt(texts.size()));
  }

  private String generateIntelligenceText() {
    List<String> texts = Arrays.asList(
        "You have gained access to confidential documents.",
        "The informant is ready to meet. But can he be trusted?",
        "You have the enemy's computer system in front of you. Time to collect data.",
        "You are eavesdropping on an important conversation."
    );
    return texts.get(random.nextInt(texts.size()));
  }

  private String generateExtractionText() {
    List<String> texts = Arrays.asList(
        "Time to evacuate. But the alarm could go off at any moment.",
        "The meeting point is ahead. Hopefully, transportation is waiting.",
        "You're almost at the exit, but security is tight."
    );
    return texts.get(random.nextInt(texts.size()));
  }

  private String generateAssassinationText() {
    List<String> texts = Arrays.asList(
        "Target in sight. One opportunity.",
        "You have a window to act. The enemy is not waiting to attack.",
        "The moment to strike has come, but the risk is high."
    );
    return texts.get(random.nextInt(texts.size()));
  }

  private String generateGenericText() {
    List<String> texts = Arrays.asList(
        "The situation is evolving. A decision must be made.",
        "You are on the scene. What will you do next?",
        "The mission continues. Every step counts."
    );
    return texts.get(random.nextInt(texts.size()));
  }

  /**
   * Генерує завершальний текст
   */
  private String generateOutroText(SceneGenerationParams params) {
    List<String> outros = Arrays.asList(
        "Mission complete. Time to evacuate.",
        "You completed the task. But did you leave any traces?",
        "The operation is coming to an end. The results will be evaluated.",
        "The last step. A little more - and you are safe."
    );
    return outros.get(random.nextInt(outros.size()));
  }

  /**
   * Генерує вибори для сцени
   */
  private List<Choice> generateChoices(Scene scene, SceneGenerationParams params) {
    List<Choice> choices = new ArrayList<>();

    int choiceCount = params.isLastScene() ?
        random.nextInt(2) + 2 :
        random.nextInt(2) + 3;

    for (int i = 0; i < choiceCount; i++) {
      Choice choice = generateChoice(scene, params, i);
      choices.add(choice);
    }

    return choices;
  }

  /**
   * Генерує один вибір
   */
  private Choice generateChoice(Scene scene, SceneGenerationParams params, int index) {
    Difficulty difficulty = params.getDifficulty();

    Choice choice = new Choice();
    choice.setId(scene.getId() + "_choice_" + index);

    // Різні типи виборів
    ChoiceType type = ChoiceType.values()[random.nextInt(ChoiceType.values().length)];

    switch (type) {
      case STEALTH -> {
        choice.setDescription("Act secretly");
        choice.setRiskLevel(difficulty.calculateRisk(10));
        choice.setRewardPoints(difficulty.calculateReward(50));
      }
      case AGGRESSIVE -> {
        choice.setDescription("Act aggressively");
        choice.setRiskLevel(difficulty.calculateRisk(30));
        choice.setRewardPoints(difficulty.calculateReward(70));
      }
      case DIPLOMATIC -> {
        choice.setDescription("Try negotiation");
        choice.setRiskLevel(difficulty.calculateRisk(15));
        choice.setRewardPoints(difficulty.calculateReward(60));
      }
      case TECHNICAL -> {
        choice.setDescription("Use technical means");
        choice.setRiskLevel(difficulty.calculateRisk(20));
        choice.setRewardPoints(difficulty.calculateReward(65));
      }
    }

    // Генеруємо можливі наслідки
    generateConsequences(choice, params);

    return choice;
  }

  /**
   * Генерує наслідки для вибору
   */
  private void generateConsequences(Choice choice, SceneGenerationParams params) {
    // Іноді додаємо предмет як нагороду
    if (random.nextInt(100) < 30) {
      Item reward = generateRandomItem();
      choice.setItemReward(reward);
    }

    // Встановлюємо ймовірність підозри
    int suspicionChance = random.nextInt(50);
    choice.setRiskLevel(choice.getRiskLevel() + suspicionChance / 10);
  }

  /**
   * Генерує випадковий предмет
   */
  private Item generateRandomItem() {
    List<String> itemNames = Arrays.asList(
        "Documents", "Keycard", "Communication device",
        "Medical kit", "Signal jammer"
    );

    String name = itemNames.get(random.nextInt(itemNames.size()));

    Item item = new Item();
    item.setName(name);
    item.setDescription("Useful item for the mission");
    return item;
  }

  /**
   * Зв'язує сцени між собою (встановлює nextSceneId)
   */
  private void linkScenes(List<Scene> scenes) {
    for (int i = 0; i < scenes.size() - 1; i++) {
      Scene current = scenes.get(i);
      Scene next = scenes.get(i + 1);

      // Для кожного вибору в поточній сцені встановлюємо наступну сцену
      for (Choice choice : current.getChoices()) {
        choice.setNextSceneId(next.getId());
      }
    }

    // Остання сцена не має наступної
    Scene lastScene = scenes.get(scenes.size() - 1);
    for (Choice choice : lastScene.getChoices()) {
      choice.setNextSceneId(null); // Кінець місії
    }
  }

  /**
   * Типи виборів
   */
  private enum ChoiceType {
    STEALTH,
    AGGRESSIVE,
    DIPLOMATIC,
    TECHNICAL
  }
}