package com.game.shadowcircle.template;

import com.game.shadowcircle.events.MissionCompletedEvent;
import com.game.shadowcircle.events.MissionStartedEvent;
import com.game.shadowcircle.model.GameContext;

public abstract class AbstractMission {

  public final MissionResult execute(GameContext context) {
    prepareMission(context);
    MissionResult result = executeMissionLogic(context);
    cleanupMission(context);
    return result;
  }

  protected void prepareMission(GameContext context) {
    context.getEventPublisher().publishEvent(
        new MissionStartedEvent(this)
    );
  }

  protected abstract MissionResult executeMissionLogic(GameContext context);

  protected void cleanupMission(GameContext context) {
    context.getEventPublisher().publishEvent(
        new MissionCompletedEvent(this)
    );
  }
}