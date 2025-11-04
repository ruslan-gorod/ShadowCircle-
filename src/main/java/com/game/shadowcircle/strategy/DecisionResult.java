package com.game.shadowcircle.strategy;

import com.game.shadowcircle.model.Item;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DecisionResult {

  private final boolean success;
  private final int scoreGain;
  private final int riskPenalty;
  private final int suspicionIncrease;
  private final int healthChange;
  private final String message;
  @Builder.Default
  private final List<Item> itemsGained = new ArrayList<>();

  public String getFullMessage() {
    return message + (success ? " (+" + scoreGain + " очок)" : " (-" + riskPenalty + " штраф)");
  }
}