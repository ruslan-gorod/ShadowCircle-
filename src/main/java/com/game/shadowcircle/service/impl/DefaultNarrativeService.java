package com.game.shadowcircle.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.shadowcircle.model.Mission;
import com.game.shadowcircle.model.Scene;
import com.game.shadowcircle.service.DialogueService;
import com.game.shadowcircle.service.NarrativeService;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Data
@RequiredArgsConstructor
public class DefaultNarrativeService implements NarrativeService {

  private final DialogueService dialogueService;

  @Value("${game.missions.path}")
  private String missionsPath; // шлях до missions.json

  private Map<String, Mission> missionMap = new HashMap<>();

  @PostConstruct
  public void init() {
    loadMissions();
  }

  private void loadMissions() {
    ObjectMapper mapper = new ObjectMapper();
    try {
      MissionData data = mapper.readValue(new File(missionsPath), MissionData.class);
      for (Mission m : data.getMissions()) {
        missionMap.put(m.getTitle(), m);
      }
      log.info("Місії завантажено: {}", missionMap.keySet());
    } catch (Exception e) {
      log.error("Не вдалося завантажити місії з {}", missionsPath, e);
      throw new RuntimeException("Помилка завантаження місій", e);
    }
  }

  @Override
  public Scene getSceneById(String missionId, String sceneId) {
    Mission mission = missionMap.get(missionId);
    if (mission == null) {
      log.warn("Місія {} не знайдена", missionId);
      return null;
    }
    return mission.getScenes().stream()
        .filter(s -> s.getId().equals(sceneId))
        .findFirst()
        .orElse(null);
  }

  @Override
  public void displayScene(Scene scene) {
    if (scene == null) {
      dialogueService.print("Сцена відсутня або не завантажена.");
      return;
    }

    // Вивід тексту сцени
    dialogueService.print("\n=== " + scene.getNarrativeText() + " ===");

    // Вивід варіантів вибору
    if (scene.getChoices() != null && !scene.getChoices().isEmpty()) {
      for (int i = 0; i < scene.getChoices().size(); i++) {
        dialogueService.print((i + 1) + ". " + scene.getChoices().get(i).getDescription());
      }
    } else if (scene.isEndingScene()) {
      dialogueService.print("=== Кінець сцени ===");
    }
  }
}
