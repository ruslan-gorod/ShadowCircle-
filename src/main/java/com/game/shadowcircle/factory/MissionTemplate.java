package com.game.shadowcircle.factory;

import com.game.shadowcircle.composite.Objective;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MissionTemplate {

  private String type;
  private String baseTitle;
  private String description;
  private Objective primaryObjective;
  private List<String> sceneTemplates;
  private int baseTimeLimit;
  private int baseSuspicionThreshold;
  private List<Objective> possibleSecondaryObjectives;
}