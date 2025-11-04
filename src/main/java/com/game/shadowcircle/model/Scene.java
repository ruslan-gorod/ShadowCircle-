package com.game.shadowcircle.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Scene {

  private String id;
  private String title;
  private String narrativeText;
  private List<Choice> choices;
  @JsonProperty("isEndingScene")
  private boolean isEndingScene;
  private Mission mission;
}
