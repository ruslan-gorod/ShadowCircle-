package com.game.shadowcircle.builder;

import com.game.shadowcircle.composite.Objective;
import com.game.shadowcircle.model.Mission;
import com.game.shadowcircle.model.Scene;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MissionBuilder {

  private String title;
  private String description;
  private List<Scene> scenes = new ArrayList<>();
  private Map<String, Objective> objectives = new HashMap<>();
  private int suspicionThreshold = 100;
  private Duration timeLimit;

  public static MissionBuilder infiltration() {
    return new MissionBuilder()
        .withTitle("Операція 'Тінь'")
        .withDescription("Проникнення до штаб-квартири ворога для збору інформації")
        .withSuspicionThreshold(80)
        .withTimeLimit(Duration.ofHours(24));
  }

  public static MissionBuilder sabotage() {
    return new MissionBuilder()
        .withTitle("Операція 'Підрив'")
        .withDescription("Диверсійна операція з підриву комунікацій ворога")
        .withSuspicionThreshold(60)
        .withTimeLimit(Duration.ofHours(12));
  }

  public MissionBuilder withTitle(String title) {
    this.title = title;
    return this;
  }

  public MissionBuilder withDescription(String description) {
    this.description = description;
    return this;
  }

  public MissionBuilder withScenes(List<Scene> scenes) {
    this.scenes = scenes;
    return this;
  }

  public MissionBuilder withObjectives(Map<String, Objective> objectives) {
    this.objectives = objectives;
    return this;
  }

  public MissionBuilder withSuspicionThreshold(int suspicionThreshold) {
    this.suspicionThreshold = suspicionThreshold;
    return this;
  }

  public MissionBuilder withTimeLimit(Duration timeLimit) {
    this.timeLimit = timeLimit;
    return this;
  }

  public MissionBuilder addScene(Scene scene) {
    scenes.add(scene);
    return this;
  }

  public MissionBuilder addObjective(String id, Objective objective) {
    objectives.put(id, objective);
    return this;
  }

  public Mission build() {
    validate();
    return Mission.builder()
        .title(title)
        .description(description)
        .scenes(scenes)
        .completed(false)
        .build();
  }

  private void validate() {
    if (title == null || title.trim().isEmpty()) {
      throw new IllegalStateException("Назва місії не може бути порожньою");
    }
    if (description == null || description.trim().isEmpty()) {
      throw new IllegalStateException("Опис місії не може бути порожнім");
    }
    if (scenes == null || scenes.isEmpty()) {
      throw new IllegalStateException("Місія повинна містити принаймні одну сцену");
    }
    if (suspicionThreshold <= 0 || suspicionThreshold > 100) {
      throw new IllegalStateException("Поріг підозри повинен бути в діапазоні 1-100");
    }
    if (timeLimit != null && timeLimit.isNegative()) {
      throw new IllegalStateException("Ліміт часу не може бути від'ємним");
    }
  }
}