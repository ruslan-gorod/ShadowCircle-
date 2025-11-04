package com.game.shadowcircle.events;

import com.game.shadowcircle.model.Mission;
import com.game.shadowcircle.model.Player;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MissionFailedEvent extends GameEvent {

  private Mission mission;
  private String reason;
  private Player player;
  private int severity;

  public MissionFailedEvent(Mission mission, String reason, Player player) {
    super();
    this.setType("MISSION_FAILED");
    this.setMessage(String.format("Mission failed: %s. Reason: %s",
        mission.getTitle(), reason));
    this.setSeverity(3);
    this.setTimestamp(LocalDateTime.now());
    this.mission = mission;
    this.reason = reason;
    this.player = player;
  }
}
