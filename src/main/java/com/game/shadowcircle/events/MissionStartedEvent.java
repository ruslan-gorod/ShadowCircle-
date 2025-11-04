package com.game.shadowcircle.events;

import com.game.shadowcircle.factory.Difficulty;
import com.game.shadowcircle.model.Mission;
import com.game.shadowcircle.model.Player;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Подія початку місії
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MissionStartedEvent extends GameEvent {

  private Mission mission;
  private Player player;
  private Difficulty difficulty;

  public MissionStartedEvent(Mission mission, Player player, Difficulty difficulty) {
    super();
    this.setType("MISSION_STARTED");
    this.setMessage(String.format("Mission started: %s", mission.getTitle()));
    this.setPayload(mission);
    this.setTimestamp(LocalDateTime.now());
    this.mission = mission;
    this.player = player;
    this.difficulty = difficulty;
  }

  public MissionStartedEvent(Mission mission) {
    this(mission, null, Difficulty.AGENT);
  }
}
