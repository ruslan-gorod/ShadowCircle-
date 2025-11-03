package com.game.shadowcircle.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Choice {

  private String description;
  private String nextSceneId;
  private int riskLevel;
  private int rewardPoints;
  private Item itemReward;
}
