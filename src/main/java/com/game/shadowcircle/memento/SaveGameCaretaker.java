package com.game.shadowcircle.memento;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.shadowcircle.model.GameState;
import com.game.shadowcircle.model.Inventory;
import com.game.shadowcircle.model.Player;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Stack;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaveGameCaretaker {

  private final Stack<GameStateMemento> saveHistory = new Stack<>();
  private final ObjectMapper mapper;
  private final String SAVE_DIRECTORY = "saves";

  public void save(GameState state, String saveName) {
    try {
      GameStateMemento memento = GameStateMemento.createFrom(state);
      saveHistory.push(memento);
      persistToFile(memento, saveName);
      log.info("Гру збережено: {}", saveName);
    } catch (Exception e) {
      log.error("Помилка збереження гри: {}", saveName, e);
    }
  }

  public GameState load(String saveName) {
    try {
      GameStateMemento memento = loadFromFile(saveName);
      return restoreFromMemento(memento);
    } catch (Exception e) {
      log.error("Помилка завантаження гри: {}", saveName, e);
      return null;
    }
  }

  public GameState undoLastAction() {
    if (saveHistory.size() > 1) {
      saveHistory.pop(); // Поточний стан
      GameStateMemento previousMemento = saveHistory.peek();
      return restoreFromMemento(previousMemento);
    }
    return null;
  }

  private void persistToFile(GameStateMemento memento, String saveName) throws IOException {
    Path saveDir = Paths.get(SAVE_DIRECTORY);
    if (!Files.exists(saveDir)) {
      Files.createDirectories(saveDir);
    }

    File saveFile = new File(SAVE_DIRECTORY, saveName + ".json");
    mapper.writeValue(saveFile, memento);
  }

  private GameStateMemento loadFromFile(String saveName) throws IOException {
    File saveFile = new File(SAVE_DIRECTORY, saveName + ".json");
    if (!saveFile.exists()) {
      throw new IOException("Файл збереження не знайдено: " + saveName);
    }
    return mapper.readValue(saveFile, GameStateMemento.class);
  }

  private GameState restoreFromMemento(GameStateMemento memento) {
    Player player = Player.builder()
        .name(memento.getPlayerName())
        .score(memento.getScore())
        .health(memento.getHealth())
        .stealth(memento.getStealth())
        .intelligence(memento.getIntelligence())
        .charisma(memento.getCharisma())
        .coverIntegrity(memento.getCoverIntegrity())
        .inventory(Inventory.builder()
            .items(new ArrayList<>(memento.getInventory()))
            .build())
        .build();

    return GameState.builder()
        .player(player)
        .currentMission(null) // Можна додати логіку відновлення місії
        .build();
  }

  public int getSaveHistorySize() {
    return saveHistory.size();
  }

  public void clearHistory() {
    saveHistory.clear();
    log.debug("Історію збережень очищено");
  }
}