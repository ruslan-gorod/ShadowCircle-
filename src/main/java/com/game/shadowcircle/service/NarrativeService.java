package com.game.shadowcircle.service;

import com.game.shadowcircle.model.Scene;

public interface NarrativeService {

  Scene getSceneById(String missionId, String sceneId);

  void displayScene(Scene scene);
}
