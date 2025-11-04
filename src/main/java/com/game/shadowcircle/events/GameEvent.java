package com.game.shadowcircle.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameEvent {

  private String type;       // Тип події, напр. "CHOICE_MADE", "PLAYER_DAMAGED"
  private String message;    // Повідомлення або опис події
  private Object payload;    // Додаткові дані

  public static GameEvent of(String type, String message) {
    return GameEvent.builder().type(type).message(message).build();
  }
}

