package com.game.shadowcircle.events;

import com.game.shadowcircle.template.AbstractMission;
import lombok.Getter;

@Getter
public class MissionStartedEvent extends GameEvent {

  private final AbstractMission mission;

  public MissionStartedEvent(AbstractMission mission) {
    super("MISSION_STARTED", "Розпочато місію: " + mission.getClass().getSimpleName(), mission);
    this.mission = mission;
  }
}