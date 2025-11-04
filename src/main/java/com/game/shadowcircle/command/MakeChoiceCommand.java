package com.game.shadowcircle.command;

import com.game.shadowcircle.model.Choice;
import com.game.shadowcircle.model.GameContext;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor(force = true)
@AllArgsConstructor
public class MakeChoiceCommand extends BaseGameCommand {

  private final Choice choice;

  @Override
  public void execute(GameContext context) {
    context.getPlayer().addScore(Objects.requireNonNull(choice).getRewardPoints());
    context.getPlayer().applyRisk(choice.getRiskLevel());
    if (choice.getItemReward() != null) {
      context.getInventory().addItem(choice.getItemReward());
    }
    context.transitionToScene(choice.getNextSceneId());
  }

  @Override
  public void undo(GameContext context) {
    // Реалізація відміни для складних сценаріїв
  }

  @Override
  public boolean canExecute(GameContext context) {
    return super.canExecute(context) &&
        context.getPlayer().isAlive() &&
        !context.getPlayer().isCompromised();
  }
}