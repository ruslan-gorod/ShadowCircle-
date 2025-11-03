package com.game.shadowcircle.service;

import com.game.shadowcircle.model.GameState;

public interface GamePersistenceService {

  void save(GameState state);

  GameState load();
}

