package com.game.shadowcircle.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "game")
public class GameConfig {

  /**
   * Шлях до папки з файлами сцен (JSON або YAML).
   */
  private String scenesPath = "data/scenes";

  /**
   * Шлях до папки збережень.
   */
  private String savePath = "data/saves";

  /**
   * Режим налагодження (true = показує додаткову інформацію, наприклад ідентифікатори сцен).
   */
  private boolean debug = false;

  /**
   * Активний профіль гри (наприклад: "main", "dlc1", "training").
   */
  private String profile = "main";

  // --- Getters & Setters ---

  public String getScenesPath() {
    return scenesPath;
  }

  public void setScenesPath(String scenesPath) {
    this.scenesPath = scenesPath;
  }

  public String getSavePath() {
    return savePath;
  }

  public void setSavePath(String savePath) {
    this.savePath = savePath;
  }

  public boolean isDebug() {
    return debug;
  }

  public void setDebug(boolean debug) {
    this.debug = debug;
  }

  public String getProfile() {
    return profile;
  }

  public void setProfile(String profile) {
    this.profile = profile;
  }

  @Override
  public String toString() {
    return "GameConfig{" +
        "scenesPath='" + scenesPath + '\'' +
        ", savePath='" + savePath + '\'' +
        ", debug=" + debug +
        ", profile='" + profile + '\'' +
        '}';
  }
}
