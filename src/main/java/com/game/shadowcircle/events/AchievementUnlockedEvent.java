package com.game.shadowcircle.events;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AchievementUnlockedEvent extends GameEvent {

  private String achievementId;
  private String achievementName;
  private String description;

  public AchievementUnlockedEvent(String achievementId, String achievementName,
      String description) {
    super();
    this.setType("ACHIEVEMENT_UNLOCKED");
    this.setMessage(String.format("Achievement unlocked: %s", achievementName));
    this.setTimestamp(LocalDateTime.now());
    this.achievementId = achievementId;
    this.achievementName = achievementName;
    this.description = description;
  }
}