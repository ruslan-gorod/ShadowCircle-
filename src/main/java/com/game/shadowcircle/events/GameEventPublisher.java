package com.game.shadowcircle.events;

public interface GameEventPublisher {

  void registerListener(GameEventListener listener);

  void removeListener(GameEventListener listener);

  void publishEvent(GameEvent event);
}

