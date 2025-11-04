package com.game.shadowcircle.service;

import com.game.shadowcircle.command.GameCommand;
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
public class CommandProcessor {

  private final Deque<GameCommand> executedCommands = new ArrayDeque<>();
  private final GameEventPublisher eventPublisher;
  private final int maxHistorySize = 50;

  /**
   * Виконує команду
   */
  public void execute(GameCommand command, GameContext context) {
    log.debug("Виконання команди: {}", command.getClass().getSimpleName());

    if (!command.canExecute(context)) {
      log.warn("Команду не можна виконати: {}", command.getClass().getSimpleName());
      return;
    }

    command.execute(context);

    // Зберігаємо команду в історії для можливості відміни
    executedCommands.addLast(command);

    // Обмежуємо розмір історії
    if (executedCommands.size() > maxHistorySize) {
      executedCommands.removeFirst();
    }

    // Публікуємо подію про виконання команди
    eventPublisher.publishEvent(
        GameEvent.builder()
            .type("COMMAND_EXECUTED")
            .message("Виконано: " + command.getClass().getSimpleName())
            .build()
    );
  }

  /**
   * Відміняє останню команду
   */
  public boolean undo(GameContext context) {
    if (executedCommands.isEmpty()) {
      log.warn("Немає команд для відміни");
      return false;
    }

    GameCommand lastCommand = executedCommands.removeLast();
    log.debug("Відміна команди: {}", lastCommand.getClass().getSimpleName());

    lastCommand.undo(context);

    eventPublisher.publishEvent(
        GameEvent.builder()
            .type("COMMAND_UNDONE")
            .message("Відмінено: " + lastCommand.getClass().getSimpleName())
            .build()
    );

    return true;
  }

  /**
   * Очищає історію команд
   */
  public void clearHistory() {
    executedCommands.clear();
    log.debug("Історію команд очищено");
  }

  /**
   * Повертає кількість команд в історії
   */
  public int getHistorySize() {
    return executedCommands.size();
  }
}