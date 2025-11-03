package com.game.shadowcircle.service.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.game.shadowcircle.model.Mission;
import lombok.Data;

import java.util.List;

@Data
public class MissionData {
  @JsonProperty("missions")
  private List<Mission> missions;
}
