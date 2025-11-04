package com.game.shadowcircle.service.impl;

import com.game.shadowcircle.model.Enemy;
import com.game.shadowcircle.service.EnemyService;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EnemyServiceImpl implements EnemyService {

  private final List<Enemy> enemies = new ArrayList<>();

  @Override
  public void addEnemy(Enemy enemy) {
    enemies.add(enemy);
    log.info("Додано ворога '{}'", enemy.getName());
  }

  @Override
  public void removeEnemy(Enemy enemy) {
    enemies.remove(enemy);
    log.info("Видалено ворога '{}'", enemy.getName());
  }

  @Override
  public List<Enemy> getAllEnemies() {
    return new ArrayList<>(enemies);
  }

  @Override
  public Enemy getEnemyByName(String name) {
    return enemies.stream()
        .filter(e -> e.getName().equalsIgnoreCase(name))
        .findFirst()
        .orElse(null);
  }

  @Override
  public void neutralizeEnemy(Enemy enemy) {
    enemy.setNeutralized(true);
    log.info("Ворог '{}' нейтралізований", enemy.getName());
  }
}
