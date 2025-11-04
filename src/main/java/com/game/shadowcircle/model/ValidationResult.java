package com.game.shadowcircle.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Результат валідації вибору гравця
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationResult {

  private boolean valid;
  private String reason;

  @Builder.Default
  private List<String> errors = new ArrayList<>();

  @Builder.Default
  private List<String> warnings = new ArrayList<>();

  public static ValidationResult valid() {
    return ValidationResult.builder()
        .valid(true)
        .reason("OK")
        .build();
  }

  public static ValidationResult invalid(String reason) {
    return ValidationResult.builder()
        .valid(false)
        .reason(reason)
        .errors(Collections.singletonList(reason))
        .build();
  }

  public static ValidationResult warning(String warning) {
    return ValidationResult.builder()
        .valid(true)
        .reason("OK with warnings")
        .warnings(Collections.singletonList(warning))
        .build();
  }

  public void addError(String error) {
    this.valid = false;
    this.errors.add(error);
  }

  public void addWarning(String warning) {
    this.warnings.add(warning);
  }

  public boolean hasWarnings() {
    return !warnings.isEmpty();
  }

  public boolean hasErrors() {
    return !errors.isEmpty();
  }
}