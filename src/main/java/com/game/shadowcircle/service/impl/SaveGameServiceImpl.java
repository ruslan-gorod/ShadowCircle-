package com.game.shadowcircle.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.shadowcircle.events.GameEventPublisher;
import com.game.shadowcircle.events.GameLoadedEvent;
import com.game.shadowcircle.events.GameSavedEvent;
import com.game.shadowcircle.model.GameContext;
import com.game.shadowcircle.model.GameState;
import com.game.shadowcircle.model.Inventory;
import com.game.shadowcircle.service.SaveGameService;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Реалізація сервісу збереження гри
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SaveGameServiceImpl implements SaveGameService {

  private static final String AUTOSAVE_NAME = "autosave";
  private static final String QUICKSAVE_NAME = "quicksave";
  private static final String FILE_EXTENSION = ".json";
  private final GameEventPublisher eventPublisher;
  private final ObjectMapper objectMapper;
  @Value("${game.save.path:./saves}")
  private String savePath;

  /**
   * Ініціалізація: створення директорії збережень та налаштування ObjectMapper
   */
  @PostConstruct
  public void init() {
    try {
      // Створюємо директорію збережень якщо не існує
      Path saveDir = Paths.get(savePath);
      if (!Files.exists(saveDir)) {
        Files.createDirectories(saveDir);
        log.info("Created saves directory: {}", saveDir.toAbsolutePath());
      }

      log.info("SaveGameService initialized. Save path: {}", saveDir.toAbsolutePath());

    } catch (IOException e) {
      log.error("Failed to create saves directory", e);
      throw new RuntimeException("Cannot initialize SaveGameService", e);
    }
  }

  @Override
  public void save(GameContext context) {
    save(context, AUTOSAVE_NAME);
  }

  @Override
  public void save(GameContext context, String saveName) {
    if (context == null) {
      log.warn("Attempted to save null context");
      throw new IllegalArgumentException("GameContext cannot be null");
    }

    if (context.getPlayer() == null) {
      log.warn("Attempted to save context without player");
      throw new IllegalArgumentException("Player cannot be null");
    }

    log.debug("Saving game to: {}", saveName);

    try {
      // Конвертуємо GameContext в GameState для збереження
      GameState stateToSave = convertContextToState(context);
      stateToSave.setLastSaved(LocalDateTime.now());

      // Формуємо шлях до файлу
      String fileName = saveName.endsWith(FILE_EXTENSION) ? saveName : saveName + FILE_EXTENSION;
      File saveFile = new File(savePath, fileName);

      // Зберігаємо в JSON з форматуванням
      objectMapper.writerWithDefaultPrettyPrinter()
          .writeValue(saveFile, stateToSave);

      log.info("Game saved successfully: {}", saveFile.getAbsolutePath());

      // Публікуємо подію збереження
      eventPublisher.publishEvent(new GameSavedEvent(saveName));

    } catch (IOException e) {
      log.error("Failed to save game: {}", saveName, e);
      throw new RuntimeException("Failed to save game: " + e.getMessage(), e);
    }
  }

  @Override
  public GameContext load() {
    return load(AUTOSAVE_NAME);
  }

  @Override
  public GameContext load(String saveName) {
    log.debug("Loading game from: {}", saveName);

    try {
      String fileName = saveName.endsWith(FILE_EXTENSION) ? saveName : saveName + FILE_EXTENSION;
      File saveFile = new File(savePath, fileName);

      if (!saveFile.exists()) {
        log.warn("Save file not found: {}", saveFile.getAbsolutePath());
        return null;
      }

      // Завантажуємо GameState з JSON
      GameState loadedState = objectMapper.readValue(saveFile, GameState.class);

      // Конвертуємо GameState назад в GameContext
      GameContext context = convertStateToContext(loadedState);

      log.info("Game loaded successfully from: {}", saveFile.getAbsolutePath());
      log.debug("Loaded player: {}, score: {}",
          loadedState.getPlayer().getName(),
          loadedState.getTotalScore());

      // Публікуємо подію завантаження
      eventPublisher.publishEvent(
          new GameLoadedEvent(saveName, loadedState.getLastSaved())
      );

      return context;

    } catch (IOException e) {
      log.error("Failed to load game: {}", saveName, e);
      throw new RuntimeException("Failed to load game: " + e.getMessage(), e);
    }
  }

  @Override
  public List<String> listSaves() {
    log.debug("Listing available saves");

    try {
      Path saveDir = Paths.get(savePath);

      if (!Files.exists(saveDir)) {
        log.debug("Save directory does not exist");
        return new ArrayList<>();
      }

      List<String> saves = Files.list(saveDir)
          .filter(path -> path.toString().endsWith(FILE_EXTENSION))
          .map(path -> path.getFileName().toString())
          .sorted()
          .collect(Collectors.toList());

      log.debug("Found {} save file(s)", saves.size());

      return saves;

    } catch (IOException e) {
      log.error("Failed to list saves", e);
      return new ArrayList<>();
    }
  }

  @Override
  public boolean deleteSave(String saveName) {
    log.debug("Deleting save: {}", saveName);

    try {
      String fileName = saveName.endsWith(FILE_EXTENSION) ? saveName : saveName + FILE_EXTENSION;
      File saveFile = new File(savePath, fileName);

      if (!saveFile.exists()) {
        log.warn("Save file not found for deletion: {}", saveFile.getAbsolutePath());
        return false;
      }

      boolean deleted = saveFile.delete();

      if (deleted) {
        log.info("Save deleted successfully: {}", saveFile.getAbsolutePath());
      } else {
        log.warn("Failed to delete save: {}", saveFile.getAbsolutePath());
      }

      return deleted;

    } catch (Exception e) {
      log.error("Error deleting save: {}", saveName, e);
      return false;
    }
  }

  @Override
  public boolean exists(String saveName) {
    String fileName = saveName.endsWith(FILE_EXTENSION) ? saveName : saveName + FILE_EXTENSION;
    File saveFile = new File(savePath, fileName);
    boolean exists = saveFile.exists();

    log.debug("Save '{}' exists: {}", saveName, exists);

    return exists;
  }

  @Override
  public void quickSave(GameContext context) {
    log.debug("Performing quick save");
    save(context, QUICKSAVE_NAME);
  }

  @Override
  public GameContext quickLoad() {
    log.debug("Performing quick load");
    return load(QUICKSAVE_NAME);
  }

  /**
   * Конвертує GameContext в GameState для збереження
   */
  private GameState convertContextToState(GameContext context) {
    return GameState.builder()
        .player(context.getPlayer())
        .currentMission(context.getCurrentMission())
        .completedMissions(context.getCompletedMissions() != null
            ? new ArrayList<>(context.getCompletedMissions())
            : new ArrayList<>())
        .totalScore(context.getPlayer().getScore())
        .lastSaved(LocalDateTime.now())
        .gameOver(false)
        .build();
  }

  /**
   * Конвертує GameState назад в GameContext після завантаження
   */
  private GameContext convertStateToContext(GameState state) {
    return GameContext.builder()
        .player(state.getPlayer())
        .currentMission(state.getCurrentMission())
        .eventPublisher(eventPublisher)
        .suspicionLevel(0) // Можна зберігати в GameState якщо потрібно
        .coverIntegrity(100) // Можна зберігати в GameState якщо потрібно
        .inventory(state.getPlayer().getInventory() != null
            ? state.getPlayer().getInventory()
            : new Inventory())
        .completedMissions(state.getCompletedMissions() != null
            ? new ArrayList<>(state.getCompletedMissions())
            : new ArrayList<>())
        .turnNumber(0)
        .build();
  }

  /**
   * Отримує інформацію про збереження без повного завантаження
   */
  public SaveInfo getSaveInfo(String saveName) {
    try {
      String fileName = saveName.endsWith(FILE_EXTENSION) ? saveName : saveName + FILE_EXTENSION;
      File saveFile = new File(savePath, fileName);

      if (!saveFile.exists()) {
        return null;
      }

      GameState state = objectMapper.readValue(saveFile, GameState.class);

      return new SaveInfo(
          saveName,
          state.getPlayer().getName(),
          state.getTotalScore(),
          state.getLastSaved(),
          state.getCompletedMissions() != null ? state.getCompletedMissions().size() : 0,
          state.getCurrentMission() != null ? state.getCurrentMission().getTitle() : "None"
      );

    } catch (Exception e) {
      log.error("Failed to get save info: {}", saveName, e);
      return null;
    }
  }

  /**
   * Клас з інформацією про збереження
   */
  public record SaveInfo(
      String fileName,
      String playerName,
      int score,
      LocalDateTime savedAt,
      int completedMissions,
      String currentMission
  ) {

    @Override
    public String toString() {
      return String.format("%s - %s (Score: %d, Missions: %d, Current: %s) [%s]",
          fileName, playerName, score, completedMissions, currentMission, savedAt);
    }
  }
}