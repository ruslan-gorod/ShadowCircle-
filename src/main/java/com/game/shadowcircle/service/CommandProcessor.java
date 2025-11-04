package com.game.shadowcircle.service;

import com.game.shadowcircle.command.GameCommand;
import com.game.shadowcircle.events.GameEvent;
import com.game.shadowcircle.events.GameEventPublisher;
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
  public void execute(GameCommand command) {
    log.debug("Command execution: {}", command.getClass().getSimpleName());

    command.execute();

    // Зберігаємо команду в історії для можливості відміни
    executedCommands.addLast(command);

    // Обмежуємо розмір історії
    if (executedCommands.size() > maxHistorySize) {
      executedCommands.removeFirst();
    }

    // Публікуємо подію про виконання команди
    eventPublisher.publishEvent(
        GameEvent.of("COMMAND_EXECUTED",
            "Done: " + command.getClass().getSimpleName())
    );
  }

  /**
   * Відміняє останню команду
   */
  public boolean undo() {
    if (executedCommands.isEmpty()) {
      log.warn("No commands to cancel");
      return false;
    }

    GameCommand lastCommand = executedCommands.removeLast();
    log.debug("Canceling a command: {}", lastCommand.getClass().getSimpleName());

    lastCommand.undo();

    eventPublisher.publishEvent(
        GameEvent.of("COMMAND_UNDONE",
            "Cancelled: " + lastCommand.getClass().getSimpleName())
    );

    return true;
  }

  /**
   * Очищає історію команд
   */
  public void clearHistory() {
    executedCommands.clear();
    log.debug("Command history cleared");
  }

  /**
   * Повертає кількість команд в історії
   */
  public int getHistorySize() {
    return executedCommands.size();
  }
}