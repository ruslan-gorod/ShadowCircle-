package com.game.shadowcircle.command;

import com.game.shadowcircle.model.Choice;
import com.game.shadowcircle.model.DecisionResult;
import com.game.shadowcircle.model.GameContext;
import com.game.shadowcircle.model.Player;
import com.game.shadowcircle.strategy.DecisionStrategy;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class MakeChoiceCommand implements GameCommand {

  private final Choice choice;
  private final DecisionStrategy strategy;
  private final GameContext context;

  private DecisionResult result;
  private GameContext previousContext;

  @Override
  public void execute() {
    // Зберігаємо попередній стан для можливості відміни
    previousContext = context.deepCopy();

    // Виконуємо рішення
    result = strategy.evaluate(choice, context.getPlayer(), context);

    // Застосовуємо зміни
    applyResult(result, context);

    // Записуємо в історію
    context.recordChoice(choice);
  }

  @Override
  public void undo() {
    if (previousContext != null) {
      // TODO Відновлюємо попередній контекст
      // (потребує реалізації методів копіювання стану)
    }
  }

  @Override
  public boolean canExecute() {
    return context.getPlayer().isAlive() &&
        context.isCoverIntact();
  }

  private void applyResult(DecisionResult result, GameContext context) {
    Player player = context.getPlayer();

    player.setScore(player.getScore() + result.getScoreGain());
    player.setHealth(player.getHealth() + result.getHealthChange());
    context.addSuspicion(result.getSuspicionIncrease());

    // Додаємо предмети
    result.getItemsGained().forEach(item ->
        context.getInventory().addItem(item)
    );

    // Застосовуємо зміни у відносинах
    result.getRelationshipChanges().forEach((npc, change) ->
        context.improveRelationship(npc, change)
    );
  }
}