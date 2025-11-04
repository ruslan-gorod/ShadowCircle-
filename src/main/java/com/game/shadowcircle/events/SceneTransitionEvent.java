package com.game.shadowcircle.events;

import com.game.shadowcircle.model.Scene;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SceneTransitionEvent extends GameEvent {

  private Scene fromScene;
  private Scene toScene;

  public SceneTransitionEvent(Scene fromScene, Scene toScene) {
    super();
    this.setType("SCENE_TRANSITION");
    this.setMessage(String.format("Transition: %s → %s",
        fromScene != null ? fromScene.getId() : "start",
        toScene.getId()));
    this.setTimestamp(LocalDateTime.now());
    this.fromScene = fromScene;
    this.toScene = toScene;
  }
}
