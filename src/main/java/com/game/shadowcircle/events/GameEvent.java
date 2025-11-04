package com.game.shadowcircle.events;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameEvent {

  private String type;
  private String message;
  private Object payload;

  @Builder.Default
  private LocalDateTime timestamp = LocalDateTime.now();

  @Builder.Default
  private int severity = 0; // 0-низький, 1-середній, 2-високий, 3-критичний

  public static GameEvent of(String type, String message) {
    return GameEvent.builder()
        .type(type)
        .message(message)
        .timestamp(LocalDateTime.now())
        .severity(0)
        .build();
  }

  public static GameEvent critical(String type, String message, Object payload) {
    return GameEvent.builder()
        .type(type)
        .message(message)
        .payload(payload)
        .timestamp(LocalDateTime.now())
        .severity(3)
        .build();
  }

  public static GameEvent warning(String type, String message) {
    return GameEvent.builder()
        .type(type)
        .message(message)
        .timestamp(LocalDateTime.now())
        .severity(2)
        .build();
  }

  public static GameEvent info(String type, String message) {
    return GameEvent.builder()
        .type(type)
        .message(message)
        .timestamp(LocalDateTime.now())
        .severity(1)
        .build();
  }

  public boolean isCritical() {
    return severity >= 3;
  }

  public boolean isWarning() {
    return severity == 2;
  }

  public boolean isInfo() {
    return severity == 1;
  }
}