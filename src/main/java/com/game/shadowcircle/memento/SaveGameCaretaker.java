package com.game.shadowcircle.memento;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.game.shadowcircle.model.GameState;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Caretaker для Memento Pattern Відповідає за збереження та завантаження станів гри
 */
@Slf4j
@Service
public class SaveGameCaretaker {

  private static final int MAX_HISTORY_SIZE = 10;
  private final Stack<GameState> saveHistory = new Stack<>();
  private final ObjectMapper objectMapper;
  @Value("${game.save.path:./saves}")
  private String savePath;

  public SaveGameCaretaker() {
    this.objectMapper = new ObjectMapper();
    this.objectMapper.registerModule(new JavaTimeModule());
    this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  /**
   * Зберігає стан гри
   *
   * @param state    стан гри для збереження
   * @param saveName ім'я файлу збереження
   */
  public void save(GameState state, String saveName) {
    if (state == null) {
      log.warn("Attempting to save null state");
      return;
    }

    try {
      // Створюємо директорію якщо не існує
      Path saveDir = Paths.get(savePath);
      if (!Files.exists(saveDir)) {
        Files.createDirectories(saveDir);
        log.info("Created save directory: {}", saveDir);
      }

      // Оновлюємо час збереження
      state.setLastSaved(LocalDateTime.now());

      // Формуємо шлях до файлу
      String fileName = saveName.endsWith(".json") ? saveName : saveName + ".json";
      File saveFile = new File(saveDir.toFile(), fileName);

      // Зберігаємо в JSON
      objectMapper.writerWithDefaultPrettyPrinter()
          .writeValue(saveFile, state);

      // Додаємо в історію
      saveHistory.push(state.copy());

      // Обмежуємо розмір історії
      if (saveHistory.size() > MAX_HISTORY_SIZE) {
        saveHistory.remove(0);
      }

      log.info("Game saved successfully: {}", saveFile.getAbsolutePath());

    } catch (IOException e) {
      log.error("Error saving game: {}", saveName, e);
      throw new RuntimeException("Failed to save game: " + e.getMessage(), e);
    }
  }

  /**
   * Завантажує стан гри
   *
   * @param saveName ім'я файлу збереження
   * @return завантажений стан гри або null якщо файл не знайдено
   */
  public GameState load(String saveName) {
    try {
      String fileName = saveName.endsWith(".json") ? saveName : saveName + ".json";
      File saveFile = new File(savePath, fileName);

      if (!saveFile.exists()) {
        log.warn("Save file not found: {}", saveFile.getAbsolutePath());
        return null;
      }

      GameState state = objectMapper.readValue(saveFile, GameState.class);
      log.info("Game successfully loaded from: {}", saveFile.getAbsolutePath());

      return state;

    } catch (IOException e) {
      log.error("Error loading game: {}", saveName, e);
      throw new RuntimeException("Failed to load game: " + e.getMessage(), e);
    }
  }

  /**
   * Отримує список доступних збережень
   *
   * @return список імен файлів збережень
   */
  public List<String> listSaves() {
    List<String> saves = new ArrayList<>();

    try {
      Path saveDir = Paths.get(savePath);

      if (!Files.exists(saveDir)) {
        return saves;
      }

      Files.list(saveDir)
          .filter(path -> path.toString().endsWith(".json"))
          .forEach(path -> saves.add(path.getFileName().toString()));

      log.debug("Found saves: {}", saves.size());

    } catch (IOException e) {
      log.error("Error reading save directory", e);
    }

    return saves;
  }

  /**
   * Видаляє збереження
   *
   * @param saveName ім'я файлу збереження
   * @return true якщо успішно видалено
   */
  public boolean delete(String saveName) {
    try {
      String fileName = saveName.endsWith(".json") ? saveName : saveName + ".json";
      File saveFile = new File(savePath, fileName);

      if (saveFile.exists() && saveFile.delete()) {
        log.info("Save deleted: {}", saveFile.getAbsolutePath());
        return true;
      }

      return false;

    } catch (Exception e) {
      log.error("Error deleting save: {}", saveName, e);
      return false;
    }
  }

  /**
   * Відміняє останню дію (завантажує попередній стан)
   *
   * @return попередній стан або null якщо історія порожня
   */
  public GameState undo() {
    if (saveHistory.size() > 1) {
      saveHistory.pop(); // Видаляємо поточний
      GameState previousState = saveHistory.peek(); // Отримуємо попередній
      log.debug("Restored to previous state");
      return previousState.copy();
    }

    log.warn("No previous states to restore");
    return null;
  }

  /**
   * Очищає історію збережень
   */
  public void clearHistory() {
    saveHistory.clear();
    log.debug("Save history cleared");
  }

  /**
   * Отримує кількість збережень в історії
   *
   * @return розмір історії
   */
  public int getHistorySize() {
    return saveHistory.size();
  }

  /**
   * Перевіряє чи існує збереження
   *
   * @param saveName ім'я файлу збереження
   * @return true якщо існує
   */
  public boolean exists(String saveName) {
    String fileName = saveName.endsWith(".json") ? saveName : saveName + ".json";
    File saveFile = new File(savePath, fileName);
    return saveFile.exists();
  }

  /**
   * Отримує інформацію про збереження
   *
   * @param saveName ім'я файлу збереження
   * @return інформація про збереження або null
   */
  public SaveInfo getSaveInfo(String saveName) {
    try {
      GameState state = load(saveName);
      if (state == null) {
        return null;
      }

      return new SaveInfo(
          saveName,
          state.getPlayer().getName(),
          state.getTotalScore(),
          state.getLastSaved(),
          state.getCompletedMissions() != null ? state.getCompletedMissions().size() : 0
      );

    } catch (Exception e) {
      log.error("Error retrieving save information", e);
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
      int completedMissions
  ) {

    @Override
    public String toString() {
      return String.format("%s - %s (Account: %d, Missions: %d) [%s]",
          fileName, playerName, score, completedMissions, savedAt);
    }
  }
}