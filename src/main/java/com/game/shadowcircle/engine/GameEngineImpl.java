package com.game.shadowcircle.engine;

import com.game.shadowcircle.chain.ChoiceValidatorChain;
import com.game.shadowcircle.command.GameCommand;
import com.game.shadowcircle.command.MakeChoiceCommand;
import com.game.shadowcircle.events.ChoiceMadeEvent;
import com.game.shadowcircle.events.CriticalSituationEvent;
import com.game.shadowcircle.events.GameEventPublisher;
import com.game.shadowcircle.events.GameOverEvent;
import com.game.shadowcircle.events.GameStartedEvent;
import com.game.shadowcircle.events.InvalidChoiceEvent;
import com.game.shadowcircle.events.ItemAcquiredEvent;
import com.game.shadowcircle.events.PlayerHealthChangedEvent;
import com.game.shadowcircle.events.SceneTransitionEvent;
import com.game.shadowcircle.events.SuspicionChangedEvent;
import com.game.shadowcircle.factory.DecisionStrategyFactory;
import com.game.shadowcircle.memento.SaveGameCaretaker;
import com.game.shadowcircle.model.Choice;
import com.game.shadowcircle.model.DecisionResult;
import com.game.shadowcircle.model.GameContext;
import com.game.shadowcircle.model.GameState;
import com.game.shadowcircle.model.Inventory;
import com.game.shadowcircle.model.Item;
import com.game.shadowcircle.model.Player;
import com.game.shadowcircle.model.Scene;
import com.game.shadowcircle.model.ValidationResult;
import com.game.shadowcircle.service.CommandProcessor;
import com.game.shadowcircle.service.DialogueService;
import com.game.shadowcircle.service.PlayerService;
import com.game.shadowcircle.service.SaveGameService;
import com.game.shadowcircle.state.GameOverState;
import com.game.shadowcircle.state.GameStateMachine;
import com.game.shadowcircle.state.State;
import com.game.shadowcircle.strategy.DecisionStrategy;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameEngineImpl implements GameEngine {

  private final PlayerService playerService;
  private final DialogueService dialogueService;
  private final CommandProcessor commandProcessor;
  private final GameStateMachine stateMachine;
  private final DecisionStrategyFactory strategyFactory;
  private final ChoiceValidatorChain validatorChain;
  private final GameEventPublisher eventPublisher;
  private final SaveGameCaretaker saveGameCaretaker;
  private final SaveGameService saveGameService;
  private final State mainMenuState;

  private GameContext context;
  private boolean gameOver = false;

  @Override
  public void startNewGame(String playerName) {
    log.info("Starting a new game for a player: {}", playerName);

    Player player = playerService.createPlayer(playerName);

    context = GameContext.builder()
        .player(player)
        .eventPublisher(eventPublisher)
        .suspicionLevel(0)
        .coverIntegrity(100)
        .inventory(player.getInventory() != null ? player.getInventory() : new Inventory())
        .turnNumber(0)
        .build();

    gameOver = false;

    // Перехід до головного меню
    stateMachine.transitionTo(mainMenuState);

    // Публікація події
    eventPublisher.publishEvent(new GameStartedEvent(player));

    dialogueService.print("The game has started! Good luck, agent " + playerName + "!");
    log.info("The game has started successfully for player {}", playerName);
  }

  @Override
  public void loadGame() {
    log.info("Loading saved game...");

    try {
      // Завантажуємо стан гри через Memento Pattern
      GameContext loadedContext = saveGameService.load();

      if (loadedContext == null) {
        dialogueService.print("Save file not found.");
        log.warn("Save file not found");
        return;
      }

      // Відновлюємо контекст
      context = loadedContext;
      gameOver = false;

      // Оновлюємо сервіс гравця
      playerService.updatePlayer(loadedContext.getPlayer());

      dialogueService.print("Game successfully loaded!");
      dialogueService.print(String.format("Agent: %s, Account: %d",
          loadedContext.getPlayer().getName(),
          loadedContext.getPlayer().getScore()));

      log.info("Game successfully loaded for player {}", loadedContext.getPlayer().getName());

      // Переходимо до головного меню
      stateMachine.transitionTo(mainMenuState);

    } catch (Exception e) {
      log.error("Error loading game", e);
      dialogueService.print("Failed to load game: " + e.getMessage());
    }
  }

  @Override
  public void saveGame() {
    log.info("Saving game...");

    if (context == null) {
      dialogueService.print("No active game to save.");
      log.warn("Attempting to save without active game");
      return;
    }

    try {
      saveGameService.save(context);

      dialogueService.print("Game saved successfully!");
      log.info("Game saved successfully for player {}", context.getPlayer().getName());

    } catch (Exception e) {
      log.error("Game save error", e);
      dialogueService.print("Game save failed: " + e.getMessage());
    }
  }

  @Override
  public void playScene(Scene scene) {
    if (scene == null) {
      log.warn("Attempting to play null scene");
      return;
    }

    log.debug("Playing scene: {}", scene.getId());

    // Оновлюємо поточну сцену в контексті
    if (context != null) {
      context.setCurrentScene(scene);
    }

    // Відображаємо наративний текст
    dialogueService.print("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    dialogueService.print(scene.getNarrativeText());
    dialogueService.print("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

    // Якщо є вибори, відображаємо їх
    if (scene.getChoices() != null && !scene.getChoices().isEmpty()) {
      dialogueService.print("\nAvailable options:");
      for (int i = 0; i < scene.getChoices().size(); i++) {
        Choice choice = scene.getChoices().get(i);
        dialogueService.print(String.format("  %d. %s", i + 1, choice.getDescription()));

        // Показуємо ризик якщо високий
        if (choice.getRiskLevel() > 30) {
          dialogueService.print(String.format("     Risk: %d", choice.getRiskLevel()));
        }
      }
      dialogueService.print("");
    } else if (scene.isEndingScene()) {
      dialogueService.print("\nFinal scene");
    }

    // Публікуємо подію переходу до сцени
    eventPublisher.publishEvent(
        new SceneTransitionEvent(context.getCurrentScene(), scene)
    );

    log.debug("Scene {} successfully played", scene.getId());
  }

  @Override
  public void applyChoice(Choice choice) {
    if (choice == null) {
      log.warn("Attempting to apply null choice");
      return;
    }

    log.debug("Applying choice: {}", choice.getDescription());

    // Викликаємо розширений метод makeChoice
    makeChoice(choice);
  }

  /**
   * Розширений метод обробки вибору з повною логікою
   */
  public void makeChoice(Choice choice) {
    log.debug("Processing choice: {}", choice.getDescription());

    // 1. Валідація вибору
    ValidationResult validation = validatorChain.validate(choice, context);
    if (!validation.isValid()) {
      log.warn("Choice validation failed: {}", validation.getReason());
      eventPublisher.publishEvent(new InvalidChoiceEvent(validation.getReason()));
      dialogueService.print(validation.getReason());
      return;
    }

    // Показуємо попередження якщо є
    if (validation.hasWarnings()) {
      for (String warning : validation.getWarnings()) {
        dialogueService.print("⚠️ " + warning);
      }
    }

    // 2. Вибір стратегії на основі типу вибору
    String strategyType = determineStrategyType(choice);
    DecisionStrategy strategy = strategyFactory.getStrategy(strategyType, "BALANCED");

    log.debug("Selected strategy: {}", strategyType);

    // 3. Виконання через Command Pattern
    GameCommand command = new MakeChoiceCommand(choice, strategy, context);

    // Перевірка чи можна виконати команду
    if (!command.canExecute()) {
      log.warn("The command cannot be executed");
      dialogueService.print("This action cannot be performed at this time");
      return;
    }

    commandProcessor.execute(command);

    // 4. Отримання результату
    DecisionResult result = strategy.evaluate(choice, context.getPlayer(), context);

    // 5. Застосування результату
    applyDecisionResult(result);

    // 6. Публікація події
    eventPublisher.publishEvent(new ChoiceMadeEvent(choice, result));

    // 7. Перехід до наступної сцени якщо є
    if (result.getNextSceneId() != null && !result.getNextSceneId().isEmpty()) {
      transitionToNextScene(result.getNextSceneId());
    }

    // 8. Перевірка умов завершення гри
    checkGameOverConditions();

    log.debug("Selection processed successfully");
  }

  /**
   * Застосовує результат рішення до гри
   */
  private void applyDecisionResult(DecisionResult result) {
    Player player = context.getPlayer();

    log.debug("Applying solution result");

    // Очки
    if (result.getScoreGain() > 0) {
      playerService.addScore(result.getScoreGain());
      dialogueService.print(String.format("+%d points", result.getScoreGain()));
    }

    // Здоров'я
    if (result.getHealthChange() != 0) {
      int oldHealth = player.getHealth();
      player.setHealth(Math.max(0, Math.min(100, player.getHealth() + result.getHealthChange())));

      eventPublisher.publishEvent(
          new PlayerHealthChangedEvent(oldHealth, player.getHealth(), "choice_consequence")
      );

      dialogueService.print(String.format("Health: %d → %d",
          oldHealth, player.getHealth()));
    }

    // Підозра
    if (result.getSuspicionIncrease() != 0) {
      int oldSuspicion = context.getSuspicionLevel();
      context.addSuspicion(result.getSuspicionIncrease());

      eventPublisher.publishEvent(
          new SuspicionChangedEvent(oldSuspicion, context.getSuspicionLevel(), "choice_consequence")
      );

      if (result.getSuspicionIncrease() > 0) {
        dialogueService.print(String.format("Suspicion level: %d → %d",
            oldSuspicion, context.getSuspicionLevel()));

        if (context.getSuspicionLevel() > 80) {
          dialogueService.print("WARNING: Very high suspicion level!");
        }
      }
    }

    // Цілісність легенди
    if (result.getCoverDamage() > 0) {
      int oldCover = context.getCoverIntegrity();
      context.setCoverIntegrity(Math.max(0, oldCover - result.getCoverDamage()));

      dialogueService.print(String.format("Legend Integrity: %d → %d",
          oldCover, context.getCoverIntegrity()));

      if (context.getCoverIntegrity() < 30) {
        dialogueService.print("CRITICAL: Legend in danger!");
      }
    }

    // Предмети
    for (Item item : result.getItemsGained()) {
      context.getInventory().addItem(item);
      eventPublisher.publishEvent(new ItemAcquiredEvent(item, player));
      dialogueService.print(String.format("Received: %s", item.getName()));
    }

    // Зміни атрибутів
    if (!result.getAttributeChanges().isEmpty()) {
      result.getAttributeChanges().forEach((attr, change) -> {
        dialogueService.print(String.format("%s: %+d", attr, change));
      });
    }

    // Зміни у відносинах
    if (!result.getRelationshipChanges().isEmpty()) {
      result.getRelationshipChanges().forEach((npc, change) -> {
        context.improveRelationship(npc, change);
        dialogueService.print(String.format("Relationship with %s: %+d", npc, change));
      });
    }

    // Основне повідомлення
    if (result.getMessage() != null && !result.getMessage().isEmpty()) {
      dialogueService.print("\n" + result.getMessage());
    }

    // Додаткова інформація
    if (result.getAdditionalInfo() != null && !result.getAdditionalInfo().isEmpty()) {
      dialogueService.print(result.getAdditionalInfo());
    }

    // Наслідки
    if (!result.getConsequences().isEmpty()) {
      dialogueService.print("\n Consequences:");
      for (String consequence : result.getConsequences()) {
        dialogueService.print("  • " + consequence);
      }
    }

    // Критична подія
    if (result.isTriggersCriticalEvent()) {
      handleCriticalEvent(result);
    }

    log.debug("Solution result applied");
  }

  /**
   * Обробка критичної події
   */
  private void handleCriticalEvent(DecisionResult result) {
    dialogueService.print("\n CRITICAL EVENT!");

    CriticalSituationEvent.SituationType situationType;

    if (!context.isCoverIntact()) {
      situationType = CriticalSituationEvent.SituationType.COVER_BLOWN;
    } else if (context.getPlayer().getHealth() < 20) {
      situationType = CriticalSituationEvent.SituationType.LOW_HEALTH;
    } else if (context.getSuspicionLevel() > 80) {
      situationType = CriticalSituationEvent.SituationType.HIGH_SUSPICION;
    } else {
      situationType = CriticalSituationEvent.SituationType.MISSION_CRITICAL;
    }

    eventPublisher.publishEvent(
        new CriticalSituationEvent(situationType, result.getMessage())
    );
  }

  /**
   * Перевірка умов завершення гри
   */
  private void checkGameOverConditions() {
    // Перевірка смерті
    if (!context.getPlayer().isAlive()) {
      log.info("Game over: player died");
      gameOver = true;
      stateMachine.transitionTo(new GameOverState(eventPublisher));

      eventPublisher.publishEvent(
          new GameOverEvent(
              GameOverEvent.GameOverReason.DEATH,
              context.getPlayer().getScore(),
              context.getPlayer()
          )
      );

      dialogueService.print("\nYOU DIED");
      dialogueService.print("Final score: " + context.getPlayer().getScore());
    }
    // Перевірка викриття
    else if (!context.isCoverIntact()) {
      log.info("Game over: legend revealed");
      gameOver = true;
      stateMachine.transitionTo(new GameOverState(eventPublisher));

      eventPublisher.publishEvent(
          new GameOverEvent(
              GameOverEvent.GameOverReason.COMPROMISED,
              context.getPlayer().getScore(),
              context.getPlayer()
          )
      );

      dialogueService.print("\nYOUR LEGEND HAS BEEN EXPOSED!");
      dialogueService.print("Mission failed. Final score: " + context.getPlayer().getScore());
    }
  }

  /**
   * Перехід до наступної сцени
   */
  private void transitionToNextScene(String sceneId) {
    if (context.getCurrentMission() == null) {
      log.warn("Unable to go to scene: no active mission");
      return;
    }

    Scene nextScene = context.getCurrentMission().getScenes().stream()
        .filter(s -> s.getId().equals(sceneId))
        .findFirst()
        .orElse(null);

    if (nextScene != null) {
      log.debug("Going to next scene: {}", sceneId);
      playScene(nextScene);
    } else {
      log.warn("Scene {} not found", sceneId);
    }
  }

  /**
   * Визначає тип стратегії на основі опису вибору
   */
  private String determineStrategyType(Choice choice) {
    String description = choice.getDescription().toLowerCase();

    if (description.contains("hidden") || description.contains("quietly")) {
      return "STEALTH";
    } else if (description.contains("aggressive") || description.contains("attack")
        || description.contains("strengths")) {
      return "AGGRESSIVE";
    } else if (description.contains("negotiation") || description.contains("diplomat")
        || description.contains("conversations")) {
      return "DIPLOMATIC";
    } else if (description.contains("tech") || description.contains("hacking")
        || description.contains("device")) {
      return "TECHNICAL";
    }

    return "BALANCED";
  }

  @Override
  public GameState getCurrentState() {
    if (context == null) {
      log.warn("GameContext is null");
      return null;
    }

    // Створюємо об'єкт GameState з поточного контексту
    return GameState.builder()
        .player(context.getPlayer())
        .currentMission(context.getCurrentMission())
        .completedMissions(context.getCompletedMissions())
        .totalScore(context.getPlayer().getScore())
        .lastSaved(LocalDateTime.now())
        .gameOver(gameOver)
        .build();
  }

  @Override
  public void processInput(String input) {
    if (context == null) {
      log.warn("Attempting to process input without active context");
      dialogueService.print("No active game. Start a new game.");
      return;
    }

    if (gameOver) {
      log.debug("Game over, input ignored");
      return;
    }

    log.debug("Processing input: {}", input);

    // Передаємо введення в State Machine
    stateMachine.handleInput(input, context);
  }

  @Override
  public boolean isGameOver() {
    return gameOver || (context != null &&
        (!context.getPlayer().isAlive() || !context.isCoverIntact()));
  }

  /**
   * Відновлює GameContext з GameState (для завантаження)
   */
  private GameContext restoreContextFromGameState(GameState savedState) {
    return GameContext.builder()
        .player(savedState.getPlayer())
        .currentMission(savedState.getCurrentMission())
        .eventPublisher(eventPublisher)
        .suspicionLevel(0) // Можна зберегти в GameState якщо потрібно
        .coverIntegrity(100) // Можна зберегти в GameState якщо потрібно
        .inventory(savedState.getPlayer().getInventory())
        .completedMissions(savedState.getCompletedMissions())
        .turnNumber(0)
        .build();
  }

  /**
   * Скидає стан гри
   */
  public void resetGame() {
    log.info("Resetting game state");
    context = null;
    gameOver = false;
    commandProcessor.clearHistory();
  }
}