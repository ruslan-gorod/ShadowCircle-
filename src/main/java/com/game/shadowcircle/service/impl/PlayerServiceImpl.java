package com.game.shadowcircle.service.impl;

import com.game.shadowcircle.model.Player;
import com.game.shadowcircle.service.PlayerService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PlayerServiceImpl implements PlayerService {

  @Getter
  private Player currentPlayer;

  @Override
  public Player createPlayer(String name) {
    currentPlayer = new Player();
    currentPlayer.setName(name);
    currentPlayer.setScore(0);
    currentPlayer.setHealth(100);
    log.info("Створено гравця '{}'", name);
    return currentPlayer;
  }

  @Override
  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  @Override
  public void updatePlayer(Player player) {
    this.currentPlayer = player;
    log.info("Оновлено дані гравця '{}'", player.getName());
  }

  @Override
  public void addScore(int points) {
    if (currentPlayer != null) {
      currentPlayer.setScore(currentPlayer.getScore() + points);
      log.info("Гравець '{}' отримав {} балів, загальний бал: {}", currentPlayer.getName(), points,
          currentPlayer.getScore());
    }
  }

  @Override
  public void applyRisk(int risk) {
    if (currentPlayer != null) {
      int health = currentPlayer.getHealth() - risk;
      currentPlayer.setHealth(Math.max(0, health));
      log.info("Гравець '{}' отримав ризик {}. Здоров'я: {}", currentPlayer.getName(), risk,
          currentPlayer.getHealth());
    }
  }
}
