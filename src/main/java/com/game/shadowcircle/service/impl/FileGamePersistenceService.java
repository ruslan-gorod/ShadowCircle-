package com.game.shadowcircle.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.shadowcircle.model.GameState;
import com.game.shadowcircle.service.GamePersistenceService;
import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileGamePersistenceService implements GamePersistenceService {

  private final ObjectMapper mapper = new ObjectMapper();

  @Value("${game.save.path}")
  private String savePath;

  @Override
  public void save(GameState state) {
    try {
      File file = new File(savePath, "save.json");
      mapper.writerWithDefaultPrettyPrinter().writeValue(file, state);
      log.info("Гру збережено у {}", file.getAbsolutePath());
    } catch (IOException e) {
      log.error("Помилка при збереженні гри", e);
      throw new RuntimeException("Не вдалося зберегти гру", e);
    }
  }

  @Override
  public GameState load() {
    try {
      File file = new File(savePath, "save.json");
      GameState state = mapper.readValue(file, GameState.class);
      log.info("Гру успішно завантажено з {}", file.getAbsolutePath());
      return state;
    } catch (IOException e) {
      log.error("Помилка при завантаженні гри", e);
      throw new RuntimeException("Не вдалося завантажити гру", e);
    }
  }
}
