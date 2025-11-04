package com.game.shadowcircle.template;

import com.game.shadowcircle.events.MissionCompletedEvent;
import com.game.shadowcircle.events.MissionStartedEvent;
import com.game.shadowcircle.model.GameContext;
import com.game.shadowcircle.model.Mission;
import com.game.shadowcircle.model.MissionResult;

public abstract class AbstractMission {

  public final MissionResult execute(GameContext context) {
    prepareMission(context);
    MissionResult result = executeMissionLogic(context);
    cleanupMission(context);
    return result;
  }

  protected void prepareMission(GameContext context) {
    context.getEventPublisher().publishEvent(
        new MissionStartedEvent(new Mission())
    );
  }

  protected abstract MissionResult executeMissionLogic(GameContext context);

  protected void cleanupMission(GameContext context) {
    context.getEventPublisher().publishEvent(
        new MissionCompletedEvent(new Mission(), new MissionResult(), context.getPlayer())
    );
  }
}