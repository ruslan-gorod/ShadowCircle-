package com.game.shadowcircle.template;

import lombok.Getter;

@Getter
public class MissionResult {

  private final boolean success;
  private final String message;
  private final int reward;

  private MissionResult(boolean success, String message, int reward) {
    this.success = success;
    this.message = message;
    this.reward = reward;
  }

  public static MissionResult success(int reward) {
    return new MissionResult(true, "Місію успішно завершено!", reward);
  }

  public static MissionResult failure(String message) {
    return new MissionResult(false, message, 0);
  }
}