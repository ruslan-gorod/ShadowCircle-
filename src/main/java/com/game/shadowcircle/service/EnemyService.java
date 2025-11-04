package com.game.shadowcircle.service;

import com.game.shadowcircle.model.Enemy;
import java.util.List;

public interface EnemyService {

  void addEnemy(Enemy enemy);

  void removeEnemy(Enemy enemy);

  List<Enemy> getAllEnemies();

  Enemy getEnemyByName(String name);

  void neutralizeEnemy(Enemy enemy);
}
