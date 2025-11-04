package com.game.shadowcircle.factory;

public enum Difficulty {
  EASY("Легкий", 1.0),
  NORMAL("Нормальний", 1.5),
  HARD("Складний", 2.0),
  EXPERT("Експерт", 2.5);

  private final String displayName;
  private final double multiplier;

  Difficulty(String displayName, double multiplier) {
    this.displayName = displayName;
    this.multiplier = multiplier;
  }

  public String getDisplayName() {
    return displayName;
  }

  public double getMultiplier() {
    return multiplier;
  }
}
