package com.game.shadowcircle.factory;

import com.game.shadowcircle.model.Mission;

public interface MissionFactory {

  Mission createMission(String type, Difficulty difficulty);
}
