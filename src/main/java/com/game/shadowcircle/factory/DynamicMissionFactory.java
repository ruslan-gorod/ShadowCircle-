package com.game.shadowcircle.factory;

import com.game.shadowcircle.model.Mission;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DynamicMissionFactory implements MissionFactory {

  private final Map<String, MissionTemplate> templates = new HashMap<>();
  private final SceneGenerator sceneGenerator;

  @Override
  public Mission createMission(String type, Difficulty difficulty) {
    MissionTemplate template = templates.get(type);

    return Mission.builder()
        .title(generateTitle(type, difficulty))
        .scenes(sceneGenerator.generateScenes(template, difficulty))
        .build();
  }

  private String generateTitle(String type, Difficulty difficulty) {
    return "%s with %s".formatted(type, difficulty.getDisplayName());
  }
}