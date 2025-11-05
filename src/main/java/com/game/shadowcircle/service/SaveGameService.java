package com.game.shadowcircle.service;

import com.game.shadowcircle.model.GameContext;
import java.util.List;

/**
 * Інтерфейс сервісу збереження гри
 */
public interface SaveGameService {

  void save(GameContext context);

  void save(GameContext context, String saveName);

  GameContext load();

  GameContext load(String saveName);

  List<String> listSaves();

  boolean deleteSave(String saveName);

  boolean exists(String saveName);

  void quickSave(GameContext context);

  GameContext quickLoad();
}