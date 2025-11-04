package com.game.shadowcircle.composite;

import com.game.shadowcircle.model.GameContext;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor(force = true)
@AllArgsConstructor
public class CompositeObjective implements Objective {

  private final String description;
  private final List<Objective> subObjectives = new ArrayList<>();
  private final int bonusPoints;

  @Override
  public boolean isCompleted(GameContext context) {
    return subObjectives.stream()
        .allMatch(obj -> obj.isCompleted(context));
  }

  @Override
  public int getRewardPoints() {
    int total = subObjectives.stream()
        .mapToInt(Objective::getRewardPoints)
        .sum();
    return isCompleted(null) ? total + bonusPoints : total;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public List<Objective> getSubObjectives() {
    return subObjectives;
  }
}
