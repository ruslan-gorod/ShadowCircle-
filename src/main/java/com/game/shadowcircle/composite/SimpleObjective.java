package com.game.shadowcircle.composite;

import com.game.shadowcircle.model.GameContext;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor(force = true)
@AllArgsConstructor
public class SimpleObjective implements Objective {

  private final String id;
  private final String description;
  private final int points;
  private final Predicate<GameContext> completionCondition;

  @Override
  public boolean isCompleted(GameContext context) {
    return completionCondition.test(context);
  }

  @Override
  public int getRewardPoints() {
    return points;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public List<Objective> getSubObjectives() {
    return Collections.emptyList();
  }
}
