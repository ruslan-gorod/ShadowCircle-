package com.game.shadowcircle.state;

import com.game.shadowcircle.events.GameEvent;
import com.game.shadowcircle.events.GameEventPublisher;
import com.game.shadowcircle.model.GameContext;
import java.util.ArrayDeque;
import java.util.Deque;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Data
@Service
@RequiredArgsConstructor
public class GameStateMachine {

  private final GameEventPublisher eventPublisher;
  private final Deque<GameState> stateHistory = new ArrayDeque<>();
  private GameState currentState;

  /**
   * Перехід до нового стану
   */
  public void transitionTo(GameState newState) {
    if (currentState != null) {
      log.debug("Вихід зі стану: {}", currentState.getClass().getSimpleName());
      currentState.exit(null);
      stateHistory.addLast(currentState);
    }

    log.debug("Перехід до стану: {}", newState.getClass().getSimpleName());
    currentState = newState;
    currentState.enter(null);

    eventPublisher.publishEvent(
        GameEvent.of("STATE_CHANGED",
            "Новий стан: " + newState.getClass().getSimpleName())
    );
  }

  /**
   * Обробка поточного стану
   */
  public void update(GameContext context) {
    if (currentState != null) {
      currentState.update(context);
    }
  }

  /**
   * Обробка введення
   */
  public GameState handleInput(String input, GameContext context) {
    if (currentState != null) {
      return currentState.handleInput(input, context);
    }
    return currentState;
  }

  /**
   * Повернення до попереднього стану
   */
  public boolean returnToPreviousState() {
    if (stateHistory.isEmpty()) {
      return false;
    }

    currentState.exit(null);
    currentState = stateHistory.removeLast();
    currentState.enter(null);

    return true;
  }
}
