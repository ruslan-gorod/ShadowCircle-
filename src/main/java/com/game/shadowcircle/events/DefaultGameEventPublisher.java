package com.game.shadowcircle.events;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DefaultGameEventPublisher implements GameEventPublisher {

  private final List<GameEventListener> listeners = new ArrayList<>();

  @Override
  public void registerListener(GameEventListener listener) {
    listeners.add(listener);
    log.debug("Слухач зареєстровано: {}", listener.getClass().getSimpleName());
  }

  @Override
  public void removeListener(GameEventListener listener) {
    listeners.remove(listener);
    log.debug("Слухач видалено: {}", listener.getClass().getSimpleName());
  }

  @Override
  public void publishEvent(GameEvent event) {
    log.info("Публікація події: {} - {}", event.getType(), event.getMessage());
    for (GameEventListener listener : listeners) {
      listener.onGameEvent(event);
    }
  }
}

