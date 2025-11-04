package com.game.shadowcircle.state;

import com.game.shadowcircle.events.GameEvent;
import com.game.shadowcircle.events.GameEventPublisher;
import com.game.shadowcircle.model.GameContext;
import java.util.ArrayDeque;
import java.util.Deque;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameStateMachine {

  private final GameEventPublisher eventPublisher;
  private final Deque<State> stateHistory = new ArrayDeque<>();
  private State currentState;

  /**
   * Перехід до нового стану
   */
  public void transitionTo(State newState) {
    if (currentState != null) {
      log.debug("Exit from the state: {}", currentState.getClass().getSimpleName());
      currentState.exit(null);
      stateHistory.addLast(currentState);
    }

    log.debug("Transition to state: {}", newState.getClass().getSimpleName());
    currentState = newState;
    currentState.enter(null);

    eventPublisher.publishEvent(
        GameEvent.of("STATE_CHANGED",
            "New state: " + newState.getClass().getSimpleName())
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
  public State handleInput(String input, GameContext context) {
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
