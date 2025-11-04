package com.game.shadowcircle.events;

import com.game.shadowcircle.template.AbstractMission;
import lombok.Getter;

@Getter
public class MissionCompletedEvent extends GameEvent {

  private final AbstractMission mission;

  public MissionCompletedEvent(AbstractMission mission) {
    super("MISSION_COMPLETED", "Завершено місію: " + mission.getClass().getSimpleName(), mission);
    this.mission = mission;
  }
}