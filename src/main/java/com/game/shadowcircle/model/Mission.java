package com.game.shadowcircle.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mission {

  private String title;
  private String description;
  private List<Scene> scenes;
  private boolean completed;
}

