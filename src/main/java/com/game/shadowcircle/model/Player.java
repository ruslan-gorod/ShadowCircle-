package com.game.shadowcircle.model;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Player {

  private String name;
  @Builder.Default
  private int score = 0;
  @Builder.Default
  private int health = 100;
  @Builder.Default
  private int stealth = 50;
  @Builder.Default
  private int intelligence = 50;
  @Builder.Default
  private int charisma = 50;
  private Inventory inventory;
  @Builder.Default
  private int coverIntegrity = 100;
  @Builder.Default
  private int suspicionLevel = 10;
  @Builder.Default
  private List<String> skills = new ArrayList<>();
  @Builder.Default
  private String currentDisguise = "civilian";

  public boolean isAlive() {
    return health > 0;
  }

  public boolean isCompromised() {
    return coverIntegrity <= 0;
  }

  public void damage(int amount) {
    this.health = Math.max(0, this.health - amount);
  }

  public void heal(int amount) {
    this.health = Math.min(100, this.health + amount);
  }

  public void addScore(int points) {
    this.score += points;
  }

  public void applyRisk(int riskLevel) {
    // Логіка застосування ризику
    this.health -= riskLevel / 2;
    this.coverIntegrity -= riskLevel;
  }

  public void addSkill(String skill) {
    if (!skills.contains(skill)) {
      skills.add(skill);
    }
  }

  public boolean hasSkill(String skill) {
    return skills.contains(skill);
  }

  public Player copy() {
    Player copy = new Player();
    copy.setName(this.name);
    copy.setScore(this.score);
    copy.setHealth(this.health);
    copy.setStealth(this.stealth);
    copy.setIntelligence(this.intelligence);

    if (this.inventory != null) {
      copy.setInventory(this.inventory.copy());
    }

    return copy;
  }
}