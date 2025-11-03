package com.game.shadowcircle.service.impl;


import com.game.shadowcircle.model.Mission;
import com.game.shadowcircle.model.Scene;
import com.game.shadowcircle.service.MissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MissionServiceImpl implements MissionService {

  private final DefaultNarrativeService narrativeService;

  @Override
  public List<Mission> getAllMissions() {
    return List.copyOf(narrativeService.getMissionMap().values());
  }

  @Override
  public Mission getMissionByTitle(String title) {
    return narrativeService.getMissionMap().get(title);
  }

  @Override
  public Scene getScene(String missionTitle, String sceneId) {
    return narrativeService.getSceneById(missionTitle, sceneId);
  }

  @Override
  public void completeMission(Mission mission) {
    mission.setCompleted(true);
    log.info("Місія '{}' завершена", mission.getTitle());
  }
}
