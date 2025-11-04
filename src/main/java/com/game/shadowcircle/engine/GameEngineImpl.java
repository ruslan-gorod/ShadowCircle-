package com.game.shadowcircle.engine;

import com.game.shadowcircle.chain.ChoiceValidatorChain;
import com.game.shadowcircle.chain.ValidationResult;
import com.game.shadowcircle.command.GameCommand;
import com.game.shadowcircle.command.MakeChoiceCommand;
import com.game.shadowcircle.events.ChoiceMadeEvent;
import com.game.shadowcircle.events.GameEventPublisher;
import com.game.shadowcircle.events.GameOverEvent;
import com.game.shadowcircle.events.GameStartedEvent;
import com.game.shadowcircle.events.InvalidChoiceEvent;
import com.game.shadowcircle.events.ItemAcquiredEvent;
import com.game.shadowcircle.events.PlayerHealthChangedEvent;
import com.game.shadowcircle.events.SuspicionChangedEvent;
import com.game.shadowcircle.factory.DecisionStrategyFactory;
import com.game.shadowcircle.model.Choice;
import com.game.shadowcircle.model.GameContext;
import com.game.shadowcircle.model.GameState;
import com.game.shadowcircle.model.Inventory;
import com.game.shadowcircle.model.Item;
import com.game.shadowcircle.model.Player;
import com.game.shadowcircle.model.Scene;
import com.game.shadowcircle.service.CommandProcessor;
import com.game.shadowcircle.service.DialogueService;
import com.game.shadowcircle.service.MissionService;
import com.game.shadowcircle.service.PlayerService;
import com.game.shadowcircle.state.GameOverState;
import com.game.shadowcircle.state.GameStateMachine;
import com.game.shadowcircle.state.MainMenuState;
import com.game.shadowcircle.strategy.DecisionResult;
import com.game.shadowcircle.strategy.DecisionStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameEngineImpl implements GameEngine {

  private final PlayerService playerService;
  private final DialogueService dialogueService;
  private final MissionService missionService;
  private final CommandProcessor commandProcessor;
  private final GameStateMachine stateMachine;
  private final DecisionStrategyFactory strategyFactory;
  private final ChoiceValidatorChain validatorChain;
  private final GameEventPublisher eventPublisher;

  private GameContext context;

  @Override
  public void startNewGame(String playerName) {
    Player player = playerService.createPlayer(playerName);

    context = GameContext.builder()
        .player(player)
        .eventPublisher(eventPublisher)
        .suspicionLevel(0)
        .coverIntegrity(100)
        .inventory(new Inventory())
        .build();

    stateMachine.transitionTo(new MainMenuState());
    eventPublisher.publishEvent(new GameStartedEvent(player));

    dialogueService.print("Гра розпочалась! Успіхів, агент " + playerName + "!");
  }

  @Override
  public void loadGame() {
    // Логіка завантаження гри
  }

  @Override
  public void saveGame() {
    // Логіка збереження гри
  }

  @Override
  public void playScene(Scene scene) {
    // Логіка відтворення сцени
    dialogueService.print(scene.getNarrativeText());
  }

  @Override
  public void applyChoice(Choice choice) {
    // 1. Валідація
    ValidationResult validation = validatorChain.validate(choice, context);
    if (!validation.isValid()) {
      eventPublisher.publishEvent(new InvalidChoiceEvent(validation.getReason()));
      dialogueService.print(validation.getReason());
      return;
    }

    // 2. Вибір стратегії
    DecisionStrategy strategy = strategyFactory.getStrategy(
        choice.getDescription(),
        "BALANCED"
    );

    // 3. Виконання через Command
    GameCommand command = new MakeChoiceCommand(choice);
    commandProcessor.execute(command, context);

    // 4. Отримання результату
    DecisionResult result = strategy.evaluate(choice, context.getPlayer(), context);

    // 5. Застосування результату
    applyDecisionResult(result);

    // 6. Публікація події
    eventPublisher.publishEvent(new ChoiceMadeEvent(choice, result));

    // 7. Перевірка завершення гри
    checkGameOverConditions();
  }

  private void applyDecisionResult(DecisionResult result) {
    Player player = context.getPlayer();

    // Очки
    if (result.getScoreGain() > 0) {
      player.addScore(result.getScoreGain());
    }

    // Здоров'я
    if (result.getHealthChange() != 0) {
      int oldHealth = player.getHealth();
      player.setHealth(player.getHealth() + result.getHealthChange());
      eventPublisher.publishEvent(
          new PlayerHealthChangedEvent(oldHealth, player.getHealth(), "choice_consequence")
      );
    }

    // Підозра
    if (result.getSuspicionIncrease() != 0) {
      int oldSuspicion = context.getSuspicionLevel();
      context.setSuspicionLevel(context.getSuspicionLevel() + result.getSuspicionIncrease());
      eventPublisher.publishEvent(
          new SuspicionChangedEvent(oldSuspicion, context.getSuspicionLevel(), "choice_consequence")
      );
    }

    // Предмети
    for (Item item : result.getItemsGained()) {
      context.getInventory().addItem(item);
      eventPublisher.publishEvent(new ItemAcquiredEvent(item, player));
    }

    // Повідомлення
    dialogueService.print(result.getFullMessage());
  }

  private void checkGameOverConditions() {
    if (!context.getPlayer().isAlive()) {
      stateMachine.transitionTo(new GameOverState());
      eventPublisher.publishEvent(
          new GameOverEvent(
              GameOverEvent.GameOverReason.DEATH,
              context.getPlayer().getScore(),
              context.getPlayer()
          )
      );
    } else if (!context.isCoverIntact()) {
      stateMachine.transitionTo(new GameOverState());
      eventPublisher.publishEvent(
          new GameOverEvent(
              GameOverEvent.GameOverReason.COMPROMISED,
              context.getPlayer().getScore(),
              context.getPlayer()
          )
      );
    }
  }

  @Override
  public GameState getCurrentState() {
    return GameState.builder()
        .player(context.getPlayer())
        .currentMission(context.getCurrentMission())
        .build();
  }
}