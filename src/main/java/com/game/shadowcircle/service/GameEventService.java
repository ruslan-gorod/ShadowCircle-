package com.game.shadowcircle.service;

import com.game.shadowcircle.events.GameEvent;
import com.game.shadowcircle.events.GameEventListener;

public interface GameEventService {

  void registerListener(GameEventListener listener);

  void publishEvent(GameEvent event);
}
