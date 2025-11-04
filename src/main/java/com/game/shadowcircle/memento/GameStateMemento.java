package com.game.shadowcircle.memento;

import com.game.shadowcircle.model.GameState;
import com.game.shadowcircle.model.Player;
import com.game.shadowcircle.model.Mission;
import com.game.shadowcircle.model.Item;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import lombok.Data;

@Data
public class GameStateMemento {

  private final String playerName;
  private final int score;
  private final int health;
  private final int stealth;
  private final int intelligence;
  private final int charisma;
  private final int coverIntegrity;
  private final String currentMissionId;
  private final String currentSceneId;
  private final List<String> completedMissions;
  private final List<Item> inventory;
  private final int suspicionLevel;
  private final LocalDateTime timestamp;

  // Private constructor, доступний тільки через GameState
  private GameStateMemento(GameState state) {
    Player player = state.getPlayer();
    this.playerName = player.getName();
    this.score = player.getScore();
    this.health = player.getHealth();
    this.stealth = player.getStealth();
    this.intelligence = player.getIntelligence();
    this.charisma = player.getCharisma();
    this.coverIntegrity = player.getCoverIntegrity();

    Mission currentMission = state.getCurrentMission();
    this.currentMissionId = currentMission != null ? currentMission.getTitle() : null;
    this.currentSceneId = null; // Додати логіку для збереження поточної сцени

    this.completedMissions = new ArrayList<>(); // Додати логіку для збереження завершених місій
    this.inventory = player.getInventory() != null ?
        new ArrayList<>(player.getInventory().getItems()) : new ArrayList<>();
    this.suspicionLevel = 0; // Додати через GameContext
    this.timestamp = LocalDateTime.now();
  }

  public static GameStateMemento createFrom(GameState state) {
    return new GameStateMemento(state);
  }
}