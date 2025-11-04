package com.game.shadowcircle.service;

import com.game.shadowcircle.model.Mission;
import com.game.shadowcircle.model.Scene;
import java.util.List;

public interface MissionService {

  List<Mission> getAllMissions();

  Mission getMissionByTitle(String title);

  Scene getScene(String missionTitle, String sceneId);

  void completeMission(Mission mission);
}
