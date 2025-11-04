package com.game.shadowcircle.composite;

import com.game.shadowcircle.model.GameContext;
import java.util.List;

public interface Objective {

  boolean isCompleted(GameContext context);

  int getRewardPoints();

  String getDescription();

  List<Objective> getSubObjectives();
}
