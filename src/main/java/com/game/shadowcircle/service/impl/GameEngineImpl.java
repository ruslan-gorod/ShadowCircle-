package com.game.shadowcircle.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.shadowcircle.model.*;
import com.game.shadowcircle.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameEngineImpl implements GameEngine {

  private final PlayerService playerService;
  private final DialogueService dialogueService;
  private final MissionService missionService;
  private final InventoryService inventoryService;
  private final EnemyService enemyService;

  private GameState currentState;
  private static final String SAVE_FILE = "./save/game_save.json";

  @Override
  public void startNewGame(String playerName) {
    Player player = playerService.createPlayer(playerName);
    currentState = GameState.builder()
        .player(player)
        .currentMission(null)
        .completedMissions(new ArrayList<>())
        .totalScore(0)
        .lastSaved(LocalDateTime.now())
        .build();
    dialogueService.print("Гра почалася! Успіхів, агент " + playerName + "!");
  }

  @Override
  public void loadGame() {
    File file = new File(SAVE_FILE);
    if (!file.exists()) {
      dialogueService.print("Файл збереження не знайдено.");
      return;
    }

    try {
      ObjectMapper mapper = new ObjectMapper();
      GameState savedState = mapper.readValue(file, GameState.class);
      this.currentState = savedState;
      playerService.updatePlayer(savedState.getPlayer());
      dialogueService.print("Гра завантажена успішно.");
    } catch (IOException e) {
      log.error("Помилка завантаження гри", e);
      dialogueService.print("Не вдалося завантажити гру!");
    }
  }

  @Override
  public void saveGame() {
    try {
      Files.createDirectories(Paths.get("./save"));
      currentState.setLastSaved(LocalDateTime.now());
      currentState.setTotalScore(playerService.getCurrentPlayer().getScore());

      ObjectMapper mapper = new ObjectMapper();
      mapper.writerWithDefaultPrettyPrinter().writeValue(new File(SAVE_FILE), currentState);

      dialogueService.print("Прогрес збережено у файлі: " + SAVE_FILE);
    } catch (IOException e) {
      log.error("Помилка збереження гри", e);
      dialogueService.print("Не вдалося зберегти гру!");
    }
  }

  @Override
  public void playScene(Scene scene) {
    if (scene == null) return;

    currentState.setCurrentMission(scene.getMission());
    currentState.setPlayer(playerService.getCurrentPlayer());

    dialogueService.print("\n=== " + scene.getNarrativeText() + " ===");

    if (scene.getChoices() != null && !scene.getChoices().isEmpty()) {
      for (int i = 0; i < scene.getChoices().size(); i++) {
        dialogueService.print((i + 1) + ". " + scene.getChoices().get(i).getDescription());
      }
    } else if (scene.isEndingScene()) {
      dialogueService.print("=== Кінець сцени ===");
    }
  }

  @Override
  public void applyChoice(Choice choice) {
    if (choice == null) return;

    // 1. Нанести ризик
    playerService.applyRisk(choice.getRiskLevel());

    // 2. Додати бали
    playerService.addScore(choice.getRewardPoints());
    currentState.setTotalScore(playerService.getCurrentPlayer().getScore());

    // 3. Додати предмети
    if (choice.getItemReward() != null) {
      inventoryService.addItem(playerService.getCurrentPlayer(), choice.getItemReward());
    }

    // 4. Завершення місії або перехід до наступної сцени
    Scene nextScene = null;
    if (choice.getNextSceneId() == null) {
      Mission mission = currentState.getCurrentMission();
      if (mission != null) {
        missionService.completeMission(mission);
        currentState.getCompletedMissions().add(mission.getTitle());
      }
      dialogueService.print("Місія завершена!");
    } else {
      nextScene = missionService.getScene(
          currentState.getCurrentMission().getTitle(),
          choice.getNextSceneId()
      );
      playScene(nextScene);
    }

    // 5. Перевірка життя гравця
    if (playerService.getCurrentPlayer().getHealth() <= 0) {
      dialogueService.print("Ваше життя закінчилось! Гру завершено.");
    }
  }

  @Override
  public GameState getCurrentState() {
    return currentState;
  }
}
