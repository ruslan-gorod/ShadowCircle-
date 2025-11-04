package com.game.shadowcircle.factory;

/**
 * Рівень складності гри
 */
public enum Difficulty {
  TRAINEE("Rookie", 1.0, 150, 0.5),
  AGENT("Agent", 1.5, 100, 0.75),
  VETERAN("Veteran", 2.0, 80, 1.0),
  ELITE("Elite", 2.5, 60, 1.5),
  LEGENDARY("Legendary", 3.0, 50, 2.0);

  private final String displayName;
  private final double scoreMultiplier;
  private final int baseSuspicionThreshold;
  private final double enemyDifficultyMultiplier;

  Difficulty(String displayName, double scoreMultiplier,
      int baseSuspicionThreshold, double enemyDifficultyMultiplier) {
    this.displayName = displayName;
    this.scoreMultiplier = scoreMultiplier;
    this.baseSuspicionThreshold = baseSuspicionThreshold;
    this.enemyDifficultyMultiplier = enemyDifficultyMultiplier;
  }

  public String getDisplayName() {
    return displayName;
  }

  public double getScoreMultiplier() {
    return scoreMultiplier;
  }

  public int getBaseSuspicionThreshold() {
    return baseSuspicionThreshold;
  }

  public double getEnemyDifficultyMultiplier() {
    return enemyDifficultyMultiplier;
  }

  public int calculateReward(int baseReward) {
    return (int) (baseReward * scoreMultiplier);
  }

  public int calculateRisk(int baseRisk) {
    return (int) (baseRisk * enemyDifficultyMultiplier);
  }

  public String getDescription() {
    return switch (this) {
      case TRAINEE -> "For beginners. The opponent is not very attentive.";
      case AGENT -> "Standard difficulty. Balanced game.";
      case VETERAN -> "For experienced agents. The opponent is attentive.";
      case ELITE -> "High difficulty. One mistake can be fatal.";
      case LEGENDARY -> "Maximum difficulty. Only for real professionals.";
    };
  }
}