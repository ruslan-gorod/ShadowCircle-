package com.game.shadowcircle.events;

import com.game.shadowcircle.model.Mission;
import com.game.shadowcircle.model.MissionResult;
import com.game.shadowcircle.model.Player;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MissionCompletedEvent extends GameEvent {

  private Mission mission;
  private MissionResult result;
  private Player player;

  public MissionCompletedEvent(Mission mission, MissionResult result, Player player) {
    super();
    this.setType("MISSION_COMPLETED");
    this.setMessage(String.format("Mission completed: %s - %s",
        mission.getTitle(), result.getStatus()));
    this.setPayload(result);
    this.setTimestamp(LocalDateTime.now());
    this.mission = mission;
    this.result = result;
    this.player = player;
  }
}